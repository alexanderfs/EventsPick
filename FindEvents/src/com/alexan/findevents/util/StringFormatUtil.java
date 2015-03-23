package com.alexan.findevents.util;

import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBPickEvent;


public class StringFormatUtil {
	public static String buildAddrString(DBEvent e) {
		StringBuilder sb = new StringBuilder("【地点】   ");
		sb.append(e.getAddress() == null ? "EA" : e.getAddress()).append("(");
		sb.append(e.getCity() == null ? "CITY" : e.getCity()).append(", ");
		sb.append(e.getDistrict() == null ? "DISTRICT" : e.getDistrict()).append(", ");
		sb.append(e.getAddressdetail() == null ? "EAD" : e.getAddressdetail()).append(")");
		
		return sb.toString();
	}
	
	public static String buildPickAddrString(DBPickEvent e) {
		StringBuilder sb = new StringBuilder("【地点】   ");
		sb.append(e.getAddress() == null ? "EA" : e.getAddress()).append("(");
		sb.append(e.getCity() == null ? "CITY" : e.getCity()).append(", ");
		sb.append(e.getDistrict() == null ? "DISTRICT" : e.getDistrict()).append(", ");
		sb.append(e.getAddressdetail() == null ? "EAD" : e.getAddressdetail()).append(")");
		
		return sb.toString();
	}
}
