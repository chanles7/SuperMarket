package com.mail.product.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mail.product.entity.AttrEntity;
import com.mail.product.entity.AttrGroupRelationEntity;
import com.mail.product.entity.CategoryEntity;
import com.mail.product.service.AttrGroupRelationService;
import com.mail.product.service.AttrService;
import com.mail.product.service.CategoryService;
import com.mail.product.vo.response.AttrGroupRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

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
    @Resource
    private AttrGroupRelationService attrGroupRelationService;
    @Resource
    private AttrService attrService;
    @Resource
    private AttrGroupDao attrGroupDao;


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
    public Long[] getChainById(Long attrGroupId) {
        AttrGroupEntity attrGroup = this.baseMapper.selectById(attrGroupId);
        if (attrGroup == null || attrGroup.getCatelogId() == null) {
            return null;
        }
        return categoryService.getCurrentCategoryPath(attrGroup.getCatelogId());
    }


    @Override
    public AttrGroupEntity getAttrGroupByAttrId(Long attrId) {
        AttrGroupRelationEntity attrGroupRelation = attrGroupRelationService.getAttrGroupIdByAttrId(attrId);
        return attrGroupRelation == null ? null : this.baseMapper.selectById(attrGroupRelation.getAttrGroupId());
    }


    @Override
    public List<AttrEntity> getAttrListByRelation(Long attrGroupId) {
        return attrService.getAttrListByRelation(attrGroupId);
    }


    @Override
    public PageUtils getAttrListNoRelation(Map<String, Object> params, Long attrGroupId) {
        Page<AttrEntity> page = new Page<>(Long.parseLong(params.get("page").toString()), Long.parseLong(params.get("limit").toString()));
        List<AttrEntity> attrEntities = attrGroupDao.getAttrListNoRelation(page, attrGroupId);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrEntities);
        return pageUtils;
    }


    @Override
    public List<AttrGroupRespVO> getAttrTreeByCategoryId(Long categoryId) {
        return attrGroupDao.getAttrTreeByCategoryId(categoryId);
    }
}