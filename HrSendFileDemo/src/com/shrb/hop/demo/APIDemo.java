package com.shrb.hop.demo;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author
 * @date 2020-08-24 16:11
 * @apiNote
 */

public class APIDemo {

    /**
     * @URL 合作方请求华瑞的接口地址
     * @AES_PRIVATE_KEY AES密钥
     * @RSA_PRIVATE_KEY 请求加签私钥，base64编码，2048位，PKCS8格式
     * @RSA_PUBLIC_KEY  请求验签公钥，base64编码，2048位，PKCS8格式
     * @RSA256_PUB_KEY  返回验签公钥，由华瑞银行开放平台提供，若appID为自己在华瑞开放平台门户网站注册，则自行登录门户网站进入应用查看
     *
     */

    private static final String URL = "https://devopen.shrbank.com:4443/secapi/user/getVeriCode";
    private static final String APP_ID = "d5da021e-f962-47d6-862e-7842b7523952";
    private static final String ENCRYPT_METHOD = "AES";
    private static final String SIGN_METHOD = "SHA256withRSA";
    private static final String AES_PRIVATE_KEY = "QUVTLTllYjJjNDczLTk3NjgtNDFlOS04ODdkLTYyMmJlNmE2YTVkOA==";

    private static final String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCHqDe9x5zVzVClfLo+ZRPlNBxLcVk/BW6mIFXL2gwJ8kiUxFp3u0xO2wsQ2ilRnkB9poiaEosZ9yHtOzVMLRMSBa8Ql6d/ADOi20DVapLjxIlaZZO1PfisHzLYzsc0rE3hk+Oa1Ki0l2StnsgKb0UdFryE1brXkN0zWrDy/6W2auh2uJJv7h/QOkzjXxCwEeey+tEQojk2RO3r/Hga+t62T8ljDCIn2R9Or5HyHreE8n4Ijwpje0XYQ0bzr5+WpXo0OsRTJp2Gmeg30i8Gyu1n+b7DVuJLOBta6KSdOSqNVncKA+Gk9+xZrGhicyj9zDfka5+y295/TPFiK+Idwy4xAgMBAAECggEAddMbpxArHmAXvcSZlvv/zcH3GWe31k8Hje7j1gzrC/COLfhlgz0AyDTkZZy26pcC+J+eYgPdIY5Ov1f/sMwtlietqEGwT1SDhN3eb/8C2iWoDwXOx0quJAFtDnZ6+zr04gqrU52FcmVUIqCYqsd+RgerccyZXgYKukH5EvYMnmT1CD9OmpH9caHwr9GLyyRdOJwIi1qMpYub4eTwPwG7YgDn3s88qLyNUGMSOOD1sxMpE0vBEAeLi7onrzk6K8FYQuO6q/lF9hyqMGrdtriSD0iZX1x31ZbNGGqP5kx1HaKzFC82Nusx2BPdVXrP/XVsMId+u03OhFtQPpAHEQDuyQKBgQC8wVNU1Wyx2e9MtyQkdfZDG0r41sJAPNDZkkEUHvrS7rErOkc2uKpYPG32oUmx5mJ/fMfTKRPtooHS6G1KJbT/VatL8bFOuwVgt/H+lV/BhUGcNphruzD9+3r/k4fB3n3S1iCRS99QC1xTLF3PDEwbx9QXsXfSLDkmozrO9gp6ewKBgQC3/EpqF5ojWiokMFuTVCEOYCXHX2Vo7oz2KpPnJXK6PlAvHHWbMS9ztm1PPP5L2/4rpxdwPOkWQ331j/kHdgzyxL7Y/qtCOStqRUnATqJ1b7x5+qSLOSvmdpdjUXg5C9pbMC5Ijteu1FO/EkP92EE4UcnTBvTGU5c9H7TGjRVgQwKBgQCMp3M1UCTDEydi/0YLdWug7gGcFEyEGc13k6izGbLG499Z2v+eI1WskrtOgTmtO4RCg/VTwNR8zA/53gEys46AhpIy8VOy22JShD/r+dt1FZuikHxcKw2C9ZbeRE1xhh40siNqoiMyPoT55aGx2/QK74l4bmJfV1zvEcuM1YNRaQKBgAlczlCdyNs5sGFNEKvroQ8BN4rwwNFZKlUwj3w4vKDboOr00MtH8nk59FAO84XauRFxnFC62MWoQ5WmcO6/RCNqfae/NenVzO7IfdtKZRwCxp2ScqMfEqvUpxmHwM1uW/s99Wz9z932shhMDqexRIhgKRwTA4vAHzceEO31qfvPAoGBAJS9Bfy+FdKyujpQhdH358z7j2dW7ItIIz3vtIRbosYsY1J3BlVUVMGlqa/vEHoWV5Bzbnk4eANXpX/KD7T+AOAW2dcAi+3kKVCMA5BWheAhcnEIgYNER8hfkuDec85FtbrV6ETGsRmwpwNOD+QikTx5jV4PcIdqBj1F1VmBOV1d";
    private static final String RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh6g3vcec1c1QpXy6PmUT5TQcS3FZPwVupiBVy9oMCfJIlMRad7tMTtsLENopUZ5AfaaImhKLGfch7Ts1TC0TEgWvEJenfwAzottA1WqS48SJWmWTtT34rB8y2M7HNKxN4ZPjmtSotJdkrZ7ICm9FHRa8hNW615DdM1qw8v+ltmrodriSb+4f0DpM418QsBHnsvrREKI5NkTt6/x4Gvretk/JYwwiJ9kfTq+R8h63hPJ+CI8KY3tF2ENG86+flqV6NDrEUyadhpnoN9IvBsrtZ/m+w1biSzgbWuiknTkqjVZ3CgPhpPfsWaxoYnMo/cw35Gufstvef0zxYiviHcMuMQIDAQAB";

