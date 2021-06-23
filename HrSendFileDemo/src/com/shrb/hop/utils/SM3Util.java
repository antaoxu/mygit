package com.shrb.hop.utils;

import org.bouncycastle.crypto.digests.SM3Digest;

public class SM3Util {
	/**
	 * SM3加密并转换成16进制
	 * @param bytes
	 * @return
	 */
	public static String getSm3HexStr(byte[] bytes) {
		SM3Digest digest = new SM3Digest();
		digest.update(bytes,0,bytes.length);
		byte[] hash = new byte[digest.getDigestSize()];
		digest.doFinal(hash, 0);
		return parseByte2HexStr(hash);
	}
	
	/**
	 * 将二进制转换成16进制
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if(hex.length()==1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
}
