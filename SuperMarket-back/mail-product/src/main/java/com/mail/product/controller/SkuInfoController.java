package com.mail.product.controller;

import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.product.entity.SkuInfoEntity;
import com.mail.product.service.SkuInfoService;
import com.mail.product.vo.response.SkuDetailInfoRespVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * sku信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {

    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @RequestMapping("list")
    //@RequiresPermissions("product:skuinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
        return R.ok(skuInfo);
    }


    /**
     * 通过skuId获取商品详情信息
     */
    @GetMapping("detail/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R detailInfo(@PathVariable("skuId") Long skuId) {
        SkuDetailInfoRespVO skuDetailInfoRespVO = skuInfoService.getDetailInfoBySkuId(skuId);
        return R.ok(skuDetailInfoRespVO);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
