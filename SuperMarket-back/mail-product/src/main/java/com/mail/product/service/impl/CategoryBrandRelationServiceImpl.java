package com.mail.product.service.impl;

import com.mail.product.dao.BrandDao;
import com.mail.product.entity.BrandEntity;
import com.mail.product.entity.CategoryEntity;
import com.mail.product.service.BrandService;
import com.mail.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.product.dao.CategoryBrandRelationDao;
import com.mail.product.entity.CategoryBrandRelationEntity;
import com.mail.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {


    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public List<CategoryBrandRelationEntity> categoryList(Long brandId) {
        return query().eq("brand_id", brandId).list();
    }


    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brandEntity = brandService.getById(categoryBrandRelation.getBrandId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        CategoryEntity categoryEntity = categoryService.getById(categoryBrandRelation.getCatelogId());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }


    @Override
    public List<CategoryBrandRelationEntity> brandList(Long categoryId) {
        return query().eq("catelog_id", categoryId).list();
    }
}