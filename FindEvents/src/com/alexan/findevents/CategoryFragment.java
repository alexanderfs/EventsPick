package com.alexan.findevents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexan.findevents.dao.DBCategory;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.dao.DBPickEventDao;
import com.alexan.findevents.event.EventDetailActivity;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class CategoryFragment extends Fragment {

	private Spinner vSort;
	private int spSort;
	private TextView vCategory0;
	private TextView vCategory1;
	private TextView vCategory2;
	private TextView vCategory3;
	private TextView vCategory4;
	private TextView vCategory5;
	private TextView vCategory6;
	private TextView vCategory7;
	private TextView vCategory8;
	private TextView vCategory9;
	private TextView vCategory10;
	
	private TextView vTime1;
	private TextView vTime2;
	private TextView vTime3;
	
	private ListView vList;
	private String currCity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currCity = getArguments().getString("curr_city");
		View v = inflater.inflate(R.layout.fragment_category, container, false);
		initView(v);
		((FrameworkActivity)getActivity()).setPFlag(3);
		((FrameworkActivity)getActivity()).supportInvalidateOptionsMenu();
		getActivity().setTitle("");
		categoryList = getActivity().getResources().getStringArray(R.array.category);
		return v;
	}

	private String[] hotSpotsList;
	private TextView currCategory;
	private TextView currTime;
	private int category;
	private int time;

	void initView(View v) {
		vCategory0 = (TextView) v.findViewById(R.id.act_ca_ca0);
		vCategory0.setTag("0");
		vCategory0.setTextColor(Color.BLUE);
		String st = "<b>"+vCategory0.getText().toString()+"</b>";
		vCategory0.setText(Html.fromHtml(st));
		currCategory = vCategory0;
		
		vCategory0.setOnClickListener(clicklistener1);
		vCategory1 = (TextView) v.findViewById(R.id.act_ca_ca1);
		vCategory1.setTag("1");
		vCategory1.setOnClickListener(clicklistener1);
		vCategory2 = (TextView) v.findViewById(R.id.act_ca_ca2);
		vCategory2.setTag("2");
		vCategory2.setOnClickListener(clicklistener1);
		vCategory3 = (TextView) v.findViewById(R.id.act_ca_ca3);
		vCategory3.setTag("3");
		vCategory3.setOnClickListener(clicklistener1);
		vCategory4 = (TextView) v.findViewById(R.id.act_ca_ca4);
		vCategory4.setTag("4");
		vCategory4.setOnClickListener(clicklistener1);
		vCategory5 = (TextView) v.findViewById(R.id.act_ca_ca5);
		vCategory5.setTag("5");
		vCategory5.setOnClickListener(clicklistener1);
		vCategory6 = (TextView) v.findViewById(R.id.act_ca_ca6);
		vCategory6.setTag("6");
		vCategory6.setOnClickListener(clicklistener1);
		vCategory7 = (TextView) v.findViewById(R.id.act_ca_ca7);
		vCategory7.setTag("7");
		vCategory7.setOnClickListener(clicklistener1);
		vCategory8 = (TextView) v.findViewById(R.id.act_ca_ca8);
		vCategory8.setTag("8");
		vCategory8.setOnClickListener(clicklistener1);
		vCategory9 = (TextView) v.findViewById(R.id.act_ca_ca9);
		vCategory9.setTag("9");
		vCategory9.setOnClickListener(clicklistener1);
		vCategory10 = (TextView) v.findViewById(R.id.act_ca_ca10);
		vCategory10.setTag("10");
		vCategory10.setOnClickListener(clicklistener1);
		
		vTime1 = (TextView) v.findViewById(R.id.act_ca_ca11);
		vTime1.setTag("0");
		vTime1.setTextColor(Color.BLUE);
		st = "<b>"+vTime1.getText().toString()+"</b>";
		vTime1.setText(Html.fromHtml(st));
		currTime = vTime1;
		
		vTime1.setOnClickListener(clicklistener2);
		vTime2 = (TextView) v.findViewById(R.id.act_ca_ca12);
		vTime2.setTag("1");
		vTime2.setOnClickListener(clicklistener2);
		vTime3 = (TextView) v.findViewById(R.id.act_ca_ca13);
		vTime3.setTag("2");
		vTime3.setOnClickListener(clicklistener2);

		vSort = (Spinner) v.findViewById(R.id.act_ca_sort);
		ArrayAdapter<CharSequence> aa2 = ArrayAdapter.createFromResource(
				getActivity(), R.array.sort_option, R.layout.list_simple_item);
		vSort.setAdapter(aa2);
		vSort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				spSort = position;
				reloadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});

		vList = (ListView) v.findViewById(R.id.act_ca_list);
		reloadData();
	}
	
	private List<DBPickEvent> currEventList = new ArrayList<DBPickEvent>();
	private Date today, tomorrow;
	private String[] categoryList;
	
	private OnClickListener clicklistener1 = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(currCategory == v) {
				return;
			}
			currCategory.setTextColor(Color.BLACK);
			currCategory.setText(currCategory.getText().toString());
			currCategory = (TextView)v;
			String st = "<b>"+currCategory.getText().toString()+"</b>";
			currCategory.setText(Html.fromHtml(st));
			currCategory.setTextColor(Color.BLUE);
			category = Integer.parseInt(v.getTag().toString());
			reloadData();
		}		
	};
	
	private OnClickListener clicklistener2 = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(currCategory == v) {
				return;
			}
			currTime.setTextColor(Color.BLACK);
			currTime.setText(currTime.getText().toString());
			currTime = (TextView)v;
			String st = "<b>"+currTime.getText().toString()+"</b>";
			currTime.setText(Html.fromHtml(st));
			currTime.setTextColor(Color.BLUE);
			time = Integer.parseInt(v.getTag().toString());
			reloadData();
		}		
	};
	
	private void reloadData() {
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
		switch(time) {
		case 1: {
			qbEvent.where(DBEventDao.Properties.Starttime.ge(today.getTime()), 
					DBEventDao.Properties.Starttime.le(tomorrow.getTime()));
			break;
		}
		case 2: {
			qbEvent.where(DBEventDao.Properties.Starttime.ge(tomorrow.getTime()));
			break;
		}
		}
		
		List<DBPickEvent> tmpList;
		if(spSort == 0) {
			tmpList = qbEvent./*orderDesc(DBEventDao.Properties.Timestamp).*/list();
		} else {
			tmpList = qbEvent./*orderDesc(DBEventDao.Properties.CollectionNum).*/list();
		}
		if(category != 0) {
			currEventList.clear();
			for(DBPickEvent e: tmpList) {
				boolean isFound = false;
			
				if(e.getCatagory() == null){
					continue;
				}
				if(e.getCatagory().equals(categoryList[category])){
					isFound = true;
				}
				if(isFound) {
					currEventList.add(e);
				}
			}
		} else {
			currEventList = tmpList;
		}
		
		PickListAdapter hea = new PickListAdapter(getActivity(),
				currEventList);
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
