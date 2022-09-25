package com.mail.user.dao;

import com.mail.user.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员收货地址
 * 
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:44:31
 */
@Mapper
public interface MemberReceiveAddressDao extends BaseMapper<MemberReceiveAddressEntity> {

    /**
     * 更新收货地址状态
     * @param addressId 地址id
     */
    void updateState(String addressId);
	
}
