package com.alexan.findevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexan.findevents.crj.JSONparse;
import com.alexan.findevents.dao.DBCategory;
import com.alexan.findevents.dao.DBCategoryDao;
import com.alexan.findevents.dao.DBCategoryEvent;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventCategory;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.dao.DBPickEventDao;
import com.alexan.findevents.R;
import com.alexan.findevents.event.EventDetailActivity;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class RealtimeFragment extends Fragment {
	
	private Spinner vTime;
	private Spinner vCategory;
	private Spinner vArea;
	private ListView vList;
	private SwipeRefreshLayout vRefresh;
	private int sp1;
	private int sp2;
	private int sp3;
	private List<DBPickEvent> pickList;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pickList = DBHelper.getInstance(getActivity()).getPickEventDao().queryBuilder().where(DBPickEventDao.Properties.City.eq(currCity)).list();
			System.out.print(pickList.toString());
			PickListAdapter hea = new PickListAdapter(getActivity(), pickList,vList);
			vList.setAdapter(hea);
			super.handleMessage(msg);
		}
		
	};
	//code由Crj添加
	int code = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currCity = getArguments().getString("curr_city");
		View eventView = inflater.inflate(R.layout.activity_rtevent, container, false);
		initView(eventView);
		getActivity().setTitle("");
		((FrameworkActivity)getActivity()).setPFlag(2);
		((FrameworkActivity)getActivity()).supportInvalidateOptionsMenu();
		categoryList = getActivity().getResources().getStringArray(R.array.category);
		return eventView;
	}
	
	private Date today, tomorrow;
	

	private List<DBPickEvent> currEventList = new ArrayList<DBPickEvent>();
	private String[] categoryList;
	
	private String[] districtArray;

	private void initView(View v) {
		vTime = (Spinner) v.findViewById(R.id.act_rtevent_sp1);
		ArrayAdapter<CharSequence> ac2 = ArrayAdapter.createFromResource(getActivity(), R.array.time, 
				R.layout.list_simple_item);
		vTime.setAdapter(ac2);
		vTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sp1 = position;
				reloadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vCategory = (Spinner) v.findViewById(R.id.act_rtevent_sp2);
		ArrayAdapter<CharSequence> ac3 = ArrayAdapter.createFromResource(getActivity(), R.array.category, 
				R.layout.list_simple_item);
	//	CategoryAdapter ca = new CategoryAdapter();
	//	vCategory.setAdapter(ca);
		vCategory.setAdapter(ac3);
		vCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sp2 = position;
				reloadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		districtArray = getActivity().getResources().getStringArray(R.array.district);
		vArea = (Spinner) v.findViewById(R.id.act_rtevent_sp3);
		ArrayAdapter<CharSequence> ac4 = ArrayAdapter.createFromResource(getActivity(), R.array.district, 
				R.layout.list_simple_item);
		vArea.setAdapter(ac4);
		vArea.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sp3 = position;
				reloadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vRefresh = (SwipeRefreshLayout) v.findViewById(R.id.act_rtevent_refresh);
		vRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
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
		
		vList = (ListView) v.findViewById(R.id.act_rtevent_list);
		reloadData();
		
	}
	
	private PickListAdapter hea;
	private String currCity;
	
	public void reloadData() {
		reloadData(currCity);
	}
	
	public void reloadData(String city) {
		currCity = city;
		today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		tomorrow = c.getTime();
		QueryBuilder<DBPickEvent> qbEvent = DBHelper.getInstance(getActivity()).getPickEventDao()
				.queryBuilder().where(DBPickEventDao.Properties.City.eq(currCity));
		switch(sp1) {
			case 1: {
				qbEvent.where(DBPickEventDao.Properties.Startt.ge(today.getTime())/*, 
						DBEventDao.Properties.Starttime.le(tomorrow.getTime())*/);
				break;
			}
			case 2: {
				qbEvent.where(DBPickEventDao.Properties.Startt.ge(tomorrow.getTime()));
				break;
			}
		}
		
		if(sp3 != 0) {
		//	qbEvent.where(DBPickEventDao.Properties.District.eq(districtArray[sp3]));
		}
		
		if(sp2 != 0) {
			currEventList.clear();
			List<DBPickEvent> tmpList = qbEvent.list();
			for(DBPickEvent e: tmpList) {
				boolean isFound = false;
				/*for(DBEventCategory ec: e.getCategories()) {
					DBCategory ca = DBHelper.getInstance(getActivity())
							.getCategoryDao().load(ec.getCategoryID());
					if(ca.getName().equals(categoryList[sp2])) {
						isFound = true;
						break;
					}
				}*/
				if(e.getCatagory() == null){
					continue;
				}
				if(e.getCatagory().equals(categoryList[sp2])){
					isFound = true;
				}
				if(isFound) {
					currEventList.add(e);
				}
			}
		} else {
			currEventList = qbEvent.list();
		}
		
		hea = new PickListAdapter(getActivity(), currEventList,vList);
		vList.setAdapter(hea);
		
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", currEventList.get(position).getId());
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
	}
	

}
