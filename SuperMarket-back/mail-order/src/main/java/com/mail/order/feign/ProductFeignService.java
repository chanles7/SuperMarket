package com.mail.order.feign;

import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/23 4:05
 */
@FeignClient("mail-product")
public interface ProductFeignService {

    @GetMapping("product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);


    /**
     * 根据skuId获取spu及相关销售属性信息
     * @param skuId 商品id
     * @return
     */
    @PostMapping("product/spuinfo/skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("id") Long skuId);

}
