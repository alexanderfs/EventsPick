package com.alexan.findevents.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static String getDateSpanString(Long startTime, Long endTime) {
		if(startTime == null || endTime == null) {
			return "时间: DEFAULT TIME";
		}
		Date sd = new Date(startTime);
		Calendar sdc = Calendar.getInstance();
		sdc.setTime(sd);
		Date ed = new Date(endTime);
		Calendar edc = Calendar.getInstance();
		edc.setTime(ed);
		
		StringBuilder sb = new StringBuilder("【时间】  ");
		sb.append(sdc.get(Calendar.YEAR)).append('.')
		.append(sdc.get(Calendar.MONTH)+1).append('.')
		.append(sdc.get(Calendar.DAY_OF_MONTH)).append('-');
	sb.append(edc.get(Calendar.YEAR)).append('.')
		.append(edc.get(Calendar.MONTH)+1).append('.')
		.append(edc.get(Calendar.DAY_OF_MONTH)).append(", ");
		sb.append(sdc.get(Calendar.HOUR_OF_DAY)).append(':').append(sdc.get(Calendar.MINUTE)).append('-');
		sb.append(edc.get(Calendar.HOUR_OF_DAY)).append(':').append(edc.get(Calendar.MINUTE));
		
		
		return sb.toString();
	}
}
