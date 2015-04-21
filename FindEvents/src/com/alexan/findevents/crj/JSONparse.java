package com.alexan.findevents.crj;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alexan.findevents.AppConstant;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.dao.DBPickEventDao;
import com.alexan.findevents.util.DBHelper;

public class JSONparse {
	
	static String updateImageUrl = "http://123.57.45.183/event/UpdateEventPictures";
	
	public static void parseEvents(JSONArray data, Context context){
		String purl = null;
		for(int i = 0;i<data.length();i++){
			try {
				JSONObject jo = data.getJSONObject(i);
				DBPickEvent event = new DBPickEvent();
				event.setId(Long.parseLong(jo.getString("EventId")));
				event.setDescription(jo.getString("EventDescription"));
				event.setTitle(jo.getString("EventTitle"));
				event.setUserID(Long.parseLong(jo.getString("UserId")));
				event.setCatagory(jo.getString("EventCategoryName")==null?"DEFAULT":jo.getString("EventCategoryName"));
				
				event.setStartd(jo.getString("EventStartDate")==null?"DEFAULT":jo.getString("EventStartDate"));
				event.setStartt(jo.getString("EventStartTime")==null?"DEFAULT":jo.getString("EventStartTime"));
				event.setEndd(jo.getString("EventEndDate")==null?"DEFAULT":jo.getString("EventEndDate"));
				event.setEndt(jo.getString("EventEndTime")==null?"DEFAULT":jo.getString("EventEndTime"));
				
				event.setCommentNum(jo.getString("NumOfComments")==null?0:Integer.parseInt(jo.getString("NumOfComments")));
				event.setCommentNum(jo.getString("NumOfFavorites")==null?0:Integer.parseInt(jo.getString("NumOfFavorites")));
				
				event.setProvince(jo.getString("ProvinceName")==null?"DEFAULT":jo.getString("ProvinceName"));
				event.setAddress(jo.getString("VenueName")==null?"DEFAULT":jo.getString("VenueName"));
				event.setAddressdetail(jo.getString("VenueAddress")==null?"DEFAULT":jo.getString("VenueAddress"));
				//测试阶段，暂且用provincename代替cityname。
				event.setCity(jo.getString("ProvinceName")==null?"DEFAULT":jo.getString("ProvinceName"));
				if(jo.getString("HasPictures").equals("1")){
						if(!jo.get("pics").equals(null)){
							purl = jo.getJSONArray("pics").getJSONObject(0).getString("PictureUrl");
						}
						else{
							purl = null;
						}
				}
				event.setPhoto(purl);
				
				
				DBHelper.getInstance(context).getPickEventDao().insert(event);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void postto(String url, Map<String, String>params, Context context, Handler handler){
		HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求   
        HttpPost post = new HttpPost(url);//创建 HTTP POST 请求    
        
       /* try {
        	
        }*/
	}
	
	public static void postComment(final Handler mHandler, final String params, final String url){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = Util.httpPost(url, params);
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("params", result);
				message.setData(bundle);
				mHandler.sendMessage(message);
			}
		}).start();
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
				//解析json
				JSONObject result = new JSONObject(sb.toString());
				JSONArray data = result.getJSONArray("data");
				//根据url的不同，调用不同的解析方法：
				if(url.equals("http://123.57.45.183/event/GetEvents")){
					//删了之前的event
					DBHelper.getInstance(context).getPickEventDao().deleteAll();
					JSONparse.parseEvents(data, context);
					handler.sendEmptyMessage(1);
				}
				else if(url.equals("http://123.57.45.183/venue/SearchVenues")){
					//删了之前的venue
					DBHelper.getInstance(context).getLocationDao().deleteAll();
					JSONparse.parseVenues(data, context);
					handler.sendEmptyMessage(1);
				}
			}
			//存放到本地数据库。
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseFavorite(String url, Context context, List<DBPickEvent> lsevent){
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
				//解析json
				JSONObject result = new JSONObject(sb.toString());
				JSONArray data = result.getJSONArray("data");
				
				for(int i = 0;i<data.length();i++){
					try {
						long eventId = Long.parseLong(data.getString(i));
						DBPickEvent event = DBHelper.getInstance(context).getPickEventDao().load(eventId);
						if(event!=null){
							lsevent.add(event);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}					
			}
			//存放到本地数据库。
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void parseVenues(JSONArray data, Context context) {
		// TODO Auto-generated method stub
		
		for(int i = 0;i<data.length();i++){
			try {
				JSONObject jo = data.getJSONObject(i);
				DBLocation ndbl = new DBLocation();
				ndbl.setId(Long.parseLong(jo.getString("VenueId")));
				ndbl.setAddrName(jo.getString("VenueName")==null?"DEFAULT":jo.getString("VenueName"));
				ndbl.setAddrDetail(jo.getString("VenueAddress")==null?"DEFAULT":jo.getString("VenueAddress"));
				ndbl.setAddrProvince(getProvinceName(jo.getString("VenueProvinceId")));
				ndbl.setAddrCity(getCityName(jo.getString("VenueCityId")));
			
				DBHelper.getInstance(context).getLocationDao().insert(ndbl);	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		/*try {
			JSONObject obresult = new JSONObject(result);
			String code = obresult.getString("code");
			if(!code.equals("10000")){
				Toast.makeText(context, "保存失败，请填写完整的地址", Toast.LENGTH_SHORT).show();
			}
			else{*/
	
	}
	
	protected static String getCityName(String cityid) {
		// TODO Auto-generated method stub
		return AppConstant.CITYID_KV.get(cityid);
	}

	protected static String getProvinceName(String proid) {
		// TODO Auto-generated method stub
		return AppConstant.PROVINCEID_KV.get(proid);
	
	}

	
	public static boolean uploadimage(long eventId,File file) throws ClientProtocolException, IOException {
	 	final HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求   
        final HttpPost post = new HttpPost(updateImageUrl);//创建 HTTP POST 请求    
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
	      builder.setCharset(Charset.forName("UTF-8"));//设置请求的编码格式  
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式  
        
        builder.addBinaryBody("uploadfile", file);  
        
        builder.addTextBody("EventId", ""+eventId);

        //builder.addPart("EventId", new LongBody(eventId), Charset.forName("UTF-8")));//设置请求参数  
	    HttpEntity entity = builder.build();// 生成 HTTP POST 实体        
        post.setEntity(entity);//设置请求参数  
        
        HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应  
		if (response.getStatusLine().getStatusCode()==200) {  
            return true;  
        }
		return false;
	}

}
