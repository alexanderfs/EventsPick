package com.alexan.findevents;

import java.util.HashMap;
import java.util.Map;

public class AppConstant {
	public static final String DATABASE_NAME = "findevents_db";
	public static String localStorage = "";
	public static String folderName = "/findevents";
	public static Map<String,String> COUNTRY_KV = new HashMap<String,String>(){
		{
			put("中国","1008");
		}
	};
	
	public static Map<String,String> PROVINCEID_KV = new HashMap<String,String>(){
		{
			put("1003","北京");
			put("1004","天津");
			put("1001","上海");
		}
	};
	
	public static Map<String,String> PROVINCE_KV = new HashMap<String,String>(){
		{
			put("北京","1003");
			put("天津","1004");
			put("上海","1001");
		}
	};
	
	public static Map<String,String> CITY_KV = new HashMap<String,String>(){
		{
			put("海淀","1002");
			put("朝阳","1003");
			put("东城","1004");
			put("闵行","1001");
			put("徐州","1000");
			put("test","1005");		
		}
	};
	
	public static Map<String,String> CITYID_KV = new HashMap<String,String>(){
		{
			put("1002","海淀");
			put("1003","朝阳");
			put("1004","东城");
			put("1001","闵行");
			put("1000","徐州");	
		}
	};
	
	
}