    private static final String RSA256_PUB_KEY = "00B3D7E478E37B08F9D3D7AE8870DE657E7532F8C7404401113E422B9259B1CA9581FBC28443E056B605086D748F06D90B68D45F8C7E07561F928F72E384D6C9BE545EB0211282C01485FF9B4AE156B78F28D96C5DB1F306B88ADEB6BE7DB0EFAC20D0974EACDA607F90534520F4136EE4F8D74364F1E8676443FFAE8FFBCA5CE3585043E64D618FD2A096436C2C8E173673C4CCAF3A794BFF02FCB3F3A773EA475F8152B6B381FC59C58B6D705F035F7EEF0B515DD132615623F3FA3E0C59A075BA136515233E106BECD73EDD7591663273C9E53B5DDD475F425A3ED591D4B8B7DEE29A35F97233D756B65821E63D0EB9ECC6D0964CFEC776A944CB6AE3C9B6D7";

    private static final String SERIAL_TIME_FORMAT = "yyyyMMddHHmmssSSS";

    private static final int STATUS_SUCCESS = 200;

    private static final String RESPONSE_SUCCESS = "000000";

    public static void main(String[] args) {
        // 请求数据先加密再加签
        String request = buildRequest();
        String response = doPost(URL, request);
        // 返回数据先验签再解密
        JSONObject jsonResponse = JSONObject.parseObject(response);
        if (RESPONSE_SUCCESS.equals(jsonResponse.getString("returnCode"))) {
            String sequenceId = jsonResponse.getString("sequenceID");
            String rspData = jsonResponse.getString("rspData");
            String sign = jsonResponse.getString("sign");
            String signData = rspData + sequenceId;
            boolean signFlag = verifyRsa256Sign(signData, sign);
            System.out.println("验签结果：" + signFlag);
            String decryRspData = aesDecrypt(rspData);
            System.out.println(decryRspData);
        }
    }


