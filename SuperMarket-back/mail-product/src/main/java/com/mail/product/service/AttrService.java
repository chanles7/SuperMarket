package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.product.entity.AttrEntity;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.product.vo.request.AttrReqVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrReqVO attrReqVO);

    PageUtils baseAttrList(Map<String, Object> params, String attrType, Long categoryId);

    R getAttrInfo(Long attrId);

    void updateAttr(AttrReqVO attrReqVO);

    List<AttrEntity> getAttrListByRelation(Long attrGroupId);

    boolean whetherCanBeRetrieved(Long attrId);

}




