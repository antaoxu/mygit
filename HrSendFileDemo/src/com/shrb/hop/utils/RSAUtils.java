package com.shrb.hop.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/** *//** 
 * <p> 
 * RSA公钥/私钥/签名工具包 
 * </p> 
 * <p> 
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman） 
 * </p> 
 * <p> 
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/> 
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/> 
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全 
 * </p> 
 *  
 * @author IceWee 
 * @date 2012-4-26 
 * @version 1.0 
 */ 
public class RSAUtils {  

    /** *//** 
     * 加密算法RSA 
     */  
    public static final String KEY_ALGORITHM = "RSA";  

    /** *//** 
     * 签名算法 SHA256WithRSA
     */  
    public static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

    /** *//** 
     * 获取公钥的key 
     */  
    private static final String PUBLIC_KEY = "RSAPublicKey";  

    /** *//** 
     * 获取私钥的key 
     */  
    private static final String PRIVATE_KEY = "RSAPrivateKey";  

    /** *//** 
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 245;

    /** *//** 
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 256;

    /** *//** 
     * <p> 
     * 生成密钥对(公钥和私钥) 
     * </p> 
     *  
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> genKeyPair() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);  
        keyPairGen.initialize(2048);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
        return keyMap;  
    }  

    /** *//** 
     * <p> 
     * 用私钥对信息生成数字签名 
     * </p> 
     *  
     * @param data 已加密数据 
     * @param privateKey 私钥(BASE64编码) 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] data, String privateKey) throws Exception {  
        byte[] keyBytes = Base64Utils.decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());  
    }  

    /** *//** 
     * <p> 
     * 校验数字签名 
     * </p> 
     *  
     * @param data 已加密数据 
     * @param publicKey 公钥(BASE64编码) 
     * @param sign 数字签名 
     *  
     * @return 
     * @throws Exception 
     *  
     */  
    public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
        byte[] keyBytes = Base64Utils.decode(publicKey);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicK = keyFactory.generatePublic(keySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(publicK);  
        signature.update(data);  
        return signature.verify(Base64Utils.decode(sign));  
    }  

