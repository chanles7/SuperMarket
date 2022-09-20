package com.mail.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.product.entity.SpuInfoEntity;
import com.mail.common.util.PageUtils;
import com.mail.product.vo.request.SpuInfoReqVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 14:52:17
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPageByCondition(Map<String, Object> params);

    void saveProduct(SpuInfoReqVO spuInfoReqVO);

    void saveSpuInfo(SpuInfoEntity spuInfo);

    void upShelf(Long spuId);
}

