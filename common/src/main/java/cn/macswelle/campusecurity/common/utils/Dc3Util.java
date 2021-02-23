/*
 * Copyright 2018-2020 Pnoker. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.macswelle.campusecurity.common.utils;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * Dc3 平台自定义工具类集合
 *
 * @author pnoker
 */
@Slf4j
public class Dc3Util {

    /**
     * 将字节流进行Base64编码
     *
     * @param bytes Byte Array
     * @return Byte Array
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 必须配合encode使用，用于encode编码之后解码
     *
     * @param str String
     * @return Byte Array
     */
    public static byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * 必须配合encode使用，用于encode编码之后解码
     *
     * @param input Byte Array
     * @return Byte Array
     */
    public static byte[] decode(byte[] input) {
        return Base64.getDecoder().decode(input);
    }

    /**
     * 按小时推迟时间
     *
     * @param amount Integer
     * @param field  Calendar field : Calendar.HOUR/MINUTE/...
     * @return Date
     */
    public static Date expireTime(int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(field, amount);
        return calendar.getTime();
    }
}
