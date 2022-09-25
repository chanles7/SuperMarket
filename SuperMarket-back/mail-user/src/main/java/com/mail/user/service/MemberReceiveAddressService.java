package com.mail.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.to.TransportFeeTO;
import com.mail.common.util.PageUtils;
import com.mail.user.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:44:31
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 根据用户Id查询收货地址信息
     * @param userId 用户Id
     * @return
     */
    List<MemberReceiveAddressEntity> getAddressListByUserId(String userId);


    /**
     * 更新地址默认状态
     * @param addressId 地址id
     */
    void updateState(String addressId);


    /**
     * 通过用户地址id计算运费并返回地址信息
     * @param addressId 地址id
     * @return
     */
    TransportFeeTO getTransportFeeByAddressId(String addressId);
}

