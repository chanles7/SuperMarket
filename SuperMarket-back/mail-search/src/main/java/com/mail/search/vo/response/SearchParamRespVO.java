package com.mail.search.vo.response;


import com.mail.common.to.es.SkuInfoEsTO;
import lombok.Data;

import java.util.List;


@Data
public class SearchParamRespVO {
    //分页信息
    private Long total;
    private Long totalPage;
    private Integer pageNum;

    private List<SkuInfoEsTO> products;
    private List<CategoryVo> categories;
    private List<BrandVo> brands;
    private List<AttrVo> attrs;


    @Data
    public static class CategoryVo {
        private Long categoryId;
        private String categoryName;
    }


    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }


    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
