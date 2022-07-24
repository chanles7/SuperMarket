package com.mail.user.dao;

import com.mail.user.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:44:31
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
