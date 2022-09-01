package com.mail.product.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mail.product.entity.AttrEntity;
import com.mail.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mail.product.vo.response.AttrGroupRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<AttrEntity> getAttrListNoRelation(@Param("page") Page<AttrEntity> page, @Param("attrGroupId") Long attrGroupId);


    List<AttrGroupRespVO> getAttrTreeByCategoryId(Long categoryId);
}
