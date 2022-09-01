package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.product.entity.CategoryEntity;
import com.mail.product.vo.response.CategoryRespVO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryRespVO> listAllWithTree();

    R removeCategories(List<Long> catIds);


    R saveCategory(CategoryEntity category);

    R updateCategoryById(CategoryEntity category);


    /**
     * 通过父分类id所有子分类
     * @param parentId 父分类id
     * @return 子分类
     */
    List<CategoryEntity> getListByParentId(Long parentId);

    /**
     * 通过商品分类获取当前分类的完整路径
     * @param categoryId 商品分类id
     * @return 完整路径
     */
    Long[] getCurrentCategoryPath(Long categoryId);
}



