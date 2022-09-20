package com.mail.product.controller;

import com.mail.product.entity.SpuInfoEntity;
import com.mail.product.service.SpuInfoService;
import com.mail.product.vo.request.SpuInfoReqVO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * spu信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {


    @Resource
    private SpuInfoService spuInfoService;


    /**
     * 列表
     */
    @RequestMapping("list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }


    /**
     * 保存
     */
    @RequestMapping("save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuInfoReqVO spuInfoReqVO){
		spuInfoService.saveProduct(spuInfoReqVO);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * spu上架
     */
    @PostMapping("{spuId}/shelf/up")
    public R upShelf(@PathVariable Long spuId) {
        spuInfoService.upShelf(spuId);
        return R.ok();
    }

}
