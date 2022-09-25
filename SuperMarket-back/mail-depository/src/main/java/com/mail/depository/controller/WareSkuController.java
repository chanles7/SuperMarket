package com.mail.depository.controller;

import com.mail.common.to.OrderLockTO;
import com.mail.common.util.PageUtils;
import com.mail.common.util.R;
import com.mail.depository.entity.WareSkuEntity;
import com.mail.depository.service.WareSkuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品库存
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:41:54
 */
@RestController
@RequestMapping("depository/waresku")
public class WareSkuController {

    @Resource
    private WareSkuService wareSkuService;


    /**
     * 列表
     */
    @RequestMapping("list")
    //@RequiresPermissions("depository:waresku:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("depository:waresku:info")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("depository:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("depository:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("depository:waresku:delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 查看有无库存
     */
    @GetMapping("hasStock")
    public R getHasStock(@RequestParam List<Long> ids) {
        Map<Long, Boolean> hasStock = wareSkuService.getHasStock(ids);
        return R.ok(hasStock);
    }


    @GetMapping("stock/num/{skuId}")
    public R getStockNum(@PathVariable Long skuId) {
        Integer number = wareSkuService.getStockNum(skuId);
        return R.ok(number);
    }


    @GetMapping("enough/stock/{skuId}/{needNum}")
    public R getStockEnough(@PathVariable("skuId") Long skuId, @PathVariable("needNum") Integer needNum) {
        Boolean enough = wareSkuService.getStockEnough(skuId, needNum);
        return R.ok(enough);
    }


    @PostMapping("lock/stock")
    public R lockStock(@RequestBody OrderLockTO orderLockTO) {
        return wareSkuService.lockStock(orderLockTO);
    }
}
