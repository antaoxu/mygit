package com.shrb.hop.utils.sm4;


import org.apache.commons.codec.binary.Base64;


public class SM4Utils {

    public String secretKey = "";
    public String iv = "";
    private boolean hexString = false;


    public byte[] encryptData_ECB_No64(byte[] plainText) throws Exception {
        SM4Context ctx = new SM4Context();
        ctx.isPadding = true;
        ctx.mode = SM4.SM4_ENCRYPT;

        byte[] keyBytes;
        keyBytes = secretKey.getBytes();
        SM4 sm4 = new SM4();
        sm4.sm4_setkey_enc(ctx, keyBytes);
        byte[] result = sm4.sm4_crypt_ecb(ctx, plainText);
        return result;
    }


    public byte[] decryptData_ECB_No64(byte[] cipherText) throws Exception {
        SM4Context ctx = new SM4Context();
        ctx.isPadding = true;
        ctx.mode = SM4.SM4_DECRYPT;

        byte[] keyBytes;
        keyBytes = secretKey.getBytes();
        SM4 sm4 = new SM4();
        sm4.sm4_setkey_dec(ctx, keyBytes);
        byte[] decrypted = sm4.sm4_crypt_ecb(ctx, cipherText);
        return decrypted;
    }


    public byte[] encryptData_ECB(byte[] plainText) throws Exception {
        SM4Context ctx = new SM4Context();
        ctx.isPadding = true;
        ctx.mode = SM4.SM4_ENCRYPT;

        byte[] keyBytes;
        keyBytes = secretKey.getBytes();
        SM4 sm4 = new SM4();
        sm4.sm4_setkey_enc(ctx, keyBytes);
        byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText);
        byte[] result = Base64.encodeBase64(encrypted);
        return result;
    }

    public byte[] decryptData_ECB(byte[] cipherText) throws Exception {
        SM4Context ctx = new SM4Context();
        ctx.isPadding = true;
        ctx.mode = SM4.SM4_DECRYPT;

        byte[] keyBytes;
        keyBytes = secretKey.getBytes();
        SM4 sm4 = new SM4();
        sm4.sm4_setkey_dec(ctx, keyBytes);
        byte[] decrypted = sm4.sm4_crypt_ecb(ctx,
                Base64.decodeBase64(cipherText));
        return decrypted;
    }

    public byte[] encryptData_CBC(byte[] plainText) throws Exception {
        SM4Context ctx = new SM4Context();
        ctx.isPadding = true;
        ctx.mode = SM4.SM4_ENCRYPT;

        byte[] keyBytes;
        byte[] ivBytes;
        keyBytes = secretKey.getBytes();
        ivBytes = iv.getBytes();

        SM4 sm4 = new SM4();
        sm4.sm4_setkey_enc(ctx, keyBytes);
        byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText);
        byte[] result = Base64.encodeBase64(encrypted);
        return result;
    }

    public byte[] decryptData_CBC(byte[] cipherText) throws Exception {
        SM4Context ctx = new SM4Context();
        ctx.isPadding = true;
        ctx.mode = SM4.SM4_DECRYPT;

        byte[] keyBytes;
        byte[] ivBytes;
        if (hexString) {
            keyBytes = SM4Utils.hexStringToBytes(secretKey);
            ivBytes = SM4Utils.hexStringToBytes(iv);
        } else {
            keyBytes = secretKey.getBytes();
            ivBytes = iv.getBytes();
        }

        SM4 sm4 = new SM4();
        sm4.sm4_setkey_dec(ctx, keyBytes);
        byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes,
                Base64.decodeBase64(cipherText));
        return decrypted;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


//	public static void main(String[] args) throws Exception {
//		//测试代码用例
//		String plainText = "12345678";
//		SM4Utils sm4 = new SM4Utils();
//		sm4.secretKey = "11HDESaAhiHHugDz";
//		plainText.getBytes("UTF-8");
//		System.out.println("ECB模式");
//		byte[] cipherTextByte = sm4.encryptData_ECB(plainText.getBytes("UTF-8"));
//		System.out.println("密文字符串: " + new String(cipherTextByte, "UTF-8"));
//		System.out.println("");
//
//		byte[] plainTextByte = sm4.decryptData_ECB(cipherTextByte);
//		System.out.println("明文字符串: " + new String(plainTextByte, "UTF-8"));
//		System.out.println("");
//
//		System.out.println("CBC模式");
//		sm4.iv = "UISwD9fW6cFh9SNS";
//		cipherTextByte = sm4.encryptData_CBC(plainText.getBytes("UTF-8"));
//		System.out.println("密文字符串: " + new String(cipherTextByte, "UTF-8"));
//		System.out.println("");
//
//		plainTextByte = sm4.decryptData_CBC(cipherTextByte);
//		System.out.println("明文字符串: " + new String(plainTextByte, "UTF-8"));
//
//	}
}
