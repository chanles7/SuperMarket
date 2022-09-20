package com.mail.product.controller;

import com.mail.common.util.R;
import com.mail.product.entity.CategoryEntity;
import com.mail.product.service.CategoryService;
import com.mail.product.vo.response.CategoryRespVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    /**
     * 以树形结构返回全部商品分类列表
     */
    @GetMapping("list/tree")
    //@RequiresPermissions("product:category:list")
    public R listAllWithTree() {
        List<CategoryRespVO> list = categoryService.listAllWithTree();
        return R.ok(list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("add")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        return categoryService.saveCategory(category);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        return categoryService.updateCategoryById(category);
    }


    /**
     * 删除商品分类
     */
    @PostMapping("delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds) {
        return categoryService.removeCategories(Arrays.asList(catIds));
    }



    @GetMapping("list")
    //@RequiresPermissions("product:category:delete")
    public R list() {
        List<CategoryEntity> categoryList = categoryService.getAllCategoryList();
        return R.ok(categoryList);
    }

}
