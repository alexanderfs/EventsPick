package com.alexan.findevents.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBGroup;
import com.alexan.findevents.dao.DBGroupDao;
import com.alexan.findevents.friend.SortAdapter;
import com.alexan.findevents.util.CharacterParser;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.PinyinComparator;
import com.alexan.findevents.util.SortModel;
import com.alexan.findevents.view.SideBar;
import com.alexan.findevents.view.SideBar.OnTouchingLetterChangedListener;

public class PickGroupActivity extends SherlockActivity {

	private ListView vList;
	private SideBar vSidebar;
	private TextView vIndicator;
	
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SortAdapter sa;
	private List<SortModel> dataList;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		long userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			    .getLong("curr_user_id", -1);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spotlist);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		List<DBGroup> dg = DBHelper.getInstance(this).getGroupDao().queryBuilder().
				where(DBGroupDao.Properties.UserID.eq(userID)).list();
		
		vList = (ListView) findViewById(R.id.act_spotlist_list);
		
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		vSidebar = (SideBar) findViewById(R.id.act_spotlist_sidebar);
		vIndicator = (TextView) findViewById(R.id.act_spotlist_indicator);
		vSidebar.setTextView(vIndicator);
		vSidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = sa.getPositionForSection(s.charAt(0));
				if (position != -1) {
					vList.setSelection(position);
				}

			}
		});
		
		dataList = filledData(dg);
		Collections.sort(dataList, pinyinComparator);
		sa = new SortAdapter(this, dataList);
		vList.setAdapter(sa);
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.putExtra("addr", dataList.get(position).getId());
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
	
	private List<SortModel> filledData(List<DBGroup> date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setId(date.get(i).getId());
			sortModel.setName(date.get(i).getGroupname());
			String pinyin = characterParser.getSelling(date.get(i).getGroupname());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}

}
