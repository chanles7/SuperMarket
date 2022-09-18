package com.mail.search.vo.request;


import lombok.Data;

import java.util.List;

@Data
public class SearchParamReqVO {
    //分类ID
    private Long category3Id;
    //搜索的关键字
    private String keyword;
    //品牌Id
    private List<Long> brandId;
    //商品价格区间
    private String price;
    //是否只显示有货
    private Boolean hasStock;
    //平台属性的操作
    private List<String> attrs;
    //排序:按销量、按评分、按价格升序或降序
    private String sort;
    //第几页
    private Integer pageNo;
    //每一页展示条数
    private Integer pageSize;
}
