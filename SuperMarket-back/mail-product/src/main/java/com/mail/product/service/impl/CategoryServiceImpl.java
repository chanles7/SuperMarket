package com.mail.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mail.common.util.R;
import com.mail.product.vo.response.CategoryRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.product.dao.CategoryDao;
import com.mail.product.entity.CategoryEntity;
import com.mail.product.service.CategoryService;

import javax.annotation.Resource;


@Service("categoryService")
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }


    @Override
    public List<CategoryRespVO> listAllWithTree() {
        List<CategoryEntity> categoryEntityList = query().list();
        return this.castListToTree(categoryEntityList, 0L);
    }


    /**
     * 将商品三级分类链表转化为树形结构
     *
     * @param entities 链表
     * @param catId    父目录ID
     * @return 树形结构
     */
    private List<CategoryRespVO> castListToTree(List<CategoryEntity> entities, Long catId) {
        return entities.stream()
                .filter(item -> catId.equals(item.getParentCid()))
                .map(item -> BeanUtil.copyProperties(item, CategoryRespVO.class))
                .peek(item -> {
                    if (item.getCatLevel() < 3) {
                        item.setChildren(this.castListToTree(entities, item.getCatId()));
                    }
                })
                .collect(Collectors.toList());
    }


    @Override
    public R removeCategories(List<Long> catIds) {
        log.info("要删除的商品分类id为:{}", catIds);

        removeByIds(catIds);

        return R.ok();
    }


    @Override
    public R saveCategory(CategoryEntity category) {
        log.info("要添加的商品分类信息为:{}", category);
        category.setShowStatus(1);
        category.setSort(0);

        return R.ok();
    }

    @Override
    public R updateCategoryById(CategoryEntity category) {
        log.info("要修改的商品分类信息为:{}", category);

        //TODO 更新商品分类名称要同时更新关联表的商品分类信息

        return R.ok();
    }


    @Override
    public List<CategoryEntity> getListByParentId(Long parentId) {
        return query().eq("parent_cid", parentId).list();
    }


    @Override
    public Long[] getCurrentCategoryPath(Long categoryId) {
        LinkedList<Long> path = new LinkedList<>();
        getCurrentCategoryPath(categoryId, path);
        return Arrays.copyOfRange(path.toArray(), 0, path.size(), Long[].class);
    }


    private void getCurrentCategoryPath(Long categoryId, LinkedList<Long> path) {
        CategoryEntity category = this.baseMapper.selectById(categoryId);
        if (category == null) return;
        path.addFirst(category.getCatId());
        if (category.getCatLevel() != 0) {
            this.getCurrentCategoryPath(category.getParentCid(), path);
        }
    }
}