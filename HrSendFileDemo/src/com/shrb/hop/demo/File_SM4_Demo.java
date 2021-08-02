package com.shrb.hop.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.alibaba.fastjson.JSONObject;
import com.shrb.hop.utils.FileUtil;
import com.shrb.hop.utils.SM3Util;
import com.shrb.hop.utils.SignUtil;
import com.shrb.hop.utils.sm4.SM4Utils;

/**
 * 开放平台uploadFile/downloadFile调用demo,兼容新老用户
 * 支持MD5/SM3加签算法，老用户只能MD5加签，新用户可选SM3（默认SM3）
 * 文件hash算法同加签算法一致，老用户取全文件hash，新用户取文件前1MB内容hash
 * 支持AES/SM4文件加密算法，也可以不加密
 * SM4加密算法中，支持ECB/CBC两种加密模式，CBC模式需要设置初始化向量
 *
 * @author zhongzijie
 */
public class File_SM4_Demo {
    private static final Log log = LogFactory.getLog(File_SM4_Demo.class);
    /**
     * 设置文件最大加签为1MB，仅对新用户生效，老用户全文件加签
     */
    public static final int MAX_HASH_FILE_SIZE = 1024 * 1024;
    /**
     * true-老用户，false-新用户
     */
    public static final String OLD_FLAG = "false";
    /**
     * 设置文件hash算法和加签算法（MD5/SM3）,该字段只有新用户有效，老用户固定MD5
     */
    public static final String HASH_FILE_METHOD = "SM3";
    /**
     * 设置文件加密方式（AES/SM4/NONE），NONE的情况下不加密
     */
    public static final String ENCRY_FILE_METHOD = "SM4";
    /**
     * 设置文件加密模式（ECB/CBC），仅支持SM4加密算法
     */
    public static final String ENCRY_FILE_MODE = "ECB";
    /**
     * 设置CBC模式下的初始化向量
     */
    public static final String ENCRY_FILE_INIT_VECTOR = "UISwD9fW6cFh9SNS";
    /**
     * 设置文件密钥
     */
    public static final String ENCRY_FILE_KEY = "11HDESaAhiHHugDt";
    /**
     * 是否自定义上传文件的目录，如果为true，uploadFile接口需要上送filePath字段，如果为false，则不需要上送filePath
     */
    public static final boolean IS_CUSTOMIZE_UPLOAD_FILE_PATH = true;
    /**
     * 自定义上传文件的目录，示例：aaaa/bbbb/yyyyMMdd
     */
    public static final String CUSTOMIZE_UPLOAD_FILE_PATH = "308501/PARTNERRECORDFILE/20210128";
    /**
     * 上传接口实际上送的filePath
     */
    public static final String UPLOAD_FILE_PATH = IS_CUSTOMIZE_UPLOAD_FILE_PATH ? CUSTOMIZE_UPLOAD_FILE_PATH : "";


