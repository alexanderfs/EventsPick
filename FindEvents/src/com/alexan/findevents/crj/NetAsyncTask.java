package com.alexan.findevents.crj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.friend.FCEntity;
import com.alexan.findevents.friend.FriendCircleAdapter;
import com.alexan.findevents.util.DBHelper;

public class NetAsyncTask extends AsyncTask<String, Integer, List<FCEntity>>{

	private Context context;
	private List<DBPickEvent> lsevent;
	
	public NetAsyncTask(Context context, List<DBPickEvent> lsevent) {
		super();
		this.context = context;
		this.lsevent = lsevent;
	}

	@Override
	protected void onPostExecute(List<FCEntity> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		FriendCircleAdapter fca = new FriendCircleAdapter((Activity) context, result);
	
	}

	@Override
	protected List<FCEntity> doInBackground(String... params) {
		// TODO Auto-generated method stub
		System.out.println("doInBack");
		HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求   
        HttpGet httpget = new HttpGet(params[0]);
        List<FCEntity> fclist = new ArrayList<FCEntity>();
        
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
				for(DBPickEvent event : lsevent){
					DBComment comment = new DBComment();
					FCEntity fce = new FCEntity(event, comment);
					fclist.add(fce);
				
				}
			}
			//存放到本地数据库。
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return fclist;
	}

	/*public int userlogin(String emailAddr, String password){
		System.out.println("用户名密码为"+userName+passWord);
		HttpClient hc=new DefaultHttpClient();
		code=5;
		HttpGet hg=new HttpGet("http://10.103.243.101:8080/Crj/kao.txt");
		try {
			HttpResponse hr=hc.execute(hg);
			int code=hr.getStatusLine().getStatusCode();
			if(code==200){
				HttpEntity entity=hr.getEntity();
				InputStream is=entity.getContent();
				BufferedReader reader=new BufferedReader(new InputStreamReader(is));
				
				String line = reader.readLine();
				System.out.println(line);
				
			}
		} catch (ClientProtocolException e) {
			System.out.println("Normal5``````````");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Normal6``````````");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}*/
}
