package com.mail.search.service;

import com.mail.common.to.es.SkuInfoEsTO;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    void skuInfoUpShelf(List<SkuInfoEsTO> skuInfoEsList) throws IOException;

}
