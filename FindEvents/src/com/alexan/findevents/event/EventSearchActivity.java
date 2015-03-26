package com.alexan.findevents.event;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBCategory;
import com.alexan.findevents.dao.DBEvent;

public class EventSearchActivity extends SherlockActivity {
	
	private ActionBar vACBar;
	private EditText vSearchET;
	private Button vAdvanceBtn;
	private LinearLayout vLL;
	private EditText vAddr;
	private RadioGroup vTimeRG;
	private GridView vCategoryList;
	private Button vSubmit;
	
	private boolean isAdvanceFlag = false;
    //这里待定！！！！

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventsearch);
		
		initView();
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
	
	private String sTitle;
	private String sLocation;
	private int sTime = -1;
	
	private void initView() {
		vACBar = getSupportActionBar();
		vACBar.setTitle("搜索活动");
		vACBar.setDisplayHomeAsUpEnabled(true);
		vSearchET = (EditText) findViewById(R.id.act_eventsearch_keyword);
		vAdvanceBtn = (Button) findViewById(R.id.act_eventsearch_advance);
		vAdvanceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isAdvanceFlag) {
					vLL.setVisibility(View.GONE);
					isAdvanceFlag = false;
				} else {
					vLL.setVisibility(View.VISIBLE);
					isAdvanceFlag = true;
				}
			}
		});
		vLL = (LinearLayout) findViewById(R.id.act_eventsearch_advcontent);
		vLL.setVisibility(View.GONE);
		
		vAddr = (EditText) findViewById(R.id.act_eventsearch_spots);
		
		vTimeRG = (RadioGroup) findViewById(R.id.act_eventsearch_timerg);
		vTimeRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1) {
				case R.id.act_eventsearch_timerb1: {
					sTime = 0;
				}
					break;
				case R.id.act_eventsearch_timerb2:
					sTime = 1;
					break;
				case R.id.act_eventsearch_timerb3:
					sTime = 2;
					break;
				default:
					sTime = 0;
					break;
				}
			}
		});
		vCategoryList = (GridView) findViewById(R.id.act_eventsearch_category);
		CategoryCBAdapter ca = new CategoryCBAdapter(this, new CategoryRecorder(), null);
		vCategoryList.setAdapter(ca);
		
		vSubmit = (Button) findViewById(R.id.act_eventsearch_submit);
		vSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(checkValid() == 1) {
					Toast.makeText(EventSearchActivity.this, "请填写活动名称", Toast.LENGTH_SHORT).show();
				} else {
					Bundle b = new Bundle();
					if(vSearchET.getText().length() != 0) {
						b.putString("title", vSearchET.getText().toString());
					}
					if(vAddr.getText().length() != 0) {
						b.putString("addr", vAddr.getText().toString());
					}
					if(sTime != -1) {
						b.putInt("time", sTime);
					}
					if(categorySet.size() > 0) {
						String[] categoryArray = new String[categorySet.size()];
						int i = 0;
						for(String c: categorySet) {
							categoryArray[i++] = c;
						}
						b.putStringArray("category", categoryArray);
					}
					Intent i = new Intent(EventSearchActivity.this, SearchResultActivity.class);
					i.putExtras(b);
					startActivity(i);
				}
			}
		});
	}
	
	private int checkValid() {
		if(vSearchET.getText().length() == 0 && 
			vAddr.getText().length() == 0 &&
			sTime == -1 &&
			categorySet.size() == 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private class CategoryRecorder implements CategorySelectListener {

		@Override
		public void setSelectedCategory(String category, boolean checked) {
			// TODO Auto-generated method stub
			if(checked) {
				categorySet.add(category);
			} else {
				categorySet.remove(category);
			}
		}
		
	}
	
	private HashSet<String> categorySet = new HashSet<String>();

}
