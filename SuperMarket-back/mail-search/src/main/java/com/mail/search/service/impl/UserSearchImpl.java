package com.mail.search.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mail.common.to.es.SkuInfoEsTO;
import com.mail.search.service.UserSearchService;
import com.mail.search.vo.request.SearchParamReqVO;
import com.mail.search.vo.response.SearchParamRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mail.search.config.ElasticSearchConfig.COMMON_OPTIONS;
import static com.mail.search.constant.Constant.PRODUCT_INDEX;


@Slf4j
@Service
public class UserSearchImpl implements UserSearchService {


    @Resource
    private RestHighLevelClient client;


    @Override
    public SearchParamRespVO userSearchSkuInfoList(SearchParamReqVO searchParamReqVO) {
        SearchParamRespVO searchParamRespVO;
        try {
            SearchRequest searchRequest = this.buildSearchRequest(searchParamReqVO);
            SearchResponse searchResponse = client.search(searchRequest, COMMON_OPTIONS);
            searchParamRespVO = this.buildSearchResponse(searchResponse, searchParamReqVO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return searchParamRespVO;
    }


    /**
     * 构建检索条件
     *
     * @param param 前端传递的检索条件
     * @return elasticsearch检索条件
     */
    private SearchRequest buildSearchRequest(SearchParamReqVO param) {
        log.info("检索参数为:{}", param);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //模糊匹配，按照商品分类、品牌、价格、库存、属性过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        } else {
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }

        if (param.getCategory3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryId", param.getCategory3Id()));
        }

        if (CollectionUtil.isNotEmpty(param.getBrandId())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }

        if (StringUtils.isNotBlank(param.getPrice()) && param.getPrice().length() <= 3) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            String price = param.getPrice();
            if (price.length() == 3) {
                rangeQueryBuilder.gte(price.charAt(0)).lte(price.charAt(2));
            } else if (price.indexOf("_") == 0) {
                rangeQueryBuilder.lte(price.charAt(1));
            } else {
                rangeQueryBuilder.gte(price.charAt(0));
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        if (param.getHasStock() != null && param.getHasStock()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", true));
        }

        if (CollectionUtil.isNotEmpty(param.getAttrs())) {
            param.getAttrs().forEach(item -> {
                BoolQueryBuilder nestedQueryBoolQueryBuilder = QueryBuilders.boolQuery();
                String[] split = item.split("_");
                String attrId = split[0];
                String[] attrValues = split[1].split(":");
                nestedQueryBoolQueryBuilder.must(QueryBuilders.termQuery("attrId", attrId));
                nestedQueryBoolQueryBuilder.must(QueryBuilders.termsQuery("attrValue", attrValues));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedQueryBoolQueryBuilder, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            });
        }
        searchSourceBuilder.query(boolQueryBuilder);


        //排序、分页、高亮
        if (StringUtils.isNotBlank(param.getSort())) {
            String[] s = param.getSort().split("_");
            searchSourceBuilder.sort(s[0], "asc".equalsIgnoreCase(s[1]) ? SortOrder.ASC : SortOrder.DESC);
        }

        searchSourceBuilder.from((param.getPageNo() - 1) * param.getPageSize());
        searchSourceBuilder.size(param.getPageSize());

        if (StringUtils.isNotBlank(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }


        //聚合分析
        TermsAggregationBuilder categoryAgg = AggregationBuilders.terms("category_agg");
        categoryAgg.field("categoryId").size(10);
        categoryAgg.subAggregation(AggregationBuilders.terms("category_name_agg").field("categoryName").size(1));
        searchSourceBuilder.aggregation(categoryAgg);

        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(10);
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brandAgg);

        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(10);
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));

        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(1));
        attrAgg.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(attrAgg);

