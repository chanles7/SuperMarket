package com.mail.user.controller;

import com.mail.common.to.TransportFeeTO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.user.entity.MemberReceiveAddressEntity;
import com.mail.user.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 会员收货地址
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:44:31
 */
@RestController
@RequestMapping("user/memberreceiveaddress")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("user:memberreceiveaddress:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("user:memberreceiveaddress:info")
    public R info(@PathVariable("id") Long id) {
        MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("user:memberreceiveaddress:save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress) {
        memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("user:memberreceiveaddress:update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress) {
        memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("user:memberreceiveaddress:delete")
    public R delete(@RequestBody Long[] ids) {
        memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @GetMapping("list/user")
    public R getAddressListByUserId(@RequestParam String userId) {
        List<MemberReceiveAddressEntity> memberReceiveAddressList = memberReceiveAddressService.getAddressListByUserId(userId);
        return R.ok(memberReceiveAddressList);
    }


    @PostMapping("update/state/{id}")
    public R updateState(@PathVariable("id") String addressId) {
        memberReceiveAddressService.updateState(addressId);
        return R.ok();
    }


    @GetMapping("transport/fee/{id}")
    public R getTransportFeeByAddressId(@PathVariable("id") String addressId) {
        TransportFeeTO transportFeeTo = memberReceiveAddressService.getTransportFeeByAddressId(addressId);
        return R.ok(transportFeeTo);
    }

}
