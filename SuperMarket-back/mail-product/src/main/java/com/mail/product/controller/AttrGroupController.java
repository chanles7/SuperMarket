package com.mail.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mail.product.entity.AttrEntity;
import com.mail.product.vo.response.AttrGroupRespVO;
import org.springframework.web.bind.annotation.*;

import com.mail.product.entity.AttrGroupEntity;
import com.mail.product.service.AttrGroupService;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Resource
    private AttrGroupService attrGroupService;

    /**
     * 列表
     */
    @RequestMapping("list/{categoryId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable Long categoryId) {
        PageUtils page = attrGroupService.queryPage(params, categoryId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }


    /**
     * 信息
     */
    @GetMapping("chain/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R chain(@PathVariable("attrGroupId") Long attrGroupId) {
        Long[] path = attrGroupService.getChainById(attrGroupId);
        return R.ok(path);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }


    /**
     * 根据分组id查询关联的属性
     */
    @GetMapping("{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrGroupId) {
        List<AttrEntity> attrEntityList = attrGroupService.getAttrListByRelation(attrGroupId);
        return R.ok(attrEntityList);
    }


    /**
     * 根据分组id查询本分类下还能关联的属性
     */
    @GetMapping("{attrGroupId}/attr/norelation")
    public R attrNoRelation(@RequestParam Map<String, Object> params,
                            @PathVariable Long attrGroupId) {
        PageUtils page = attrGroupService.getAttrListNoRelation(params, attrGroupId);
        return R.ok().put("page", page);
    }


    /**
     * 获取某商品分类下的全部属性分组、规格参数
     */
    @GetMapping("{categoryId}/tree/attr")
    public R getAttrTree(@PathVariable Long categoryId) {
        List<AttrGroupRespVO> attrTree = attrGroupService.getAttrTreeByCategoryId(categoryId);
        return R.ok(attrTree);
    }

}
