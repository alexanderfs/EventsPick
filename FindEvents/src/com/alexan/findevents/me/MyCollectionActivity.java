package com.alexan.findevents.me;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.crj.JSONparse;
import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBCommentDao;
import com.alexan.findevents.dao.DBCommentDao.Properties;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.event.EventDetailActivity;
import com.alexan.findevents.event.EventSearchActivity;
import com.alexan.findevents.event.PublishEventActivity;
import com.alexan.findevents.friend.FCEntity;
import com.alexan.findevents.friend.FriendCircleAdapter;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class MyCollectionActivity extends SherlockActivity {
	private ListView vMainList;
	private List<DBPickEvent> eventlist;
	private MyTask myTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("我的收藏");
		setContentView(R.layout.activity_mycollection);
		initView();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			return true;
		}
		case R.id.action_framework_add: {
			Intent i = new Intent(this, PublishEventActivity.class);
			startActivity(i);
			return true;
		}
		case R.id.action_framework_search: {
			Intent i = new Intent(this, EventSearchActivity.class);
			startActivity(i);
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}



	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vMainList = (ListView) findViewById(R.id.act_mycollection_mainlist);
		String url = "http://123.57.45.183/event/GetFavorites?UserId="+getUserID();
		myTask = new MyTask();
		myTask.execute(url);
		
		vMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", eventList.get(position).getEvent().getId());
				Intent i = new Intent(MyCollectionActivity.this, EventDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
	}

	private List<FCEntity> eventList;
	
	private long getUserID() {
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);
	}   
	
	private class MyTask extends AsyncTask<String, Integer, List<FCEntity>>{

		@Override
		protected void onPostExecute(List<FCEntity> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			FriendCircleAdapter fca = new FriendCircleAdapter(MyCollectionActivity.this, result);
			vMainList.setAdapter(fca);
		}

		@Override
		protected List<FCEntity> doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println("doInBack");
			HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求   
	        HttpGet httpget = new HttpGet(params[0]);
	        List<FCEntity> fclist = new ArrayList<FCEntity>();
	        eventlist = new ArrayList<DBPickEvent>();
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
							DBPickEvent event = DBHelper.getInstance(MyCollectionActivity.this).getPickEventDao().load(eventId);
							if(event!=null){
								eventlist.add(event);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					for(DBPickEvent event : eventlist){
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
	}
}
