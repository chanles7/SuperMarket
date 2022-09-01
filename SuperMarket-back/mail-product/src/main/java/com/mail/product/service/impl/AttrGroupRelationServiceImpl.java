package com.mail.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mail.common.util.Query;
import com.mail.product.dao.AttrGroupRelationDao;
import com.mail.product.entity.AttrGroupRelationEntity;
import com.mail.product.service.AttrGroupRelationService;
import com.mail.product.vo.request.AttrGroupRelationReqVO;
import com.mail.product.vo.response.AttrGroupRespVO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mail.common.util.PageUtils;

import javax.annotation.Resource;


@Service("attrGroupRelationService")
public class AttrGroupRelationServiceImpl extends ServiceImpl<AttrGroupRelationDao, AttrGroupRelationEntity> implements AttrGroupRelationService {

    @Resource
    private AttrGroupRelationDao attrGroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupRelationEntity> page = this.page(
                new Query<AttrGroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrGroupRelationEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public AttrGroupRelationEntity getAttrGroupIdByAttrId(Long attrId) {
        return this.query().eq("attr_id", attrId).one();
    }


    @Override
    public AttrGroupRelationEntity getAttrGroupRelation(AttrGroupRelationEntity attrGroupRelation) {
        return query().eq("attr_id", attrGroupRelation.getAttrId()).one();
    }


    @Override
    public void deleteRelation(AttrGroupRelationReqVO[] attrGroupRelationReqVOs) {
        List<AttrGroupRelationEntity> collect = Arrays.stream(attrGroupRelationReqVOs)
                .map(item -> BeanUtil.copyProperties(item, AttrGroupRelationEntity.class))
                .collect(Collectors.toList());
        attrGroupRelationDao.deleteBatchRelation(collect);
    }


    @Override
    public void saveRelation(AttrGroupRelationReqVO[] attrGroupRelationReqVOs) {
        List<AttrGroupRelationEntity> collect = Arrays.stream(attrGroupRelationReqVOs)
                .map(item -> BeanUtil.copyProperties(item, AttrGroupRelationEntity.class))
                .collect(Collectors.toList());
        this.saveBatch(collect);
    }


}