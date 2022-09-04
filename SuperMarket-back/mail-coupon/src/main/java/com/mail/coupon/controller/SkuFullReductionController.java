package com.mail.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.mail.common.to.SkuReductionTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mail.coupon.entity.SkuFullReductionEntity;
import com.mail.coupon.service.SkuFullReductionService;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;



/**
 * 商品满减信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:39:50
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:skufullreduction:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:skufullreduction:info")
    public R info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("save")
    //@RequiresPermissions("coupon:skufullreduction:save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }


    @PostMapping("saveInfo")
    //@RequiresPermissions("coupon:skufullreduction:save")
    public R saveInfo(@RequestBody SkuReductionTO skuReductionTO){
        skuFullReductionService.saveInfo(skuReductionTO);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:skufullreduction:update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:skufullreduction:delete")
    public R delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
