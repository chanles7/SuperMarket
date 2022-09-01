package com.mail.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mail.product.entity.AttrGroupRelationEntity;
import com.mail.product.vo.response.AttrGroupRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@Mapper
public interface AttrGroupRelationDao extends BaseMapper<AttrGroupRelationEntity> {

    void deleteBatchRelation(@Param("attrGroupRelations") List<AttrGroupRelationEntity> attrGroupRelations);



}
