package com.jay.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;

public class O3Utils {

    public O3Utils() {
        super();
    }

    /**
     * 加密规则:HmacMD5
     */
    private static final String HMAC_MD5 = "HmacMD5";
    
    public static long currentTimeMillis() {
        return System.currentTimeMillis() / 1000;
    }
    
    public static String calcSign(JSONObject request, String secretKey) {
        request.remove(O3.SIGN);
        request.put(O3.SIGN, getSign(request, secretKey));
        return toJson(request);
    }
    
    /**
     * 生成请求唯一标识
     *
     * @param time 时间戳
     * @return 唯一标识
     */
    public static String getTicket() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }

    /**
     * 获取sign
     *
     * @param request_map 用于签名的对象
     * @return 签名字符串
     */
    private static String getSign(Map<String, Object> request_map,
            String secret_key) {
        String json_str = toJson(SortUtils.asc(request_map));
        json_str = chineseToUnicode(json_str);
        return encryptToString(json_str, secret_key);
    }

    /**
     * 所有非ASSIC码字符均转换为对应的Unicode表示
     *
     * @param str 需要转换的字符串
     * @return String
     */
    public static String chineseToUnicode (String str) {

        StringBuilder builder = new StringBuilder("");
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (isChinese(ch) || isChinesePunctuation(ch)) {
                builder.append(gbEncoding(ch));
                continue;
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    /**
     * 根据UnicodeScript方法判断中文字符
     *
     * @param c 中文字符
     * @return true/false
     */
    private static boolean isChinese (char c)
    {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        return sc == Character.UnicodeScript.HAN;
    }

    /**
     * 根据UnicodeBlock方法判断中文标点符号
     *
     * @param c 中文字符标点
     * @return true/false
     */
    public static boolean isChinesePunctuation (char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION 
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS 
                || ub == Character.UnicodeBlock.VERTICAL_FORMS;
    }

    /**
     * 汉字转Unicode
     *
     * @param ch 汉字
     * @return unicode字符串
     */
    private static String gbEncoding(char ch) {
        return "\\u" + Integer.toHexString(ch);
    }

    /***
     * 将对象转换成json字符串
     *
     * @param map 需要转换的对象
     * @return json字符串
     */
    private static String toJson(Map<String, Object> map) {
        return JSONObject.toJSONString(map);
    }

    /**
     * HmacMD5加密 返回字符串
     *
     * @param src 需要加密的字符串
     * @param key 加密的key
     * @return 加密后的数据
     */
    private static String encryptToString(String src, String key) {

        byte[] encrypt_bytes = encrypt(src, key);
        return encrypt_bytes == null ? null : byte2HexString(encrypt_bytes);
    }

    /**
     * HmacMD5加密 返回字节数组
     *
     * @param src 需要加密的字符串
     * @param key 加密的key
     * @return 加密后的数据
     */
    private static byte[] encrypt(String src, String key) {
        // 根据key来构建密钥
        SecretKeySpec sk = new SecretKeySpec(key.getBytes(), HMAC_MD5);
        try {
            // 生成一个MAC
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            // 初始化MAC
            mac.init(sk);
            // 加密src并转化成十六进制字符串
            return mac.doFinal(src.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byte 数组转换为十六进制的字符串
     *
     * @param b 输入需要转换的byte数组
     * @return 返回十六进制 字符串
     */
    private static String byte2HexString(byte[] b) {

        char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f' };
        char[] newChar = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            newChar[2 * i] = hex[(b[i] & 0xf0) >> 4];
            newChar[2 * i + 1] = hex[b[i] & 0xf];

        }
        return new String(newChar);
    }
}
