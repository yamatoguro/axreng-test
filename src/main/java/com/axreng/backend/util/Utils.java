package com.axreng.backend.util;

public class Utils {
    public static String generateID() {
        String id = "";
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int j = 0; j < 8; j++) {
            int index = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        id = sb.toString();
        return id;
    }
}
