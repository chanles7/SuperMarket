package com.mail.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mail.common.util.PageUtils;
import com.mail.user.entity.MemberStatisticsInfoEntity;

import java.util.Map;

/**
 * 会员统计信息
 *
 * @author ChengHong
 * @email chy52306@163.com
 * @date 2022-07-21 15:44:31
 */
public interface MemberStatisticsInfoService extends IService<MemberStatisticsInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

