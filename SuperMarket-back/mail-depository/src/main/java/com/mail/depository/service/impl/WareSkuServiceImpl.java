package com.mail.depository.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.to.OrderLockTO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;
import com.mail.common.util.R;
import com.mail.common.vo.CartItemVO;
import com.mail.common.vo.request.DepositorySkuReqVO;
import com.mail.depository.dao.WareSkuDao;
import com.mail.depository.entity.WareSkuEntity;
import com.mail.depository.feign.ProductFeignService;
import com.mail.depository.service.WareSkuService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private WareSkuDao wareSkuDao;


    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank((String) params.get("wareId")), "ware_id", params.get("wareId"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("skuId")), "sku_id", params.get("skuId"));
        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }


    @Override
    public void addStock(DepositorySkuReqVO depositorySkuReqVO) {
        WareSkuEntity wareSkuEntity = this.query()
                .eq("sku_id", depositorySkuReqVO.getSkuId())
                .eq("ware_id", depositorySkuReqVO.getWareId())
                .one();
        if (wareSkuEntity != null) {
            //存在则更新数量
            wareSkuEntity.setStock(wareSkuEntity.getStock() + depositorySkuReqVO.getStock());
            this.updateById(wareSkuEntity);
        } else {
            //不存在则新增
            wareSkuEntity = BeanUtil.copyProperties(depositorySkuReqVO, WareSkuEntity.class);
            wareSkuEntity.setStockLocked(0);
            try {
                R r = productFeignService.getSkuInfo(wareSkuEntity.getSkuId());
                if (r.getCode() == 200) {
                    Map<String, Object> skuInfo = (Map<String, Object>) r.get("skuInfo");
                    wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                }
            } catch (Exception e) {

            }
            this.save(wareSkuEntity);
        }
    }


    @Override
    public Map<Long, Boolean> getHasStock(List<Long> ids) {
        Map<Long, Boolean> map = new HashMap<>();
        List<Map<String, Object>> mapList = wareSkuDao.getStockBySkuId(ids);
        mapList.forEach(item -> map.put((Long) item.get("key"), (Boolean) item.get("value")));
        return map;
    }


    @Override
    public Integer getStockNum(Long skuId) {
        List<WareSkuEntity> skuStockList = this.query().eq("sku_id", skuId).list();
        AtomicInteger number = new AtomicInteger();
        skuStockList.forEach(item -> number.addAndGet((item.getStock() - item.getStockLocked())));
        return number.get();
    }


    @Override
    public Boolean getStockEnough(Long skuId, Integer needNum) {
        List<WareSkuEntity> skuStockList = this.query().eq("sku_id", skuId).list();
        AtomicReference<Integer> haveNum = new AtomicReference<>(0);
        skuStockList.forEach(item -> {
            haveNum.updateAndGet(v -> v + (item.getStock() - item.getStockLocked()));
        });
        return haveNum.get() >= needNum;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public R lockStock(OrderLockTO orderLockTO) {
        List<CartItemVO> locks = orderLockTO.getItems();

        //查询有货的仓库
        List<SkuHasStock> skuHasStockList = locks.stream()
                .map(cartItemVO -> {
                    SkuHasStock skuHasStock = new SkuHasStock();
                    skuHasStock.setSkuId(cartItemVO.getSkuId());
                    List<Long> wareIds = wareSkuDao.hasStockWareList(cartItemVO.getSkuId());
                    skuHasStock.setWareIds(wareIds);
                    skuHasStock.setCount(cartItemVO.getCount());
                    return skuHasStock;
                })
                .collect(Collectors.toList());


        //锁定库存
        for (SkuHasStock skuHasStock : skuHasStockList) {
            Boolean stockLock = false;
            List<Long> wareIds = skuHasStock.getWareIds();
            if (CollectionUtil.isEmpty(wareIds)) {
                throw new RuntimeException("库存不足");
            }
            for (Long wareId : wareIds) {
                Long count = wareSkuDao.lockSkuStock(skuHasStock.getSkuId(), skuHasStock.getCount(), wareId);
                if (count == 1) {
                    stockLock = true;
                    break;
                }
            }
            if (!stockLock) {
                throw new RuntimeException("库存不足");
            }
        }
        return R.ok();
    }


    @Data
    class SkuHasStock {
        private Long skuId;
        private Integer count;
        private List<Long> wareIds;
    }
}