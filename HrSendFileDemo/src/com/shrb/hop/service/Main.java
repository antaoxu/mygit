package com.shrb.hop.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import com.shrb.hop.utils.FileUtil;
import com.shrb.hop.utils.SignUtil;

public class Main {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {


        // 上传源文件地址
        String srcPath = "Z:\\bbb.txt";
        // 上传加密文件地址
        String destPath = "Z:\\bbb1.txt";
        // 加密密钥：
        // sm4密钥16个字节
        String key = "11HDESaAhiHHugDt";
        // 国密算法SM4
        FileUtil.operatedFile("0", srcPath, destPath, key);


        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] byteArray;
        fis = new FileInputStream(new File(destPath));
        bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        byteArray = bos.toByteArray();

        String msg = new String(byteArray, "ISO-8859-1");
        byte[] byteArray1 = msg.getBytes("ISO-8859-1");

        System.out.println(byteArrToHex(byteArray));
        System.out.println("===");
        System.out.println(byteArrToHex(byteArray1));


//		String msg1="123123123asfag123123124123e12eqasdfasdgasfdasgasgasgasdfagasga";
//		byte[] bytes = msg1.getBytes();
//		System.out.println(byteArrToHex(bytes));
//		
//		
//		String msg2 = new String(bytes);
//		System.out.println(msg1);
//		System.out.println(byteArrToHex(msg2.getBytes()));


//		String hash = SignUtil.sign(string, "md5", "").toLowerCase();
//		byte[] bytes = FileUtils.readFileToByteArray(new File("Z:\\bbb.rar"));
//		String hash = SignUtil.sign(bytes, "md5", "").toLowerCase();
//		System.out.println(hash);
    }


    public static String byteArrToHex(byte[] btArr) {
        char strArr[] = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = HexCharArr[bt >>> 4 & 0xf];
            strArr[i++] = HexCharArr[bt & 0xf];
        }
        return new String(strArr);
    }


    private static final char HexCharArr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String HexStr = "0123456789abcdef";
}
