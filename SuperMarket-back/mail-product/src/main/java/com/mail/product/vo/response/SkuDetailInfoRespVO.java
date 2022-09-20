package com.mail.product.vo.response;


import com.mail.product.entity.SkuImagesEntity;
import com.mail.product.entity.SkuInfoEntity;
import com.mail.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuDetailInfoRespVO {

    //1、sku基本信息
    private SkuInfoEntity skuInfo;

    //2、sku图片信息
    private List<SkuImagesEntity> imagesList;

    //3、sku的销售属性组合
    private List<AttrVO> saleAttrList;

    //4、spu的介绍
    private SpuInfoDescEntity spuInfoDesc;

    //5、spu的规格参数信息
    private List<AttrGroupVO> attrGroupList;

    //6、分类信息
    private CategoryVo categoryView;

    @Data
    public static class CategoryVo {
        private String category1Name;
        private String category2Name;
        private String category3Name;
    }


    @Data
    public static class AttrVO {
        private Long attrId;
        private String attrName;
        private List<AttrValueVO> attrValueList;
    }


    @Data
    public static class AttrValueVO {
        private String attrValue;
        private Boolean isChecked;
        private List<Long> skuIds;
    }


    @Data
    public static class AttrGroupVO {
        private Long attrGroupId;
        private String attrGroupName;
        private List<AttrVO> attrs;
    }
}
