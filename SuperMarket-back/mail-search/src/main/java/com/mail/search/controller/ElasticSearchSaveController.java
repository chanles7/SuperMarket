package com.mail.search.controller;


import com.mail.common.exception.ExceptionEnum;
import com.mail.common.to.es.SkuInfoEsTO;
import com.mail.common.util.R;
import com.mail.search.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("search/save")
public class ElasticSearchSaveController {


    @Resource
    private ProductService productService;


    @PostMapping("skuInfo")
    public R skuInfoUpShelf(@RequestBody List<SkuInfoEsTO> skuInfoEsList) {
        try {
            productService.skuInfoUpShelf(skuInfoEsList);
        } catch (IOException e) {
            return R.error(ExceptionEnum.PRODUCT_UP_EXCEPTION);
        }
        return R.ok();
    }
}