        log.info("DSL检索语句为:{}", searchSourceBuilder);
        return new SearchRequest(new String[]{PRODUCT_INDEX}, searchSourceBuilder);
    }


    /**
     * 构建检索结果
     *
     * @param response 检索结果
     * @param param    检索参数
     * @return 封装为SearchParamRespVO对象
     */
    private SearchParamRespVO buildSearchResponse(SearchResponse response, SearchParamReqVO param) {
        log.info("响应结果为：{}", response);

        SearchParamRespVO searchParamRespVO = new SearchParamRespVO();

        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        searchParamRespVO.setTotal(total);
        Integer pageSize = param.getPageSize();
        searchParamRespVO.setTotalPage(total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        searchParamRespVO.setPageNum(param.getPageNo());


        SearchHit[] searchHits = hits.getHits();
        if (ArrayUtil.isNotEmpty(searchHits)) {
            List<SkuInfoEsTO> skuInfoEsList = Arrays.stream(searchHits)
                    .map(hit -> {
                        String sourceAsString = hit.getSourceAsString();
                        SkuInfoEsTO skuInfoEsTO = JSON.parseObject(sourceAsString, SkuInfoEsTO.class);
                        if (StringUtils.isNotBlank(param.getKeyword())) {
                            HighlightField skuTitleHighLight = hit.getHighlightFields().get("skuTitle");
                            String skuTitle = skuTitleHighLight.getFragments()[0].string();
                            skuInfoEsTO.setSkuTitle(skuTitle);
                        }
                        return skuInfoEsTO;
                    })
                    .collect(Collectors.toList());
            searchParamRespVO.setProducts(skuInfoEsList);
        }

        ParsedLongTerms categoryAgg = response.getAggregations().get("category_agg");
        List<? extends Terms.Bucket> buckets = categoryAgg.getBuckets();
        List<SearchParamRespVO.CategoryVo> categoryVoList = buckets.stream()
                .map(bucket -> {
                    SearchParamRespVO.CategoryVo categoryVo = new SearchParamRespVO.CategoryVo();
                    String keyAsString = bucket.getKeyAsString();
                    categoryVo.setCategoryId(Long.parseLong(keyAsString));
                    ParsedStringTerms categoryNameAgg = bucket.getAggregations().get("category_name_agg");
                    String categoryName = categoryNameAgg.getBuckets().get(0).getKeyAsString();
                    categoryVo.setCategoryName(categoryName);
                    return categoryVo;
                })
                .collect(Collectors.toList());
        searchParamRespVO.setCategories(categoryVoList);


        ParsedLongTerms brandAgg = response.getAggregations().get("brand_agg");
        buckets = brandAgg.getBuckets();
        List<SearchParamRespVO.BrandVo> brandVoList = buckets.stream()
                .map(bucket -> {
                    SearchParamRespVO.BrandVo brandVo = new SearchParamRespVO.BrandVo();
                    String keyAsString = bucket.getKeyAsString();
                    brandVo.setBrandId(Long.parseLong(keyAsString));
                    ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brand_name_agg");
                    String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
                    brandVo.setBrandName(brandName);
                    ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
                    String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
                    brandVo.setBrandImg(brandImg);
                    return brandVo;
                })
                .collect(Collectors.toList());
        searchParamRespVO.setBrands(brandVoList);


        ParsedNested attrAgg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        buckets = attrIdAgg.getBuckets();
        List<SearchParamRespVO.AttrVo> attrVoList = buckets.stream()
                .map(bucket -> {
                    SearchParamRespVO.AttrVo attrVo = new SearchParamRespVO.AttrVo();
                    String keyAsString = bucket.getKeyAsString();
                    attrVo.setAttrId(Long.parseLong(keyAsString));
                    ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
                    String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
                    attrVo.setAttrName(attrName);
                    ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
                    String attrValue = attrValueAgg.getBuckets().get(0).getKeyAsString();
                    String[] attrValues = attrValue.split(";");
                    attrVo.setAttrValue(Arrays.asList(attrValues));
                    return attrVo;
                })
                .collect(Collectors.toList());
        searchParamRespVO.setAttrs(attrVoList);


        log.info("封装后的结果为：{}", searchParamRespVO);
        return searchParamRespVO;
    }


}
