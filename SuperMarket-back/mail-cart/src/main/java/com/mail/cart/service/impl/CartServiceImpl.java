package com.mail.cart.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mail.cart.feign.DepositoryFeignService;
import com.mail.cart.feign.ProductFeignService;
import com.mail.cart.feign.UserFeignService;
import com.mail.cart.interceptor.AuthInterceptor;
import com.mail.cart.service.CartService;
import com.mail.cart.util.RedisUtils;
import com.mail.common.to.SkuInfoTO;
import com.mail.common.to.TransportFeeTO;
import com.mail.common.to.UserReceiveAddressTO;
import com.mail.common.util.R;
import com.mail.common.vo.CartItemVO;
import com.mail.common.vo.LoginUserVO;
import com.mail.common.vo.response.CartRespVO;
import com.mail.common.vo.response.OrderConfirmRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.mail.cart.constant.CartConstant.*;

/**
 * @author ch
 * description 功能描述
 * create 2022/9/22 0:36
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {


    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private DepositoryFeignService depositoryFeignService;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    private ThreadPoolExecutor executor;


    @Override
    public R addOrUpdateCart(Long skuId, Integer num) {
        CartItemVO cartItemVO = new CartItemVO();
        cartItemVO.setSkuId(skuId);
        cartItemVO.setCheck(true);
        cartItemVO.setCount(num);

        LoginUserVO loginUser = AuthInterceptor.getUser();
        String key;
        if (loginUser.getUserId() != null) {
            key = CART_USER_CACHE_PREFIX + loginUser.getUserId();
        } else {
            key = CART_TMP_CACHE_PREFIX + loginUser.getUserKey();
        }
        BoundHashOperations<String, Object, Object> cartOps = redisUtils.boundHashOps(key);
        //判断是否存在该商品
        String skuJson = (String) cartOps.get(skuId);

        R r = depositoryFeignService.getStockNum(skuId);
        Integer haveNum = r.getData(new TypeReference<Integer>() {
        });

        Integer needNum;
        if (StringUtils.isNotBlank(skuJson)) {
            //存在则更新
            CartItemVO cartItem = JSON.parseObject(skuJson, CartItemVO.class);
            needNum = cartItem.getCount() + cartItemVO.getCount();
            if (haveNum < needNum) {
                return R.error("库存不足");
            }
            if (needNum <= 0) {
                //当数量小于等于0时删除购物车中该商品
                cartOps.delete(skuId);
                return R.ok();
            }
            cartItem.setCount(needNum);
            String json = JSON.toJSONString(cartItem);
            cartOps.put(skuId, json);
        } else {
            //不存在直接新增
            if (haveNum < cartItemVO.getCount()) {
                return R.error("库存不足");
            }
            needNum = cartItemVO.getCount();
            if (needNum <= 0) {
                //当数量小于等于0时删除购物车中该商品
                cartOps.delete(skuId);
                return R.ok();
            }
            String json = JSON.toJSONString(cartItemVO);
            cartOps.put(skuId, json);
        }
        return R.ok();
    }


    @Override
    public R list() {
        LoginUserVO loginUser = AuthInterceptor.getUser();
        String key;
        if (loginUser.getUserId() != null) {
            if (loginUser.getUserKey() != null) {
                key = CART_TMP_CACHE_PREFIX + loginUser.getUserKey();
                try {
                    CartRespVO tmp = this.getCart(key);
                    tmp.getItems().forEach(item -> this.addOrUpdateCart(item.getSkuId(), item.getCount()));
                    redisUtils.delete(key);
                } catch (Exception e) {

                }
            }
            key = CART_USER_CACHE_PREFIX + loginUser.getUserId();
        } else {
            key = CART_TMP_CACHE_PREFIX + loginUser.getUserKey();
        }
        CartRespVO cart = this.getCart(key);
        return R.ok(cart);
    }


    private CartRespVO getCart(String key) {
        BoundHashOperations<String, Object, Object> cartOps = redisUtils.boundHashOps(key);
        //组装购物车响应VO
        CartRespVO cart = new CartRespVO();
        //商品类型
        AtomicInteger countType = new AtomicInteger();
        //商品数量
        AtomicInteger countNum = new AtomicInteger();
        //总价
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(new BigDecimal(0));
        List<Object> jsonList = cartOps.values();
        if (CollectionUtil.isNotEmpty(jsonList)) {
            assert jsonList != null;

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            List<CartItemVO> cartItemList = jsonList.stream()
                    .map(obj -> {
                        String json = (String) obj;
                        CartItemVO cartItem = JSON.parseObject(json, CartItemVO.class);
                        //更新商品基本信息
                        CompletableFuture<Void> skuInfoFuture = CompletableFuture.runAsync(() -> {
                            RequestContextHolder.setRequestAttributes(requestAttributes);
                            R r = productFeignService.getSkuInfo(cartItem.getSkuId());
                            if (r.getCode() == 200) {
                                SkuInfoTO skuInfo = r.getData(new TypeReference<SkuInfoTO>() {
                                });
                                cartItem.setImage(skuInfo.getSkuDefaultImg());
                                cartItem.setPrice(skuInfo.getPrice());
                                cartItem.setTitle(skuInfo.getSkuTitle());
                                cartItem.setTotalPrice();
                                //选中状态更新cart信息
                                if (cartItem.getCheck()) {
                                    countType.getAndIncrement();
                                    countNum.addAndGet(cartItem.getCount());
                                    totalAmount.set(totalAmount.get().add(cartItem.getTotalPrice()));
                                }
                            }
                        }, executor);
                        //查询有无库存
                        CompletableFuture<Void> stockFuture = CompletableFuture.runAsync(() -> {
                            RequestContextHolder.setRequestAttributes(requestAttributes);
                            R r = depositoryFeignService.getStockEnough(cartItem.getSkuId(), cartItem.getCount());
                            if (r.getCode() == 200) {
                                Boolean hasStock = r.getData(new TypeReference<Boolean>() {
                                });
                                cartItem.setHasStock(hasStock);
                            }
                        }, executor);

                        CompletableFuture<Void> future = CompletableFuture.allOf(skuInfoFuture, stockFuture);
                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                        return cartItem;
                    })
                    .collect(Collectors.toList());
            cart.setItems(cartItemList);
        }
        cart.setCountType(countType.get());
        cart.setCountNum(countNum.get());
        cart.setTotalAmount(totalAmount.get());
        //TODO 查询满减
        BigDecimal reduce = new BigDecimal(0);
        cart.setReduce(reduce);
        if (reduce.compareTo(new BigDecimal(0)) == 0) {
            cart.setResAmount(cart.getTotalAmount().subtract(cart.getReduce()));
        }
        return cart;
    }


    @Override
    public R updateCheck(Long skuId, Boolean check) {
        LoginUserVO loginUser = AuthInterceptor.getUser();
        String key;
        if (loginUser.getUserId() != null) {
            key = CART_USER_CACHE_PREFIX + loginUser.getUserId();
        } else {
            key = CART_TMP_CACHE_PREFIX + loginUser.getUserKey();
        }
        BoundHashOperations<String, Object, Object> cartOps = redisUtils.boundHashOps(key);
        String skuJson = (String) cartOps.get(skuId);
        CartItemVO cartItem = JSON.parseObject(skuJson, CartItemVO.class);
        if (ObjectUtil.isEmpty(cartItem)) {
            return R.error("系统异常,请联系管理员");
        }
        assert cartItem != null;
        cartItem.setCheck(check);
        String json = JSON.toJSONString(cartItem);
        cartOps.put(skuId, json);
        return R.ok();
    }


    @Override
    public R getCurrentCart() {
        LoginUserVO loginUser = AuthInterceptor.getUser();
        if (loginUser.getUserId() == null) {
            return R.error("请先登录");
        }
        String userId = loginUser.getUserId();

        //组装OrderConfirmRespVO对象
        OrderConfirmRespVO orderConfirm = new OrderConfirmRespVO();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //1、用户收货地址信息
        AtomicReference<Long> addressId = new AtomicReference<>(0L);
        CompletableFuture<Void> transportFeeFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R r = userFeignService.getAddressListByUserId(userId);
            if (r.getCode() == 200) {
                List<UserReceiveAddressTO> userReceiveAddressList = r.getData(new TypeReference<List<UserReceiveAddressTO>>() {
                });
                orderConfirm.setAddressList(userReceiveAddressList);
                userReceiveAddressList.forEach(item -> {
                    if (item.getDefaultStatus() == 1) {
                        addressId.set(item.getId());
                    }
                });
            }
        }, executor).thenRunAsync(() -> {
            //运费计算
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R r = userFeignService.getTransportFeeByAddressId(addressId.get().toString());
            if (r.getCode() == 200) {
                TransportFeeTO transportFeeTO = r.getData(new TypeReference<TransportFeeTO>() {
                });
                orderConfirm.setTransport(transportFeeTO);
            }
        }, executor);


        //2、购买商品信息
        CompletableFuture<Void> cartFuture = CompletableFuture
                .runAsync(() -> {
                            RequestContextHolder.setRequestAttributes(requestAttributes);
                            orderConfirm.setCart(this.getCart(CART_USER_CACHE_PREFIX + userId));
                        }, executor)
                .thenRunAsync(() -> {
                    CartRespVO cart = orderConfirm.getCart();
                    if (CollectionUtil.isEmpty(cart.getItems())) {
                        throw new RuntimeException("请先添加商品到购物车");
                    }
                    cart.setItems(cart.getItems().stream().filter(CartItemVO::getCheck).collect(Collectors.toList()));
                }, executor);

        //TODO 优惠券信息

        CompletableFuture<Void> future = CompletableFuture.allOf(cartFuture, transportFeeFuture);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        orderConfirm.setPrice(orderConfirm.getCart().getResAmount().add(orderConfirm.getTransport().getFee()));

        String token = UUID.randomUUID().toString().replace("-", "");
        redisUtils.set(USER_ORDER_TOKEN_PREFIX + userId, token, 30L, TimeUnit.MINUTES);
        orderConfirm.setOrderToken(token);


        return R.ok(orderConfirm);
    }
}
