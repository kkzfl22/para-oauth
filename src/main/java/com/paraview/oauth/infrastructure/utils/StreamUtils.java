package com.paraview.oauth.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * 公共的流的关闭方法
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/09/10
 */
public class StreamUtils {

    private static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    public static void close(Closeable close) {
        if (null != close) {
            try {
                close.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("close exception", e);
            }
        }
    }

}
