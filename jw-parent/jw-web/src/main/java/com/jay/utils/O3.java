package com.jay.utils;


public class O3 {

    /**
     * 公钥(可在o3系统开发者信息中生成)
     * 
     * @type string
     * @required true
     */
    public static final String ACCESS_KEY = "access_key";
    /**
     * 命令
     * 
     * @type string
     * @required true
     */
    public static final String CMD = "cmd";
    /**
     * 数据(详见各接口请求参数)
     * 
     * @type map
     * @required true
     */
    public static final String BODY = "body";
    /**
     * 请求唯一标识(UUID)
     * 
     * @type string
     * @required true
     */
    public static final String TICKET = "ticket";
    /**
     * API版本号，当前为v2.0
     * 
     * @type string
     * @required true
     */
    public static final String VERSION = "version";
    /**
     * 请求发起时的unix时间戳
     * 
     * @type int
     * @required true
     */
    public static final String TIME = "time";
    /**
     * 签名
     * 
     * @type string
     * @required true
     */
    public static final String SIGN = "sign";
}
