package com.mail.product.feign;


import com.mail.common.to.es.SkuInfoEsTO;
import com.mail.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mail-search")
public interface SearchFeignService {


    @PostMapping("search/save/skuInfo")
    R skuInfoUpShelf(@RequestBody List<SkuInfoEsTO> skuInfoEsList);
}
