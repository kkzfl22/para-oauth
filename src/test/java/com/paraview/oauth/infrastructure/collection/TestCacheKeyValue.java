/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.paraview.oauth.infrastructure.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 测试缓存数据读取
 *
 * @author liujun
 * @since 2021/5/23
 */
public class TestCacheKeyValue {


    @Test
    public void testCache() {
        CacheKeyValue<String, String> instance = new CacheKeyValue<>();
        instance.put("11", "22");
        Assertions.assertEquals(instance.get("11"), "22");
        //执行保存
        instance.defaultSave();
        //加载
        instance.defaultLoader();
        Assertions.assertEquals(instance.get("11"), "22");

        instance.remove("11");
        instance.put("11", "33");

        Assertions.assertEquals(instance.get("11"), "33");

    }

}
