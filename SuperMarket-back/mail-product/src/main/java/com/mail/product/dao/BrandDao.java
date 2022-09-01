package com.mail.product.dao;

import com.mail.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mail.product.vo.response.BrandRespVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 品牌
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {


}
