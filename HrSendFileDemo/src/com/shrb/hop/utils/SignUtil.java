package com.shrb.hop.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.springframework.util.DigestUtils;

public class SignUtil {

    private static enum SignType {
        MD5, SHA1, SHA256, SHA512, RSA
    }

    ;

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    public static String sign(String msg, String signType, String privateKey) throws Exception {
        String signValue = "";
        SignType type = getSignMethod(signType);
        switch (type) {
            case MD5:
                signValue = signByHash(msg.getBytes("utf-8"), "MD5");
                break;
            case SHA1:
                signValue = signByHash(msg.getBytes("UTF-8"), "SHA");
                break;
            case SHA256:
                signValue = signByHash(msg.getBytes("UTF-8"), "SHA-256");
                break;
            case SHA512:
                signValue = signByHash(msg.getBytes("UTF-8"), "SHA-512");
                break;
            case RSA:
                signValue = signByRsa(msg.getBytes("UTF-8"), signType, privateKey);
                break;
            default:
                break;
        }
        return signValue;
    }


    public static String sign(byte[] msg, String signType, String privateKey) throws Exception {
        String signValue = "";
        SignType type = getSignMethod(signType);
        switch (type) {
            case MD5:
                signValue = signByHash(msg, "MD5");
                break;
            case SHA1:
                signValue = signByHash(msg, "SHA");
                break;
            case SHA256:
                signValue = signByHash(msg, "SHA-256");
                break;
            case SHA512:
                signValue = signByHash(msg, "SHA-512");
                break;
            case RSA:
                signValue = signByRsa(msg, signType, privateKey);
                break;
            default:
                break;
        }
        return signValue;
    }

    public static boolean verifySign(String msg, String signType, String publicKey, String sign) throws Exception {
        boolean result = false;
        SignType type = getSignMethod(signType);
        switch (type) {
            case MD5:
                result = verifySignByHash(msg, "MD5", sign);
                break;
            case SHA1:
                result = verifySignByHash(msg, "SHA", sign);
                break;
            case SHA256:
                result = verifySignByHash(msg, "SHA-256", sign);
                break;
            case SHA512:
                result = verifySignByHash(msg, "SHA-512", sign);
                break;
            case RSA:
                result = verifySignByRsa(msg, publicKey, sign);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * RSA签名
     *
     * @param msg
     * @param signType
     * @param privateKey
     * @return
     */
    private static String signByRsa(byte[] msg, String signType, String privateKey) {
        String sign = "";
        try {
            sign = RSAUtils.sign(msg, privateKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * RSA验签
     *
     * @param msg
     * @param publicKey
     * @param sign
     * @return
     */
    private static boolean verifySignByRsa(String msg, String publicKey, String sign) {
        boolean status = false;
        try {
            status = RSAUtils.verifyByRSAWithSHA1(publicKey, "65537", msg, sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!status) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Hash签名
     *
     * @param msg
     * @param signType
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static String signByHash(byte[] msg, String signType)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest sign = MessageDigest.getInstance(signType);

        sign.update(msg);
        byte[] encodeBytes = sign.digest();
        char encodeStr[] = new char[encodeBytes.length * 2];
        int k = 0;
        for (int i = 0; i < encodeBytes.length; i++) {
            byte encodeByte = encodeBytes[i];
            encodeStr[k++] = hexDigits[encodeByte >> 4 & 0xf];
            encodeStr[k++] = hexDigits[encodeByte & 0xf];
        }
        return new String(encodeStr).toUpperCase();
    }

    /**
     * Hash验签
     *
     * @param msg
     * @param signType
     * @param sign
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static boolean verifySignByHash(String msg, String signType, String sign)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance(signType);

        messageDigest.update(msg.getBytes("UTF-8"));
        byte[] encodeBytes = messageDigest.digest();
        char encodeStr[] = new char[encodeBytes.length * 2];
        int k = 0;
        for (int i = 0; i < encodeBytes.length; i++) {
            byte encodeByte = encodeBytes[i];
            encodeStr[k++] = hexDigits[encodeByte >> 4 & 0xf];
            encodeStr[k++] = hexDigits[encodeByte & 0xf];
        }

        String newSign = new String(encodeStr).toUpperCase();
        if (sign.equals(newSign)) {
            return true;
        }
        return false;
    }

    private static SignType getSignMethod(String signType) {
        if (signType.equalsIgnoreCase("MD5")) {
            return SignType.MD5;
        }
        if (signType.equalsIgnoreCase("SHA1")) {
            return SignType.SHA1;
        }
        if (signType.equalsIgnoreCase("SHA256")) {
            return SignType.SHA256;
        }
        if (signType.equalsIgnoreCase("SHA512")) {
            return SignType.SHA512;
        }
        if (signType.equalsIgnoreCase("RSA")) {
            return SignType.RSA;
        }
        return SignType.MD5;
    }

    /**
     * 老用户生成hash
     *
     * @param fileByte
     * @return
     */
    public static String getHashHexByOld(byte[] fileByte) {
        String hex = DigestUtils.md5DigestAsHex(fileByte);
        return hex;
    }

    /**
     * 新用户生成hash
     *
     * @param fileByte
     * @return
     */
    public static String getHashHexByNew(byte[] fileByte, String hashFileMethod) {
        String hex = "";
        if (fileByte.length <= 1048576) {//文件小于1MB
            if ("MD5".equals(hashFileMethod)) {
                hex = DigestUtils.md5DigestAsHex(fileByte);
            }
            if ("SM3".equals(hashFileMethod)) {
                hex = SM3Util.getSm3HexStr(fileByte);
            }
        } else {
            //取文件前1MB
            byte[] neededFileByte = Arrays.copyOf(fileByte, 1048576);
            if ("MD5".equals(hashFileMethod)) {
                hex = DigestUtils.md5DigestAsHex(neededFileByte);
            }
            if ("SM3".equals(hashFileMethod)) {
                hex = SM3Util.getSm3HexStr(neededFileByte);
            }
        }
        return hex;
    }

}
