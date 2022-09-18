package com.mail.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mail.common.util.R;
import com.mail.common.util.Transform;
import com.mail.product.entity.AttrGroupEntity;
import com.mail.product.entity.AttrGroupRelationEntity;
import com.mail.product.entity.CategoryEntity;
import com.mail.product.service.AttrGroupRelationService;
import com.mail.product.service.AttrGroupService;
import com.mail.product.service.CategoryService;
import com.mail.product.vo.request.AttrReqVO;
import com.mail.product.vo.response.AttrRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.PageUtils;
import com.mail.common.util.Query;

import com.mail.product.dao.AttrDao;
import com.mail.product.entity.AttrEntity;
import com.mail.product.service.AttrService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import static com.mail.common.constant.product.AttrTypeEnum.*;
import static com.mail.common.constant.product.ProductConstant.*;


@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrGroupRelationService attrGroupRelationService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrDao attrDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public void saveAttr(AttrReqVO attrReqVO) {
        AttrEntity attrEntity = BeanUtil.copyProperties(attrReqVO, AttrEntity.class);
        this.save(attrEntity);
        boolean isBaseType = ATTR_TYPE_BASE.getValue().equals(attrReqVO.getAttrType());
        if (isBaseType) {
            AttrGroupRelationEntity attrGroupRelationEntity = new AttrGroupRelationEntity();
            attrGroupRelationEntity.setAttrGroupId(attrReqVO.getAttrGroupId());
            attrGroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrGroupRelationService.save(attrGroupRelationEntity);
        }
    }


    @Override
    public PageUtils baseAttrList(Map<String, Object> params, String attrType, Long categoryId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        //判断是否为基本属性
        boolean isBaseType = ATTR_TYPE_BASE.getType().equalsIgnoreCase(attrType);
        Integer saleType = isBaseType ? ATTR_TYPE_BASE.getValue() : ATTR_TYPE_SALE.getValue();
        wrapper.eq("attr_type", saleType);

        if (categoryId != 0) {
            wrapper.eq("catelog_id", categoryId);
        }
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and(item -> item.eq("attr_id", key).or().like("attr_name", key));
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVO> attrRespVOS = records
                .stream()
                .map(item -> {
                    AttrRespVO attrRespVO = BeanUtil.copyProperties(item, AttrRespVO.class);
                    if (isBaseType) {
                        AttrGroupEntity attrGroup = attrGroupService.getAttrGroupByAttrId(item.getAttrId());
                        if (attrGroup != null) {
                            attrRespVO.setAttrGroupName(attrGroup.getAttrGroupName());
                        }
                    }
                    CategoryEntity category = categoryService.getById(item.getCatelogId());
                    if (category != null) {
                        attrRespVO.setCategoryName(category.getName());
                    }
                    return attrRespVO;
                })
                .collect(Collectors.toList());
        pageUtils.setList(attrRespVOS);
        return pageUtils;
    }


    @Override
    public R getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.baseMapper.selectById(attrId);
        AttrRespVO attrRespVO = BeanUtil.copyProperties(attrEntity, AttrRespVO.class);
        //设置分类路径
        Long[] path = categoryService.getCurrentCategoryPath(attrRespVO.getCatelogId());
        attrRespVO.setCategoryPath(path);
        //设置分组id
        AttrGroupRelationEntity attrGroupRelation = attrGroupRelationService.getAttrGroupIdByAttrId(attrId);
        if (attrGroupRelation != null) {
            attrRespVO.setAttrGroupId(attrGroupRelation.getAttrGroupId());
        }
        return R.ok().put("attr", attrRespVO);
    }


    @Override
    public void updateAttr(AttrReqVO attrReqVO) {
        Transform.update(attrReqVO, AttrEntity.class, this.baseMapper);
        boolean isBaseType = ATTR_TYPE_BASE.getValue().equals(attrReqVO.getAttrType());
        if (isBaseType) {
            //更新关联表
            AttrGroupRelationEntity attrGroupRelation = BeanUtil.copyProperties(attrReqVO, AttrGroupRelationEntity.class);
            AttrGroupRelationEntity currentRelation = attrGroupRelationService.getAttrGroupRelation(attrGroupRelation);
            if (currentRelation == null) {
                attrGroupRelationService.save(attrGroupRelation);
            } else {
                attrGroupRelation.setId(currentRelation.getId());
                attrGroupRelationService.updateById(attrGroupRelation);
            }
        }
    }


    @Override
    public List<AttrEntity> getAttrListByRelation(Long attrGroupId) {
        return attrDao.getAttrListByRelation(attrGroupId);
    }


    @Override
    public boolean whetherCanBeRetrieved(Long attrId) {
        AttrEntity attr = this.getById(attrId);
        return CAN_BE_RETRIEVED.equals(attr.getSearchType());
    }
}