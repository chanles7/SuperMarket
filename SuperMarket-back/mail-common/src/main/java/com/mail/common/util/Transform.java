package com.mail.common.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public class Transform {


    public static <T> void insert(Object source, Class<T> clazz, BaseMapper<T> baseMapper){
        T t = BeanUtil.copyProperties(source, clazz);
        baseMapper.insert(t);
    }


    public static <T> void update(Object source, Class<T> clazz, BaseMapper<T> baseMapper){
        T t = BeanUtil.copyProperties(source, clazz);
        baseMapper.updateById(t);
    }
}
