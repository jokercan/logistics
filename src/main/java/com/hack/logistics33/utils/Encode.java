package com.hack.logistics33.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encode {

    public String MD5(String param) {
        byte[] secrets = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(param.getBytes());
            secrets = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String md5code = new BigInteger(1, secrets).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

//    public static void main(String[] args){
//        System.out.println(new Encode().MD5("123456"));
//    }
}