package com.mail.product.service.impl;


import com.mail.product.entity.CategoryEntity;
import com.mail.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.utils.PageUtils;
import com.mail.common.utils.Query;

import com.mail.product.dao.AttrGroupDao;
import com.mail.product.entity.AttrGroupEntity;
import com.mail.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Resource
    private CategoryService categoryService;


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (categoryId != 0) {
            Set<Long> attrGroupIds = new HashSet<>();
            getAttrGroupIdsByCategoryId(categoryId, attrGroupIds);
            if (attrGroupIds.isEmpty()) {
                return new PageUtils(params);
            }
            wrapper.in("attr_group_id", attrGroupIds);
        }
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and(item -> item.eq("attr_group_id", key).or().like("attr_group_name", key));
        }
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }


    /**
     * 通过商品分类id获取该分类及其子分类所包含的全部商品属性分组id
     *
     * @param categoryId   商品分类id
     * @param attrGroupIds 商品属性分组id
     */
    private void getAttrGroupIdsByCategoryId(Long categoryId, Set<Long> attrGroupIds) {
        CategoryEntity categoryEntity = categoryService.getById(categoryId);
        if (categoryEntity.getCatLevel() < 3) {
            //递归调用自身
            List<CategoryEntity> categoryEntityList = categoryService.getListByParentId(categoryEntity.getCatId());
            categoryEntityList.forEach(item -> getAttrGroupIdsByCategoryId(item.getCatId(), attrGroupIds));
            return;
        }
        List<AttrGroupEntity> attrGroupEntityList = query().eq("catelog_id", categoryId).list();
        if (attrGroupEntityList.size() > 0) {
            attrGroupIds.addAll(attrGroupEntityList
                    .stream()
                    .map(AttrGroupEntity::getAttrGroupId)
                    .collect(Collectors.toSet()));
        }
    }


    @Override
    public List<Long> getChainById(Long attrGroupId) {
        LinkedList<Long> categoryIds = new LinkedList<>();
        AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrGroupId);
        CategoryEntity category = null;
        for (; ; ) {
            if (category == null) {
                category = categoryService.getById(attrGroupEntity.getCatelogId());
            } else {
                if (category.getCatLevel() == 1) break;
                category = categoryService.getById(category.getParentCid());
            }
            categoryIds.addFirst(category.getCatId());
        }
        return categoryIds;
    }
}