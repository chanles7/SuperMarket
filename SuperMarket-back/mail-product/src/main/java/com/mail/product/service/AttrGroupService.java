package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.product.entity.AttrEntity;
import com.mail.product.entity.AttrGroupEntity;
import com.mail.product.vo.response.AttrGroupRespVO;
import com.mail.common.util.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    Long[] getChainById(Long attrGroupId);

    AttrGroupEntity getAttrGroupByAttrId(Long attrId);

    List<AttrEntity> getAttrListByRelation(Long attrGroupId);

    PageUtils getAttrListNoRelation(Map<String, Object> params, Long attrGroupId);

    List<AttrGroupRespVO> getAttrTreeByCategoryId(Long categoryId);
}

