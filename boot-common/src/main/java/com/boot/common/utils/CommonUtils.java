package com.boot.common.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import java.util.Random;
import java.util.UUID;

public class CommonUtils {
    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(3).toHex();
    }

    /**
     * 用户密码加密
     */
    public static String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex().toString();
    }

    /**
     * 生产uuid
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 生产指定位数的随机数
     */
    public static String randomNumber(int length) {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
    /**
     * 加密，把一个字符串在原有的基础上+1
     * @param data 需要解密的原字符串
     * @return 返回解密后的新字符串
     */
    public static String encode(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for(int i=0;i<b.length;i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }
    /**
     * 解密：把一个加密后的字符串在原有基础上-1
     * @param data 加密后的字符串
     * @return 返回解密后的新字符串
     */
    public static String decode(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for(int i=0;i<b.length;i++) {
            b[i] -= 1;//在原有的基础上-1
        }
        return new String(b);
    }



    public static void main(String[] arge){
        //加密英文
        String data = "/app/visitor/d0b2846f6ce011eaa0e7525400f4a27b/663305106dcc11eaa0e7525400f4a26b";
        String result = encode(data);
        System.out.println("加密后:"+result);
        //解密
        String str = decode(result);
        System.out.println("解密后:"+str);
    }
}