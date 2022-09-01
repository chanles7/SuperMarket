package com.mail.product.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.mail.product.vo.response.BrandRespVO;
import org.springframework.web.bind.annotation.*;

import com.mail.product.entity.CategoryBrandRelationEntity;
import com.mail.product.service.CategoryBrandRelationService;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;

import javax.annotation.Resource;


/**
 * 品牌分类关联
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 获取当前品牌关联的商品分类信息
     * @param brandId
     * @return
     */
    @GetMapping("category/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R categoryList(Long brandId){
        List<CategoryBrandRelationEntity> categoryBrandRelations = categoryBrandRelationService.categoryList(brandId);
        return R.ok(categoryBrandRelations);
    }


    /**
     * 获取当前商品分类关联的品牌列表
     * @param categoryId 商品关联信息
     * @return
     */
    @GetMapping("brand/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R brandList(Long categoryId){
        List<CategoryBrandRelationEntity> categoryBrandRelations = categoryBrandRelationService.brandList(categoryId);
        List<BrandRespVO> collect = categoryBrandRelations.stream()
                .map(item -> BeanUtil.copyProperties(item, BrandRespVO.class))
                .collect(Collectors.toList());
        return R.ok(collect);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);
        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long relationId){
		categoryBrandRelationService.removeById(relationId);
        return R.ok();
    }

}
