package com.mail.product.controller;

import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.product.service.AttrService;
import com.mail.product.vo.request.AttrReqVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 商品属性
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Resource
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable Long attrId) {
        return attrService.getAttrInfo(attrId);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrReqVO attrReqVO) {
        attrService.saveAttr(attrReqVO);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrReqVO attrReqVO) {
        attrService.updateAttr(attrReqVO);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }


    @RequestMapping("{attrType}/list/{categoryId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("attrType") String attrType,
                          @PathVariable("categoryId") Long categoryId) {
        PageUtils page = attrService.baseAttrList(params, attrType, categoryId);
        return R.ok().put("page", page);
    }




}
