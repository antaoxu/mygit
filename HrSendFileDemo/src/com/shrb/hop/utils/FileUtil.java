package com.shrb.hop.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.shrb.hop.utils.sm4.SM4Utils;

public class FileUtil {
    /**
     * @param encryType 0:加密  1：解密
     * @param srcFile   源文件
     * @param destDir   目标文件
     * @param secretKey AES密钥
     * @throws IOException
     * @throws Exception
     */
    public static void operateFile(String encryType, File srcFile, String destDir, String secretKey) {
        // 读原始文件,加密,生成文件
        // file为空,读出来的content也是不会null

        byte[] content;
        try {
//			content = FileUtils.readFileToByteArray(srcFile);
            FileInputStream fis = new FileInputStream(srcFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.close();
            fis.close();
            content = bos.toByteArray();

            byte[] result = null;
            if ("0".equals(encryType)) {
                //加密
                byte[] encrypt = AESUtil.encrypt(new String(content, "utf-8"), secretKey);
                result = Base64.encodeBase64(encrypt);
            } else {
                //解密
                byte[] decode = Base64.decodeBase64(content);
                result = AESUtil.decrypt(decode, secretKey);
            }
            File destFile = new File(destDir);
            FileOutputStream fos = new FileOutputStream(destFile);
            fos.write(result);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type       0:加密
     * @param srcPath    源文件
     * @param encodePath 目标文件
     * @param key        密钥
     */
    public static void operatedFile(String type, String srcPath, String encodePath, String key) {

        try {
            File srcFile = new File(srcPath);
            byte[] content = FileUtils.readFileToByteArray(srcFile);

            byte[] result = null;
            SM4Utils sm4 = new SM4Utils();
            sm4.secretKey = key;
            if ("0".equals(type)) {
                // 加密
                result = sm4.encryptData_ECB_No64(content);
            } else {
                // 解密
                result = sm4.decryptData_ECB_No64(content);
            }
            FileUtils.writeByteArrayToFile(new File(encodePath), result);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
