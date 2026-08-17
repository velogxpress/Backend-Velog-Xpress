package com.velogexpress.tools;

import java.security.SecureRandom;

public class CreatePIN {
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "@#$%&*!?";

    private static final SecureRandom random = new SecureRandom();
    public static String GENERATEPIN(){
        String pin="";
        try {
            for(int i=0;i<=5;i++){
                pin=pin+(int)(Math.random()*9 + 1)+"";
            }
        } catch (Exception e) {
        }
        return pin;
    }

    public static String generatePassword() {

        int length = 10;

        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            int index = random.nextInt(CHARACTERS.length());

            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }


}
