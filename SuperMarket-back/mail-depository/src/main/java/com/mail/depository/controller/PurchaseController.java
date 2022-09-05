package com.mail.depository.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mail.depository.vo.request.MergeReqVO;
import com.mail.depository.vo.request.PurchaseDoneReqVO;
import net.sf.jsqlparser.statement.merge.Merge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mail.depository.entity.PurchaseEntity;
import com.mail.depository.service.PurchaseService;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;

import javax.annotation.Resource;


/**
 * 采购信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
@RestController
@RequestMapping("depository/purchase")
public class PurchaseController {

    @Resource
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("depository:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("depository:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("depository:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("depository:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("depository:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 查询可合并整单的采购单列表
     */
    @GetMapping("assignable/list")
    //@RequiresPermissions("depository:purchase:list")
    public R getAssignableList() {
        List<PurchaseEntity> purchaseList = purchaseService.getAssignableList();
        return R.ok(purchaseList);
    }


    /**
     * 合并采购单
     */
    @PostMapping("merge")
    //@RequiresPermissions("depository:purchase:list")
    public R merge(@RequestBody MergeReqVO mergeReqVO) {
        purchaseService.merge(mergeReqVO);
        return R.ok();
    }


    /**
     * 采购人员领取采购单
     */
    @PostMapping("receive")
    //@RequiresPermissions("depository:purchase:list")
    public R receive(@RequestBody List<Long> ids) {
        purchaseService.receive(ids);
        return R.ok();
    }


    /**
     * 完成采购单
     */
    @PostMapping("finish")
    //@RequiresPermissions("depository:purchase:list")
    public R finish(@RequestBody PurchaseDoneReqVO purchaseDoneReqVO) {
        purchaseService.finish(purchaseDoneReqVO);
        return R.ok();
    }
}
