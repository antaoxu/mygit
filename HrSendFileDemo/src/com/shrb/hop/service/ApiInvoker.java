package com.shrb.hop.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.shrb.hop.beans.RequestBean;
import com.shrb.hop.beans.RequestBody;
import com.shrb.hop.beans.ResponseBean;
import com.shrb.hop.beans.ResponseBody;
import com.shrb.hop.beans.ValidateResult;
import com.shrb.hop.utils.AESUtil;
import com.shrb.hop.utils.BeanEnum;
import com.shrb.hop.utils.ConfigUtil;
import com.shrb.hop.utils.SequenceUtil;
import com.shrb.hop.utils.SignUtil;
import com.shrb.hop.utils.ValidateUtil;

public class ApiInvoker implements Api {

    private Log log = LogFactory.getLog(ApiInvoker.class);

    private RequestBean getHeader() {
        RequestBean header = new RequestBean();
        header.setAppID(ConfigUtil.getConfig().getProperties(ConfigUtil.APPID));
        header.setEncryptMethod(ConfigUtil.getConfig().getProperties(ConfigUtil.ENCRYPTMETHOD));
        header.setSequenceID(SequenceUtil.getInstance().generate());
        header.setSignMethod(ConfigUtil.getConfig().getProperties(ConfigUtil.SIGNMETHOD));
        return header;
    }

    public String getRequestString(RequestBody requestBody, BeanEnum beanEnum) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("华瑞API开始调用");
        }
        // 校验Body体
        List<ValidateResult> validateBody = ValidateUtil.validate(requestBody);
        if (validateBody != null && !validateBody.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ValidateResult va : validateBody) {
                if (!va.isValid()) {
                    str.append(va.getMessage()).append(";");
                }
            }

            String errorMsg = "校验reqData失败: " + str.toString();
            if (log.isErrorEnabled()) {
                log.error(errorMsg);
            }
            throw new Exception(errorMsg);
        }


        // 创建报文头，合并报文体和报文头，并校验
        RequestBean requestBean = this.getHeader();
        requestBean.setReqData(requestBody);
        List<ValidateResult> validateHead = ValidateUtil.validate(requestBean);
        if (validateHead != null && !validateHead.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ValidateResult va : validateHead) {
                if (!va.isValid()) {
                    str.append(va.getMessage()).append(";");
                }
            }
            String errorMsg = "校验失败: " + str.toString();
            if (log.isErrorEnabled()) {
                log.error(errorMsg);
            }
            throw new Exception(errorMsg);
        }

        // 数据加密
        String reqData = JSONObject.toJSONString(requestBody);
        StringBuilder sbReq = new StringBuilder();
        if (requestBean.getEncryptMethod().equalsIgnoreCase("AES")) {
            String encryptReqData = AESUtil.encryptAndBase64Encode(reqData,
                    ConfigUtil.getConfig().getProperties(ConfigUtil.AESKEY));
            sbReq.append(encryptReqData);
        } else {
            sbReq.append(reqData);
        }
        sbReq.append(requestBean.getSequenceID());

        // 数据签名
        if (!requestBean.getSignMethod().equalsIgnoreCase("RSA")) {
            sbReq.append(ConfigUtil.getConfig().getProperties(ConfigUtil.APPSECKEY));
        }
        if (log.isDebugEnabled()) {
            log.debug("待签名数据：" + sbReq.toString());
        }
        String sign = SignUtil.sign(sbReq.toString().getBytes(), requestBean.getSignMethod(),
                ConfigUtil.getConfig().getProperties(ConfigUtil.PRIVATEKEY));
        requestBean.setSign(sign);
        String strReq = JSONObject.toJSONString(requestBean);
        if (log.isDebugEnabled()) {
            log.debug("签名后数据：" + strReq);
        }
        return strReq;
    }

    public ResponseBean getResponseBean(String responseMessage, BeanEnum beanEnum) throws Exception {
        // 响应数据
        JSONObject jsonObjRsp = JSONObject.parseObject(responseMessage, Feature.OrderedField);
        StringBuilder sbRsp = new StringBuilder();
        sbRsp.append(jsonObjRsp.get("rspData"));
        sbRsp.append(jsonObjRsp.get("sequenceID"));
        if (!jsonObjRsp.getString("signMethod").equalsIgnoreCase("RSA")) {
            sbRsp.append(ConfigUtil.getConfig().getProperties(ConfigUtil.APPSECKEY));
        }
        if (log.isDebugEnabled()) {
            log.debug("待验签数据：" + sbRsp.toString());
        }

        //响应验签
        boolean verifySignResult = SignUtil.verifySign(sbRsp.toString(), jsonObjRsp.getString("signMethod"),
                ConfigUtil.getConfig().getProperties(ConfigUtil.PUBLICKEY), jsonObjRsp.getString("sign"));
        if (!verifySignResult) {
            String errorMsg = "响应数据验签失败";
            throw new Exception(errorMsg);
        }

        //响应解密
        if (jsonObjRsp.getString("encryptMethod").equalsIgnoreCase("AES")) {
            String rspData = jsonObjRsp.get("rspData").toString();
            String decryptCont = AESUtil.decryptAndBase64Decode(rspData, ConfigUtil.getConfig().getProperties(ConfigUtil.AESKEY));
            jsonObjRsp.put("rspData", decryptCont);
        }

        // 响应返回
        ResponseBean responseBean = JSONObject.parseObject(responseMessage, ResponseBean.class);
        ResponseBody body = (ResponseBody) JSONObject.parseObject(jsonObjRsp.get("rspData").toString(),
                Class.forName(beanEnum.getRspValue()));
        responseBean.setRspData(body);
        return responseBean;
    }

    @Override
    public ResponseBean invoker(RequestBody requestBody, BeanEnum beanEnum) {
        // TODO Auto-generated method stub
        return null;
    }

}
