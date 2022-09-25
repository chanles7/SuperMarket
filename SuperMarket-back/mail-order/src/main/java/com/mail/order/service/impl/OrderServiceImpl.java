package com.mail.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.to.OrderLockTO;
import com.mail.common.to.OrderSpuInfoTO;
import com.mail.common.to.SpuInfoTO;
import com.mail.common.to.TransportFeeTO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;
import com.mail.common.util.R;
import com.mail.common.vo.CartItemVO;
import com.mail.common.vo.request.OrderSubmitReqVO;
import com.mail.common.vo.response.MemberReceiveAddressRespVO;
import com.mail.common.vo.response.OrderConfirmRespVO;
import com.mail.order.dao.OrderDao;
import com.mail.order.entity.OrderEntity;
import com.mail.order.entity.OrderItemEntity;
import com.mail.order.feign.CartFeignService;
import com.mail.order.feign.DepositoryFeignService;
import com.mail.order.feign.ProductFeignService;
import com.mail.order.feign.UserFeignService;
import com.mail.order.interceptor.AuthInterceptor;
import com.mail.order.service.OrderItemService;
import com.mail.order.service.OrderService;
import com.mail.order.util.RedisUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.mail.order.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;


@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {


    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    private CartFeignService cartFeignService;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemService orderItemService;
    @Resource
    private DepositoryFeignService depositoryFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(new Query<OrderEntity>().getPage(params), new QueryWrapper<OrderEntity>());

        return new PageUtils(page);
    }


    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R submit(OrderSubmitReqVO orderSubmitReqVO) {
        log.info("order:{}", orderSubmitReqVO);

        String userId = AuthInterceptor.getUserId();

        //1、验证令牌是否合法
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = orderSubmitReqVO.getToken();
        //通过lua脚本原子验证令牌和删除令牌
        Long result = redisUtils.getRedisTemplate().execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(USER_ORDER_TOKEN_PREFIX + userId), orderToken);
        assert result != null;
        if (result == 0L) {
            return R.error("订单信息过期，请刷新页面再次提交");
        }

        //验证令牌成功
        OrderEntity order = new OrderEntity();
        String orderSn = IdWorker.getTimeId();
        order.setOrderSn(orderSn);
        order.setMemberId(Long.parseLong(userId));

        //获取收货信息
        R r = userFeignService.getTransportFeeByAddressId(orderSubmitReqVO.getAddressId().toString());
        TransportFeeTO transportFee = r.getData(new TypeReference<TransportFeeTO>() {
        });
        MemberReceiveAddressRespVO address = transportFee.getAddress();
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        order.setReceiverPostCode(address.getPostCode());
        order.setFreightAmount(transportFee.getFee());


        //设置订单状态
        order.setStatus(0);
        order.setAutoConfirmDay(7);


        //获取订单项信息
        R r3 = cartFeignService.getCurrentCart();
        OrderConfirmRespVO orderConfirm = r3.getData(new TypeReference<OrderConfirmRespVO>() {
        });
        List<CartItemVO> cartItems = orderConfirm.getCart().getItems();
        if (CollectionUtil.isEmpty(cartItems)) {
            return R.error("请先添加购物车");
        }
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(new BigDecimal("0.0"));
        List<OrderItemEntity> orderItems = cartItems.stream().map(cartItem -> {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderSn(orderSn);

            //设置sku信息
            orderItem.setSkuId(cartItem.getSkuId());
            orderItem.setSkuName(cartItem.getTitle());
            orderItem.setSkuQuantity(cartItem.getCount());
            orderItem.setSkuPic(cartItem.getImage());
            orderItem.setSkuPrice(cartItem.getPrice());

            //设置spu信息
            R r1 = productFeignService.getSpuInfoBySkuId(orderItem.getSkuId());
            OrderSpuInfoTO orderSpuInfo = r1.getData(new TypeReference<OrderSpuInfoTO>() {
            });
            SpuInfoTO spuInfo = orderSpuInfo.getSpuInfoTO();
            orderItem.setSpuId(spuInfo.getId());
            orderItem.setSpuName(spuInfo.getSpuName());
            orderItem.setSpuBrand(spuInfo.getBrandName());
            orderItem.setCategoryId(spuInfo.getCatalogId());

            orderItem.setSkuAttrsVals(StringUtils.join(orderSpuInfo.getAttrs(), ";"));

            //设置积分
            orderItem.setGiftGrowth(orderItem.getSkuPrice().intValue());
            orderItem.setGiftIntegration(orderItem.getSkuPrice().intValue());

            //设置价格
            orderItem.setPromotionAmount(new BigDecimal("0.0"));
            orderItem.setCouponAmount(new BigDecimal("0.0"));
            orderItem.setIntegrationAmount(new BigDecimal("0.0"));
            BigDecimal origin = orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuQuantity()));
            BigDecimal real = origin.subtract(orderItem.getPromotionAmount()).subtract(orderItem.getCouponAmount()).subtract(orderItem.getIntegrationAmount());
            totalPrice.set(totalPrice.get().add(real));
            orderItem.setRealAmount(real);

            return orderItem;
        }).collect(Collectors.toList());


        //验价
        order.setTotalAmount(totalPrice.get());

        BigDecimal couponAmount = new BigDecimal("0.0");
        BigDecimal integrationAmount = new BigDecimal("0.0");
        BigDecimal promotionAmount = new BigDecimal("0.0");
        BigDecimal totalAmount = new BigDecimal("0.0");
        Integer credit = 0;
        Integer growth = 0;
        for (OrderItemEntity orderItem : orderItems) {
            couponAmount = couponAmount.add(orderItem.getCouponAmount());
            integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount());
            promotionAmount = promotionAmount.add(orderItem.getPromotionAmount());
            totalAmount = totalAmount.add(orderItem.getRealAmount());
            credit += orderItem.getGiftIntegration() * orderItem.getSkuQuantity();
            growth += orderItem.getGiftGrowth() * orderItem.getSkuQuantity();
        }
        order.setCouponAmount(couponAmount);
        order.setIntegrationAmount(integrationAmount);
        order.setPromotionAmount(promotionAmount);
        order.setTotalAmount(totalAmount);
        order.setIntegration(credit);
        order.setGrowth(growth);

        order.setPayAmount(order.getTotalAmount().add(order.getFreightAmount()));
        if (Math.abs(order.getPayAmount().subtract(orderSubmitReqVO.getPayablePrice()).doubleValue()) >= 0.01) {
            return R.error("验价失败");
        }

        order.setNote(orderSubmitReqVO.getRemark());
        order.setDeleteStatus(0);
        order.setCreateTime(new Date());


        //插入数据
        orderDao.insert(order);
        orderItems.forEach(item -> item.setOrderId(order.getId()));
        orderItemService.saveBatch(orderItems);


        OrderLockTO orderLock = new OrderLockTO();
        orderLock.setItems(orderItems.stream()
                .map(orderItem -> {
                    CartItemVO cartItem = new CartItemVO();
                    cartItem.setSkuId(orderItem.getSkuId());
                    cartItem.setCount(orderItem.getSkuQuantity());
                    return cartItem;
                }).collect(Collectors.toList()));
        orderLock.setOrderNo(orderSn);

        R r2 = depositoryFeignService.lockStock(orderLock);
        if (r2.getCode() != 200) {
            return R.error("锁库存失败");
        }

        int a = 10 / 0;

        return R.ok(order.getId());
    }
}