    /** *//** 
     * <P> 
     * 私钥解密 
     * </p> 
     *  
     * @param encryptedData 已加密数据 
     * @param privateKey 私钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64Utils.decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  

    /** *//** 
     * <p> 
     * 公钥解密 
     * </p> 
     *  
     * @param encryptedData 已加密数据 
     * @param publicKey 公钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64Utils.decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  

    /** *//** 
     * <p> 
     * 公钥加密 
     * </p> 
     *  
     * @param data 源数据 
     * @param publicKey 公钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64Utils.decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  

    /** *//** 
     * <p> 
     * 私钥加密 
     * </p> 
     *  
     * @param data 源数据 
     * @param privateKey 私钥(BASE64编码) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64Utils.decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  

    /** *//** 
     * <p> 
     * 获取私钥 
     * </p> 
     *  
     * @param keyMap 密钥对 
     * @return 
     * @throws Exception 
     */  
    public static String getPrivateKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PRIVATE_KEY);  
        return Base64Utils.encode(key.getEncoded());  
    }  

    /** *//** 
     * <p> 
     * 获取公钥 
     * </p> 
     *  
     * @param keyMap 密钥对 
     * @return 
     * @throws Exception 
     */  
    public static String getPublicKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PUBLIC_KEY);  
        return Base64Utils.encode(key.getEncoded());  
    }  

    /**
     * 使用 RSA 公钥验签数据
     * @param 	modulus
     * 			RSA 裸公钥，16进制数编码的字符串
     * @param 	exponent
     * 			RSA 公钥指数，通常为 "3" 或 "65537"
     * @param 	data
     * 			待验签的数据
     * @param 	sign
     * 			签名值，16 进制数编码的字符串
     * @return 	验签是否成功
     * @throws 	Exception 验签失败
     */
    public static boolean verifyByRSAWithSHA1(String modulus, String exponent, String data, String sign) throws Exception {
        final BigInteger n = new BigInteger(parseHexStr2Byte(modulus));
        final BigInteger e = new BigInteger(exponent);
        final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        final KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        final RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(keySpec);

        final Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes("utf-8"));
        return signature.verify(parseHexStr2Byte(sign));
    }

    public static boolean verifyRsaSign(String data, String sign, String rsaPublicKeyHex){
        try {
            byte[] bytes = parseHexStr2Byte(rsaPublicKeyHex);
            assert bytes != null;
            BigInteger modules = new BigInteger(bytes);
            BigInteger exponent = new BigInteger("65537");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modules, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(parseHexStr2Byte(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将二进制转换成16进制
     * 
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            //            String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAltDKlTY4I90wkNn4iidZVKmQBcmZ8xjROoPKd6kkpF0KvS978xViV3VjxN1ltAf5wQhUIIf4K+rRF2f/MuAcbnwL19/OhfGgOYw9iyYPIdQKTjyql4pqK699dSTWnGf/MjYeHJ8Vd5x7qqbj2NzzLhsi2rLvlV1KsDLFpw87rw2pT5oBoxlvvHKguao6Wpr8k8CPJQ4vHh3Xe9GRT/UbV0HwaiLIF8/uJMjYE00ZzBeRba3KidFTPHVQqcdO+Kh0uNn5IAFS5hn6twMw9lv6luXnZrfkD7BCh8L5GgLXwws1AgZDwYKFE9awZgwxg2Ta00YeVSWp7Dt3vtYbRvVpmQIDAQAB";
            //            byte[] keyBytes = Base64Utils.decode(publicKey);
            //            System.out.println(parseByte2HexStr(keyBytes));
            //            byte[] keyBytes = Base64Utils.decode(publicKey);  
            //            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
            //            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
            //            PublicKey publicK = keyFactory.generatePublic(keySpec);  
            //            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
            //            signature.initVerify(publicK);  
            //            signature.update(data);  
            //            return signature.verify(Base64Utils.decode(sign));  
            String publicKeyHex = "00C2E3B0B7D940EE912B7E30A1254DFA9062F517E0B85077163F58C5F37A8DBE076C7692C674B8A52EC68F5E75F40CADB0798B1881CDC1EC217FA3C1F62B66CE5BEF9D9E31E48766E9077E1C37C9CB51F52E52D02A6CC22FC61BB4880C83A4A575A23C73A7361B0C9F6D6AB73E2C4E06D12436150D597556726394682B99E646C09DF48B7CB0C66E02ACB1A509073208B76ED7B76295FE6D88C2495E0115A9F63A28EEAAF6EB6054367DC8BAFB75BF7A0A620E1FA6DA2D14B8B571300EDFA36787BD5FC87A79FCCB807B7515233AA92A72576F645F214B36A691A0C001A37D89214F754AECF735949F62FCF776250B3A793E7C3A292479018ECB1ADE46AD4B137D";
            final BigInteger n = new BigInteger(parseHexStr2Byte(publicKeyHex));
            final BigInteger e = new BigInteger("65537");
            final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
            final KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            final RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(keySpec);

            byte[] keyBytes2 = publicKey.getEncoded();

            String publicKeyBase64 = Base64Utils.encode(keyBytes2);
            System.out.println(publicKeyBase64);
            File publicFileBase64 = new File("D:\\work\\20200709\\publicKeyBase64.txt");
            FileWriter fw0 = new FileWriter(publicFileBase64);
            fw0.write(publicKeyBase64);
            fw0.flush();
            fw0.close();
            //            Map<String, Object> map = genKeyPair();
            //            String publicKey = getPublicKey(map);
            //            String privateKey = getPrivateKey(map);
            //            byte[] keyBytes = Base64Utils.decode(publicKey);
            //            String publicKeyHex = parseByte2HexStr(keyBytes);
            //            System.out.println(publicKey);
            //            File publicFile = new File("D:\\work\\20200709\\publicKey.txt");
            //            File publicFileHex = new File("D:\\work\\20200709\\publicKeyHex.txt");
            //            File privateFile = new File("D:\\work\\20200709\\privateKey.txt");
            //            FileWriter fw0 = new FileWriter(publicFile);
            //            fw0.write(publicKey);
            //            fw0.flush();
            //            fw0.close();
            //            FileWriter fw = new FileWriter(publicFileHex);
            //            fw.write(publicKeyHex);
            //            fw.flush();
            //            fw.close();
            //            FileWriter fw1 = new FileWriter(privateFile);
            //            fw1.write(privateKey);
            //            fw1.flush();
            //            fw1.close();
            //            System.out.println(privateKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}  