package com.shrb.hop.service;

import com.shrb.hop.beans.RequestBody;
import com.shrb.hop.beans.ResponseBean;
import com.shrb.hop.utils.BeanEnum;

/**
 * API调用，请求华瑞服务端接口
 * 
 * @author duxiaoyang
 * 
 */
public interface Api {

	/**
	 * API调用
	 * 
	 * @param requestBody
	 *            请求javabean对象
	 * @param responseClass
	 *            响应javabean类型
	 * @return
	 */
	public ResponseBean invoker(RequestBody requestBody, BeanEnum beanEnum);
	
	public String getRequestString(RequestBody requestBody, BeanEnum beanEnum) throws Exception;
	
	public ResponseBean getResponseBean(String rspMsg, BeanEnum beanEnum) throws Exception;

}