    public static void uploadFile() throws Exception {
        String hashFileMethod = HASH_FILE_METHOD;

        // 1.生成加密文件
        // 未加密的源文件地址
        String srcPath = "D:\\fileserver\\upload\\text.txt";
        // 生成的加密文件地址
        String destPath = "D:\\fileserver\\upload\\text.txt";
        // 文件密钥：合作方的文件密钥由华瑞侧生成并提供，对合作方而言，所有测试环境文件密钥为同一个，但生产环境文件密钥会单独给出
        String key = ENCRY_FILE_KEY;
        String uploadFilePath = "";
        if ("AES".equalsIgnoreCase(ENCRY_FILE_METHOD)) {
            FileUtil.operateFile("0", new File(srcPath), destPath, key);
            uploadFilePath = destPath;
        } else if ("SM4".equalsIgnoreCase(ENCRY_FILE_METHOD)) {
            if ("ECB".equalsIgnoreCase(ENCRY_FILE_MODE)) {
                FileUtil.operatedFile("0", srcPath, destPath, key);
            } else if ("CBC".equalsIgnoreCase(ENCRY_FILE_MODE)) {
                byte[] content = FileUtils.readFileToByteArray(new File(srcPath));
                byte[] result = null;
                SM4Utils sm4 = new SM4Utils();
                sm4.secretKey = ENCRY_FILE_KEY;
                sm4.iv = ENCRY_FILE_INIT_VECTOR;
                result = sm4.encryptData_CBC(content);
                FileUtils.writeByteArrayToFile(new File(destPath), result);
            } else {
                System.out.println("加密模式设置错误");
                return;
            }

            uploadFilePath = destPath;
        } else {
            uploadFilePath = srcPath;
        }

        // 2.调用请求
        // 上传请求地址（demo提供的是公网地址，合作方应根据对接方式修改地址）
        // 例如接入方式为VPN+HTTPS双认证，修改hosts文件后，将reqUrl的值改成    https://secapi.hulubank.com.cn:444/devportal/ws/file/uploadFile
        // 并且应当引入SSL证书之后才可以正常访问
        String reqUrl = "https://devopen.shrbank.com:4443/devportal/ws/file/uploadFile";//"https://hop.hulubank.com.cn/devportal/ws/file/uploadFile";
        // 应用 id
        String appID = "d12fd828-3268-43bb-a779-c1e447f79bfe";//"def882c4-c0e3-4136-954c-7a00500d1b67";//"883e1ff7-ab96-4425-a878-b3a055981a24";
        // 交易码
        String transcode = "U00000030";
        // 流水号
        String sequenceNo = "KFPTtest00000002";
        // 应用密钥
        String appSecret = "b8ba741f-dfb9-4cee-8418-05869ef99022";//"e396e841-e999-423a-9978-eb4e6d66ec25";//"8be4e213-b90d-4d53-a68c-0235fb44f69e";

        // 3.生成签名
        File file = new File(uploadFilePath);
        String sign = null;
        String hash = null;
        try {
            byte[] content = FileUtils.readFileToByteArray(file);
            //判断加密文件是否大于1MB，大于则取前1MB加签，反之则全文件加签
            if ("false".equals(OLD_FLAG)) {
                hash = SignUtil.getHashHexByNew(content, hashFileMethod);
            }
            if ("true".equals(OLD_FLAG)) {
                hash = SignUtil.getHashHexByOld(content);
                //若是老用户，则加签方式固定为MD5
                hashFileMethod = "MD5";
            }

            System.out.println("hash值为：" + hash);
            StringBuilder sb = new StringBuilder();
            sb.append("appID=" + appID + "&");
            sb.append("transcode=" + transcode + "&");
            sb.append("sequenceNo=" + sequenceNo + "&");
            sb.append("appSecret=" + appSecret + "&");
            sb.append("hash=" + hash);
            String signMsg = sb.toString();
            if ("MD5".equals(hashFileMethod)) {
                sign = SignUtil.sign(signMsg.getBytes(), "md5", "").toLowerCase();
            }
            if ("SM3".equals(hashFileMethod)) {
                sign = SM3Util.getSm3HexStr(signMsg.getBytes("UTF-8"));
            }

        } catch (Exception e1) {
            log.error(e1);
        }

        // 4.发送请求
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpPost post = new HttpPost(reqUrl);
            // 准备上传文件，获得源目录下的文件列表
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("appID", appID);
            builder.addTextBody("transcode", transcode);
            builder.addTextBody("filePath", UPLOAD_FILE_PATH);
            builder.addTextBody("sequenceNo", sequenceNo);
            builder.addTextBody("sign", sign);
            // 相当于file标签
            builder.addBinaryBody("file", file);
            builder.setContentType(ContentType.MULTIPART_FORM_DATA);
            post.setEntity(builder.build());
            HttpResponse response = httpclient.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code != 200) {
                log.error("上传失败,响应码:" + response.getStatusLine().getStatusCode());
            } else {
                System.out.println("success");
            }

        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                log.error("上传文件处理异常", e);
            }
        }

    }


    public static void downLoadFile() {
        String hashFileMethod = HASH_FILE_METHOD;
        if ("true".equals(OLD_FLAG)) {
            //若是老用户，则加签方式固定为MD5
            hashFileMethod = "MD5";
        }
        // 下载请求地址（demo提供的是公网地址，合作方应根据对接方式修改地址）
        // 例如接入方式为VPN+HTTPS双认证，修改hosts文件后，将reqUrl的值改成    https://secapi.hulubank.com.cn:444/devportal/ws/file/downloadFile
        // 并且应当引入SSL证书之后才可以正常访问
        String reqUrl = "https://devopen.shrbank.com:4443/devportal/ws/file/downloadFile";
        // 应用id
        String appID = "d12fd828-3268-43bb-a779-c1e447f79bfe";
        // 交易码
        String transcode = "D00000027";
        // 文件路径
        String filePath = "M00003009/20210126/EPAY_MER_M00003009_20210126.zip";
        // 流水号（保证唯一即可，便于日志追溯）
        String sequenceNo = "TEST1234567";
        // 应用密钥
        String appSecret = "b8ba741f-dfb9-4cee-8418-05869ef99022";
        // 下载文件本地存放地址
        String srcPath = "D:\\fileserver";
        // 下载文件本地存放地址（解密后）
        String destPath = "D:\\fileserver";
        String secretKey = ENCRY_FILE_KEY;

        // 1.加签
        StringBuilder sb = new StringBuilder();
        sb.append("appID=" + appID + "&");
        sb.append("transcode=" + transcode + "&");
        sb.append("filePath=" + filePath + "&");
        sb.append("sequenceNo=" + sequenceNo + "&");
        sb.append("appSecret=" + appSecret);
        String signMsg = sb.toString();
        String sign = null;
        try {
            if ("MD5".equals(hashFileMethod)) {
                sign = SignUtil.sign(signMsg.getBytes(), "md5", "").toLowerCase();
            }
            if ("SM3".equals(hashFileMethod)) {
                sign = SM3Util.getSm3HexStr(signMsg.getBytes("UTF-8"));
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        // 2.发送请求
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        int status = 0;
        try {
            HttpPost post = new HttpPost(reqUrl);
            JSONObject request = new JSONObject();
            request.put("appID", appID);
            request.put("transcode", transcode);
            request.put("filePath", filePath);
            request.put("sequenceNo", sequenceNo);
            request.put("sign", sign);
            ByteArrayEntity baEntity = new ByteArrayEntity(request.toJSONString().getBytes());
            post.setEntity(baEntity);
            post.setHeader("Content-Type", "application/json");
            // 发送请求
            HttpResponse httpResponse = httpclient.execute(post);
            status = httpResponse.getStatusLine().getStatusCode();
            File file = null;
            if (status == 200) {
                HttpEntity entity = httpResponse.getEntity();
                // 接口调用成功,保存文件
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedInputStream bis = new BufferedInputStream(instream);
                    // 文件生成路径
                    file = new File(srcPath);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    int inByte;
                    while ((inByte = bis.read()) != -1) {
                        bos.write(inByte);
                    }
                    bos.close();
                    bis.close();
                }

                // 验签
                Header[] signHeaders = httpResponse.getHeaders("sign");
                String rspSign = signHeaders[0].getValue();
                Header[] timestampHeaders = httpResponse.getHeaders("timestamp");
                String timestamp = timestampHeaders[0].getValue();
                byte[] buffer = FileUtils.readFileToByteArray(file);
                String hash = SignUtil.sign(buffer, "md5", "").toLowerCase();
                StringBuilder rspSb = new StringBuilder();
                rspSb.append("appSecret=" + appSecret + "&");
                rspSb.append("hash=" + hash + "&");
                rspSb.append("timestamp=" + timestamp);
                String rspSignMsg = rspSb.toString();
                String sign2 = SignUtil.sign(rspSignMsg.getBytes(), "md5", "").toLowerCase();
                if (!sign2.equals(rspSign)) {
                    throw new Exception("验签失败!");
                }
            } else if (status == 401) {
                log.error("appID/sysName不正确！");
            } else if (status == 404) {
                log.error("请求的文件不存在");
            } else {
                log.error("未知错误: " + httpResponse.getStatusLine());
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                log.error("下载文件处理异常：", e);
            }
        }

        if (status == 200) {
            if ("AES".equalsIgnoreCase(ENCRY_FILE_METHOD)) {
                FileUtil.operateFile("1", new File(srcPath), destPath, secretKey);
            } else if ("SM4".equalsIgnoreCase(ENCRY_FILE_METHOD)) {
                FileUtil.operatedFile("1", srcPath, destPath, secretKey);
            }

            System.out.println("success");
        }

    }


    public static void main(String[] args) throws Exception {
        //uploadFile();
        downLoadFile();
    }

}
