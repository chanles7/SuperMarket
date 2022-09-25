package com.mail.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.to.TransportFeeTO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;
import com.mail.common.vo.response.MemberReceiveAddressRespVO;
import com.mail.user.dao.MemberReceiveAddressDao;
import com.mail.user.entity.MemberReceiveAddressEntity;
import com.mail.user.service.MemberReceiveAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {


    @Resource
    private MemberReceiveAddressDao memberReceiveAddressDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public List<MemberReceiveAddressEntity> getAddressListByUserId(String userId) {
        return this.query().eq("member_id", userId).list();
    }


    @Override
    public void updateState(String addressId) {
        this.memberReceiveAddressDao.updateState(addressId);
    }


    @Override
    public TransportFeeTO getTransportFeeByAddressId(String addressId) {
        TransportFeeTO transportFeeTO = new TransportFeeTO();
        MemberReceiveAddressEntity addressEntity = this.getById(addressId);
        transportFeeTO.setAddress(BeanUtil.copyProperties(addressEntity, MemberReceiveAddressRespVO.class));

        BigDecimal transportFee = new BigDecimal(5);
        transportFeeTO.setFee(transportFee);

        return transportFeeTO;
    }
}