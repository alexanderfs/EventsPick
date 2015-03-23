package com.alexan.findevents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.dao.DBPickEventDao;
import com.alexan.findevents.event.EventDetailActivity;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.crj.*;

public class HotEventFragment extends Fragment {
	private ListView vList;
	//private View vHead;
	private TextView vTitle;
	private TextView vOtherdetail;
	private TextView vAddress;
	private TextView vTime;
	private SwipeRefreshLayout vRefresh;
	private LinearLayout vLL;
	private ViewPager vImage;
    private ImageView imagev;
    private List<ImageView> imageList = new ArrayList<ImageView>();
    private List<DBPickEvent> pickList;
	//private DBEvent headEvent = new DBEvent();
	private View eventView;
	private String city;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pickList = DBHelper.getInstance(getActivity()).getPickEventDao().queryBuilder().where(DBPickEventDao.Properties.City.eq(city)).list();
			System.out.print(pickList.toString());
			PickListAdapter hea = new PickListAdapter(getActivity(), pickList);
			vList.setAdapter(hea);
			super.handleMessage(msg);
		}
		
	};

	//code由Crj添加
		int code = 0;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		eventView = inflater.inflate(R.layout.fragment_hotevents, container, false);
		initData();
		initView(eventView);
		((FrameworkActivity)getActivity()).setPFlag(1);
		((FrameworkActivity)getActivity()).supportInvalidateOptionsMenu();
		getActivity().setTitle("");
		return eventView;
	}
	
	private void initData() {
		pickList = DBHelper.getInstance(getActivity()).getPickEventDao().queryBuilder().list();
//		if(eventList.size() > 0) {
//			headEvent = eventList.get(0);
//			eventList.remove(0);
//		}
	}
	
	public void reloadData(String city) {
		this.city = city;
		pickList = DBHelper.getInstance(getActivity()).getPickEventDao().queryBuilder().where(DBPickEventDao.Properties.City.eq(city)).list();
		
		PickListAdapter hea = new PickListAdapter(getActivity(), pickList);
		vList.setAdapter(hea); 
	}
	

	
	private void initList(View mainView) {
		//initHead();
		vList = (ListView) mainView.findViewById(R.id.fgt_event_list);
		//vList.addHeaderView(vHead);
		if(pickList == null) {
			pickList = new ArrayList<DBPickEvent>();
		}
		PickListAdapter hea = new PickListAdapter(getActivity(), pickList);
		vList.setAdapter(hea); 
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Bundle b = new Bundle();
				b.putLong("event_id", pickList.get(position).getId() == null ? -1 : pickList.get(position).getId());
				Intent detailIntent = new Intent(getActivity(), EventDetailActivity.class);
				detailIntent.putExtras(b);
				startActivityForResult(detailIntent, 1);
			}
			
		});
	}
	
	private void initView(View mainView) {
		vRefresh = (SwipeRefreshLayout) mainView.findViewById(R.id.fgt_event_swipe_container);
		vRefresh.setColorScheme(R.color.white,
                R.color.holo_green_light,  
                R.color.holo_orange_light, 
                R.color.holo_red_light);  
		vRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				vRefresh.setRefreshing(true);
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String url = "http://123.57.45.183/event/GetEvents";
						//从服务器获得数据。
						JSONparse.refreshFrom( url,  getActivity(),  handler);
				        vRefresh.setRefreshing(false);
					}			
				}.start();
			}
		});
		
		initList(mainView);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			PickListAdapter hea = new PickListAdapter(getActivity(), pickList);
			vList.setAdapter(hea); 
		}
	}
	
}
