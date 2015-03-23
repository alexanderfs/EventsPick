package com.alexan.findevents.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.HotEventListAdapter;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventCategory;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class SearchResultActivity extends SherlockActivity {

	private ActionBar vACBar;
	private Spinner vTime;
	private Spinner vCategory;
	private ListView vList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData(getIntent().getExtras());
		setContentView(R.layout.activity_searchresult);
		initView();
	}
	
	private void initData(Bundle b) {
		title = b.getString("title");
		if(title == null) {
			title = "";
		}
		addr = b.getString("addr");
		if(addr == null) {
			addr = "";
		}
		time = b.getInt("time", 0);
		categoryArray = b.getStringArray("category");
		if(categoryArray == null) {
			categoryArray = new String[]{};
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		vACBar = getSupportActionBar();
		vACBar.setTitle("搜索结果");
		vACBar.setDisplayHomeAsUpEnabled(true);
		vTime = (Spinner) findViewById(R.id.act_sresult_sp1);
		ArrayAdapter<CharSequence> ac2 = ArrayAdapter.createFromResource(this, R.array.sort_option, 
				R.layout.list_simple_item);
		vTime.setAdapter(ac2);
		vTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sortOption = position;
				loadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vList = (ListView) findViewById(R.id.act_sresult_list);
		//loadData();
		
	}
	
	private List<DBEvent> currEventList = new ArrayList<DBEvent>();
	private int sortOption;
	private String title;
	private String addr;
	private int time;
	private String[] categoryArray;
	private Date today, tomorrow;
	
	private void loadData() {
		Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int dayofmonth = c.get(Calendar.DAY_OF_MONTH);
		today = new GregorianCalendar(year,month,dayofmonth).getTime();
		tomorrow = new GregorianCalendar(year,month,dayofmonth+1).getTime();
		/*c.setTime(today);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		tomorrow = c.getTime();*/
		
		QueryBuilder<DBEvent> qbEvent = DBHelper.getInstance(this).getEventDao()
				.queryBuilder();
		if(sortOption == 0) {
			qbEvent.orderDesc(DBEventDao.Properties.Starttime);
		} else {
			qbEvent.orderDesc(DBEventDao.Properties.CollectionNum);
		}
		if(title.length() > 0) {
			qbEvent.where(DBEventDao.Properties.Title.like(title));
		}
		if(addr.length() > 0) {
			qbEvent.where(DBEventDao.Properties.Addressdetail.like(addr));
		}
		if(time == 1) {
			qbEvent.where(DBEventDao.Properties.Starttime.ge(today.getTime()), 
					DBEventDao.Properties.Starttime.le(tomorrow.getTime()));
		} else if(time == 2) {
			qbEvent.where(DBEventDao.Properties.Starttime.ge(tomorrow.getTime()));
		}
		if(categoryArray.length > 0) {
			currEventList.clear();
			List<DBEvent> tmpList = qbEvent.list();
			for(DBEvent e: tmpList) {
				boolean isFound = false;
				for(DBEventCategory ec: e.getCategories()) {
					for(int i = 0; i < categoryArray.length; i++) {
						if(ec.getDBCategory().getName().equals(categoryArray[i])) {
							isFound = true;
							break;
						}
					}
					if(isFound) {
						currEventList.add(e);
						break;
					}
				}
			}
		} else {
			currEventList = qbEvent.list();
		}
		HotEventListAdapter hea = new HotEventListAdapter(this, currEventList);
		vList.setAdapter(hea);
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", currEventList.get(position).getId());
				Intent i = new Intent(SearchResultActivity.this, EventDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
	}
}
