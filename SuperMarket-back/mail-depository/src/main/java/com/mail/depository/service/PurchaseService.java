package com.mail.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.depository.entity.PurchaseEntity;
import com.mail.common.vo.request.MergeReqVO;
import com.mail.common.vo.request.PurchaseDoneReqVO;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseEntity> getAssignableList();

    void merge(MergeReqVO mergeReqVO);

    void receive(List<Long> ids);

    void finish(PurchaseDoneReqVO purchaseDoneReqVOs);
}



