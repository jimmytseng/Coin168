package com.vjtech.coin168.utils;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {

	static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static String getUUIDString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static String getVerifyCode() {
        String val = "";
        Random random = new Random();
        //參數length，表示生成幾位隨機數
        for(int i = 0; i < 6; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //輸出字母還是數字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //輸出是大寫字母還是小寫字母
                val += (char)(random.nextInt(26) + 65);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
        
    }
    
}
