package com.mail.depository.dao;

import com.mail.depository.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
