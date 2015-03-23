package com.alexan.findevents.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.HotEventListAdapter;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.util.DBHelper;

public class RealtimeEventActivity extends SherlockActivity {

	private Spinner vTime;
	private Spinner vCategory;
	private Spinner vArea;
	private ListView vList;
	private ActionBar vACBar;
	private SwipeRefreshLayout vRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rtevent);
		initView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.menu_rtevent, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		case R.id.action_rtevent_search: {
			Intent i = new Intent(this, EventSearchActivity.class);
			startActivity(i);
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private List<DBEvent> currEventList = new ArrayList<DBEvent>();

	
	
	
	
	private void initView() {
		vACBar = getSupportActionBar();
		vACBar.setTitle("ʵʱ����");
		vACBar.setDisplayHomeAsUpEnabled(true);
		SpinnerAdapter sa = ArrayAdapter.createFromResource(this, R.array.hotspot, android.R.layout.simple_spinner_dropdown_item);
		vACBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		vACBar.setListNavigationCallbacks(sa, new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int arg0, long arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		vTime = (Spinner) findViewById(R.id.act_rtevent_sp1);
		ArrayAdapter<CharSequence> ac2 = ArrayAdapter.createFromResource(this, R.array.time, 
				R.layout.list_simple_item);
		vTime.setAdapter(ac2);
		
		vCategory = (Spinner) findViewById(R.id.act_rtevent_sp2);
		ArrayAdapter<CharSequence> ac3 = ArrayAdapter.createFromResource(this, R.array.category, 
				R.layout.list_simple_item);
		vCategory.setAdapter(ac3);
		
		vArea = (Spinner) findViewById(R.id.act_rtevent_sp3);
		ArrayAdapter<CharSequence> ac4 = ArrayAdapter.createFromResource(this, R.array.district, 
				R.layout.list_simple_item);
		vArea.setAdapter(ac4);
		
		vRefresh = (SwipeRefreshLayout) findViewById(R.id.act_rtevent_refresh);
		vRefresh.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						vRefresh.setRefreshing(false);
					}
				}, 3000);

			}
		});
		
		vList = (ListView) findViewById(R.id.act_rtevent_list);
		currEventList = DBHelper.getInstance(this).getEventDao().loadAll();
		HotEventListAdapter hea = new HotEventListAdapter(this, currEventList);
		vList.setAdapter(hea);
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", currEventList.get(position).getId());
				Intent i = new Intent(RealtimeEventActivity.this, EventDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
	}
}
