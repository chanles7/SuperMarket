package com.mail.common.to;

import lombok.Data;

import java.util.List;

/**
 * description 功能描述
 *
 * @author ch
 * create 2022/9/25 5:05
 */
@Data
public class OrderSpuInfoTO {

    private SpuInfoTO spuInfoTO;

    /**
     * 销售属性信息
     */
    private List<String> attrs;
}
