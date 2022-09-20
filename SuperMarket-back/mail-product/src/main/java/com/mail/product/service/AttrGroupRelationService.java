package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.product.vo.request.AttrGroupRelationReqVO;
import com.mail.common.util.PageUtils;
import com.mail.product.entity.AttrGroupRelationEntity;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface AttrGroupRelationService extends IService<AttrGroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AttrGroupRelationEntity getAttrGroupIdByAttrId(Long attrId);

    AttrGroupRelationEntity getAttrGroupRelation(AttrGroupRelationEntity attrGroupRelation);

    void deleteRelation(AttrGroupRelationReqVO[] attrGroupRelationReqVOs);

    void saveRelation(AttrGroupRelationReqVO[] attrGroupRelationReqVOs);
}

