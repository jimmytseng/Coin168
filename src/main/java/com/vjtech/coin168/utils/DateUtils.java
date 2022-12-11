package com.vjtech.coin168.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static final SimpleDateFormat YYYYMMdd_SPLASH = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String formatDate(Date date,SimpleDateFormat sdf) {
		return sdf.format(date);
	}
	

}
