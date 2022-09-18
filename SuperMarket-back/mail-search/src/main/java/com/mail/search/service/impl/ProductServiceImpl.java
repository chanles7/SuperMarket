package com.mail.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.mail.common.to.es.SkuInfoEsTO;
import com.mail.search.config.ElasticSearchConfig;
import com.mail.search.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mail.search.constant.Constant.*;


@Slf4j
@Service("productService")
public class ProductServiceImpl implements ProductService {


    @Resource
    private RestHighLevelClient client;


    @Override
    public void skuInfoUpShelf(List<SkuInfoEsTO> skuInfoEsList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuInfoEsTO sku : skuInfoEsList) {
            IndexRequest indexRequest = new IndexRequest(PRODUCT_INDEX);
            indexRequest.id(sku.getSkuId().toString());
            indexRequest.source(JSON.toJSONString(sku), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);

        boolean hasFailures = bulk.hasFailures();
        if (hasFailures) {
            List<String> collect = Arrays.stream(bulk.getItems())
                    .map(BulkItemResponse::getId)
                    .collect(Collectors.toList());

            log.error("商品sku保存ES错误,{}", collect);
        }

    }


}
