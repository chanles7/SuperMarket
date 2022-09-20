package com.mail.common.util;


public class CommonUtils {


    /**
     * 生成指定位数的随机数
     * @param placeNum 位数
     * @return
     */
    public static String generateRandomNumber(Integer placeNum) {
        int random = (int) (Math.random() * Math.pow(10, placeNum));
        return String.format("%0" + placeNum + "d", random);
    }


}
