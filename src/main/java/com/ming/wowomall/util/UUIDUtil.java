package com.ming.wowomall.util;

import org.springframework.util.DigestUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @author m969130721@163.com
 * @date 18-6-20 下午4:25
 */
public class UUIDUtil {
    /**
     * 盐值
     */
    private static final String salt = "９_asASb)/.12$%$!*553}LDSse.";

    /**
     *获取API UUID
     * @return
     */
    public static String createByAPI() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     *获取当前时间数字字符串
     * @return
     */
    public static String createByTime() {

        StringBuilder nowTime = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        for (int i = 0; i < 5; i++) {
            nowTime.append(new Random().nextInt(10));
        }
        return nowTime.toString();
    }

    /**
     * 获取MD5
     * @param string
     * @return
     */
    public static String createMD5(String string) {
        string = salt + string;
        return DigestUtils.md5DigestAsHex(string.getBytes());
    }


}
