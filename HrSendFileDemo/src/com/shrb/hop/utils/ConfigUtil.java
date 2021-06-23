package com.shrb.hop.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 加载配置文件
 * 
 * @author duxiaoyang
 * 
 */
public class ConfigUtil {

    private static Log log = LogFactory.getLog(ConfigUtil.class);

    public static final String APPID = "appID";
    public static final String APPSECKEY = "appSecKey";
    public static final String ENCRYPTMETHOD = "encryptMethod";
    public static final String SIGNMETHOD = "signMethod";
    public static final String REQUESTURL = "requestUrl";
    public static final String AESKEY = "aesKey";
    public static final String PRIVATEKEY = "privateKey";
    public static final String PUBLICKEY = "publicKey";

    private static ConfigUtil configUtil = null;
    private static Properties properties = new Properties();

    public String getProperties(String key) {
        return properties.getProperty(key);
    }

    public String getProperties(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 以配置文件的方式初始化
     * 
     * @param filePath
     * @return
     */
    public final synchronized static void initConfig(String filePath) {
        if (configUtil == null) {
            init(filePath);
            configUtil = new ConfigUtil();
        }
    }

    /**
     * 以参数方式初始化
     * 
     * @param appID
     * @param appSecKey
     * @param encryptMethod
     *            加密方法，默认是NONE
     * @param signMethod
     *            加签方法，Hash算法，支持：MD5/SHA/SHA-256/SHA-512，RSA算法
     * @param requestUrl
     *            请求地址，默认是http://secapi.hulubank.com.cn
     */
    public final synchronized static void initConfig(String appID, String appSecKey, String encryptMethod,
            String signMethod, String requestUrl, String aesKey, String privateKey, String publicKey) {
        if (configUtil == null) {
            properties.setProperty("appID", appID);
            properties.setProperty("appSecKey", appSecKey);
            properties.setProperty("encryptMethod", encryptMethod);
            properties.setProperty("signMethod", signMethod);
            if (requestUrl != null && requestUrl.length() > 0) {
                properties.setProperty("requestUrl", requestUrl);
            }
            properties.setProperty("aesKey", aesKey);
            properties.setProperty("privateKey", privateKey);
            properties.setProperty("publicKey", publicKey);
            configUtil = new ConfigUtil();
        }
    }

    public final synchronized static ConfigUtil getConfig() {
        if (configUtil == null) {
            configUtil = new ConfigUtil();
        }
        return configUtil;
    }

    private ConfigUtil() {

    }

    private static void init(String filePath) {
        try {
            InputStream is = new FileInputStream(filePath);
            properties.load(is);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("加载user.properties异常", e);
            }
        }
    }
}