    /**
     * 生成请求报文
     *
     * @return
     */
    private static String buildRequest() {
        JSONObject request = new JSONObject();
        String sequenceId = getRandomSeqNo();
        request.put("appID", APP_ID);
        request.put("encryptMethod", ENCRYPT_METHOD);
        request.put("signMethod", SIGN_METHOD);
        request.put("sequenceID", sequenceId);
        JSONObject reqData = new JSONObject();
        reqData.put("bindTel", "15295527304");
        reqData.put("operateType","0");
        //reqData.put("productId", "201104001572");
        request.put("reqData", reqData);
        System.out.println("待加密字符串为："+reqData.toJSONString());
        String reqBody = aesEncrypt(reqData.toJSONString());
        request.put("reqData", reqBody);
        System.out.println("加密后字符串为："+reqBody);
        String tobeSign = reqBody + sequenceId;
        System.out.println("待加签字符串为："+tobeSign);
        request.put("sign", getRsaSign(RSA_PRIVATE_KEY, tobeSign));
        return request.toJSONString();
    }

    /**
     * AES加密
     * @param content 需要加密的内容
     * @return 加密之后的结果
     */
    public static String aesEncrypt(String content) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(AES_PRIVATE_KEY.getBytes());
            keyGenerator.init(128, random);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(byteContent);
            return Base64.encode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param content 待解密内容
     * @return 解密之后的结果
     */
    public static String aesDecrypt(String content) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(AES_PRIVATE_KEY.getBytes());
            keyGenerator.init(128, random);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = Base64.decode(content);
            byte[] result = cipher.doFinal(bytes);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对一段String生成RSA加签
     * @param rsaPrivateKey 私钥
     * @param message       参数内容
     * @return 签名后的内容，base64后的字符串
     */
    public static String getRsaSign(String rsaPrivateKey, String message) {
        byte[] result = null;
        // base64解码私钥
        try {
            byte[] decodePrivateKey = java.util.Base64.getDecoder().decode(rsaPrivateKey.replace("\n", ""));
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodePrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(message.getBytes());
            // 生成签名
            result = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // base64编码签名为字符串
        return java.util.Base64.getEncoder().encodeToString(result);
    }

    private static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    private static String getSerialDateString(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(SERIAL_TIME_FORMAT);

        return getDateString(date, dateFormat);
    }

    private static String getRandomSeqNo() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return getSerialDateString(new Date()) + uuid.substring(0, 8);
    }


    /**
     * 资方请求平台
     *
     * @param url     连接地址
     * @param request 请求报文
     * @return 返回报文
     */
    public static String doPost(String url, String request) {
        System.out.println("=========================================================");
        System.out.println("请求链接：" + url);
        System.out.println("请求报文：" + request);
        String response = null;
        HttpResponse httpResponse = null;
        HttpEntity entity = null;
        try {
            long start = System.currentTimeMillis();
            CloseableHttpClient httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            ByteArrayEntity bae = new ByteArrayEntity(request.getBytes(StandardCharsets.UTF_8));
            httpPost.setEntity(bae);
            httpResponse = httpClient.execute(httpPost);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (STATUS_SUCCESS != statusCode) {
                return "{\"errorCode\":\"999999\",\"errorMsg\":\"返回失败\"}";
            }
            entity = httpResponse.getEntity();
            if (entity != null) {
                byte[] bytes = EntityUtils.toByteArray(entity);
                response = new String(bytes, StandardCharsets.UTF_8);
            }
            System.out.println("响应报文：" + response);
            long end = System.currentTimeMillis();
            System.out.println("请求耗时: " + (end - start) + " ms.");
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"returnCode\":\"999999\",\"returnMsg\":\"返回失败\"}";
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return response;
    }


    /**
     * 创建CloseableHttpClient连接，过滤证书认证
     *
     * @return httpClient
     */
    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainConnectionSocketFactory = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainConnectionSocketFactory);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = (x509Certificates, s) -> true;

            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslConnectionSocketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    /**
     * SHA256withRSA 验证签名
     *
     * @param data 数据
     * @param sign 签名
     * @return 验证结果
     */
    public static boolean verifyRsa256Sign(String data, String sign) {
        try {
            byte[] bytes = parseHexStr2Byte(RSA256_PUB_KEY);
            assert bytes != null;
            BigInteger modules = new BigInteger(bytes);
            BigInteger exponent = new BigInteger("65537");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modules, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(parseHexStr2Byte(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     * @return 二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
