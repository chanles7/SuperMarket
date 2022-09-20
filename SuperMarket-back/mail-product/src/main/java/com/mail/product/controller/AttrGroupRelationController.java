package com.mail.product.controller;

import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.product.entity.AttrGroupRelationEntity;
import com.mail.product.service.AttrGroupRelationService;
import com.mail.product.vo.request.AttrGroupRelationReqVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 属性&属性分组关联
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
@RestController
@RequestMapping("product/attrgrouprelation")
public class  AttrGroupRelationController {

    @Resource
    private AttrGroupRelationService attrGroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attrgrouprelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:attrgrouprelation:info")
    public R info(@PathVariable("id") Long id){
        AttrGroupRelationEntity attrAttrgroupRelation = attrGroupRelationService.getById(id);
        return R.ok().put("attrAttrgroupRelation", attrAttrgroupRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:attrgrouprelation:save")
    public R saveRelation(@RequestBody AttrGroupRelationReqVO[] attrGroupRelationReqVOs){
        attrGroupRelationService.saveRelation(attrGroupRelationReqVOs);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgrouprelation:update")
    public R update(@RequestBody AttrGroupRelationEntity attrAttrgroupRelation){
        attrGroupRelationService.updateById(attrAttrgroupRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("product:attrgrouprelation:delete")
    public R deleteRelation(@RequestBody AttrGroupRelationReqVO[] attrGroupRelationReqVOs){
        attrGroupRelationService.deleteRelation(attrGroupRelationReqVOs);
        return R.ok();
    }

}
