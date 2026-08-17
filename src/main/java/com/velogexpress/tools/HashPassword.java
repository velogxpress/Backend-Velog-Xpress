package com.velogexpress.tools;

import java.util.Base64;

public class HashPassword {
    public String ENCRYPT(String pwd) {
        String pass =null;
        try {
            pass = Base64.getEncoder().encodeToString(pwd.getBytes("utf-8"));
        } catch (Exception e) {
        }
        return pass;
    }

    public String DECRYPT(String pwd){
        byte[] decode=null;String pass=null;
        try {
            decode=Base64.getDecoder().decode(pwd.getBytes());
            pass=new String(decode,"utf-8");
        } catch (Exception e) {
        }
        return pass;
    }
}
