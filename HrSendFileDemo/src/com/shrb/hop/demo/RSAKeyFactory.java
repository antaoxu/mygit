package com.shrb.hop.demo;

import java.util.Map;

import com.shrb.hop.utils.RSAUtils;

public class RSAKeyFactory {
    public static void main(String[] args) throws Exception {
        Map<String, Object> map = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(map);
        String privateKey = RSAUtils.getPrivateKey(map);
        System.out.println("publicKey:\n" + publicKey);
        System.out.println("privateKey:\n" + privateKey);
    }
}
