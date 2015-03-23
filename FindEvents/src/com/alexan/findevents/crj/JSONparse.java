package com.alexan.findevents.crj;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.util.DBHelper;

public class JSONparse {
	
	public static void parseEvents(JSONArray data, Context context){
		for(int i = 0;i<data.length();i++){
			try {
				JSONObject jo = data.getJSONObject(i);
				DBPickEvent event = new DBPickEvent();
				event.setId(Long.parseLong(jo.getString("EventId")));
				event.setDescription(jo.getString("EventDescription"));
				event.setTitle(jo.getString("EventTitle"));
				event.setUserID(Long.parseLong(jo.getString("UserId")));
				event.setCatagory(jo.getString("EventCategoryName")==null?"DEFAULT":jo.getString("EventCategoryName"));
				
				event.setProvince(jo.getString("ProvinceName")==null?"DEFAULT":jo.getString("ProvinceName"));
				event.setAddress("地址");
				event.setAddressdetail("测试地址");
				//测试阶段，暂且用provincename代替cityname。
				event.setCity(jo.getString("ProvinceName")==null?"DEFAULT":jo.getString("ProvinceName"));
				event.setDistrict("丰台");
				
				DBHelper.getInstance(context).getPickEventDao().insert(event);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void refreshFrom(String url, Context context, Handler handler){
		HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求   
        HttpGet httpget = new HttpGet(url);
        
        try {
			HttpResponse httpresponse = client.execute(httpget);
			int code=httpresponse.getStatusLine().getStatusCode();
			System.out.print(code);
			//添加对code的判断
			HttpEntity entity = httpresponse.getEntity();
			if(entity!=null){
				BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine())!=null){
					sb.append(line);
				}
				br.close();
				System.out.print(sb.toString());
				//删了之前的event
				DBHelper.getInstance(context).getPickEventDao().deleteAll();
				//解析json
				
				JSONObject result = new JSONObject(sb.toString());
				JSONArray data = result.getJSONArray("data");
				JSONparse.parseEvents(data, context);
				
				handler.sendEmptyMessage(1);
				
			}
			//存放到本地数据库。
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
