package com.mail.depository.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mail.common.constant.depository.PurchaseDetailStatusEnum;
import com.mail.common.constant.depository.PurchaseStatusEnum;
import com.mail.depository.entity.PurchaseDetailEntity;
import com.mail.depository.service.PurchaseDetailService;
import com.mail.depository.service.WareSkuService;
import com.mail.common.vo.request.DepositorySkuReqVO;
import com.mail.common.vo.request.MergeReqVO;
import com.mail.common.vo.request.PurchaseDoneReqVO;
import com.mail.common.vo.request.PurchaseItemDoneReqVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.depository.dao.PurchaseDao;
import com.mail.depository.entity.PurchaseEntity;
import com.mail.depository.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {


    @Resource
    private PurchaseDetailService purchaseDetailService;
    @Resource
    private WareSkuService wareSkuService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );
        return new PageUtils(page);
    }


    @Override
    public List<PurchaseEntity> getAssignableList() {
        return this.query().eq("status", PurchaseStatusEnum.CREATED.getValue()).or().eq("status", PurchaseStatusEnum.ASSIGNED.getValue()).list();
    }


    @Transactional
    @Override
    public void merge(MergeReqVO mergeReqVO) {
        if (mergeReqVO.getPurchaseId() == null) {
            PurchaseEntity purchase = new PurchaseEntity();
            purchase.setCreateTime(new Date());
            purchase.setUpdateTime(new Date());
            purchase.setStatus(PurchaseStatusEnum.CREATED.getValue());
            this.save(purchase);
            mergeReqVO.setPurchaseId(purchase.getId());
        } else {
            //TODO 确认采购单状态
        }


        //更新采购需求状态
        Long purchaseId = mergeReqVO.getPurchaseId();
        List<Long> items = mergeReqVO.getItems();
        List<PurchaseDetailEntity> purchaseDetailList = items.stream()
                .map(purchaseDetailId -> {
                    PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
                    purchaseDetail.setId(purchaseDetailId);
                    purchaseDetail.setPurchaseId(purchaseId);
                    purchaseDetail.setStatus(PurchaseDetailStatusEnum.CREATED.getValue());
                    return purchaseDetail;
                })
                .collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailList);

        //更新采购单状态
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setId(purchaseId);
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);
    }


    @Transactional
    @Override
    public void receive(List<Long> ids) {
        //1、确认当前采购单是新建或已分配状态
        List<PurchaseEntity> purchaseList = ids.stream()
                .map(this::getById)
                .filter(purchase -> PurchaseStatusEnum.CREATED.getValue().equals(purchase.getStatus()) || PurchaseStatusEnum.ASSIGNED.getValue().equals(purchase.getStatus()))
                .peek(purchase -> purchase.setStatus(PurchaseStatusEnum.RECEIVED.getValue()))
                .collect(Collectors.toList());


        //2、改变采购单的状态
        this.updateBatchById(purchaseList);


        //3、改变采购项的状态
        purchaseList.forEach(purchase -> {
            List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailService.listDetailByPurchaseId(purchase.getId());
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailList.stream()
                    .map(purchaseDetail -> {
                        PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                        purchaseDetailEntity.setId(purchaseDetail.getId());
                        purchaseDetailEntity.setStatus(PurchaseDetailStatusEnum.PURCHASING.getValue());
                        return purchaseDetailEntity;
                    })
                    .collect(Collectors.toList());
            purchaseDetailService.updateBatchById(purchaseDetailEntityList);
        });

    }


    @Transactional
    @Override
    public void finish(PurchaseDoneReqVO purchaseDoneReqVO) {
        //1、更新采购选项状态
        List<PurchaseItemDoneReqVO> purchaseDoneItems = purchaseDoneReqVO.getItems();
        AtomicBoolean hasError = new AtomicBoolean();
        List<PurchaseDetailEntity> purchaseDetailList = purchaseDoneItems.stream()
                .map(purchaseDoneItem -> {
                    PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
                    purchaseDetail.setId(purchaseDoneItem.getItemId());
                    if (PurchaseDetailStatusEnum.FINISHED.getValue().equals(purchaseDoneItem.getStatus())) {
                        purchaseDetail.setStatus(PurchaseDetailStatusEnum.FINISHED.getValue());

                        //2、入库
                        PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(purchaseDoneItem.getItemId());
                        DepositorySkuReqVO depositorySkuReqVO = BeanUtil.copyProperties(purchaseDetailEntity, DepositorySkuReqVO.class);
                        depositorySkuReqVO.setStock(purchaseDetailEntity.getSkuNum());
                        wareSkuService.addStock(depositorySkuReqVO);

                    } else {
                        hasError.set(true);
                        purchaseDetail.setStatus(PurchaseDetailStatusEnum.ERROR.getValue());
                    }
                    return purchaseDetail;
                })
                .collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailList);


        //3、更新采购单状态
        this.update().eq("id", purchaseDoneReqVO.getId())
                .set("status", hasError.get() ? PurchaseStatusEnum.ERROR.getValue() : PurchaseStatusEnum.FINISHED.getValue())
                .set("update_time", new Date())
                .update();
    }
}