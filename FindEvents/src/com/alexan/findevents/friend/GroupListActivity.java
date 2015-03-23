package com.alexan.findevents.friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBGroup;
import com.alexan.findevents.dao.DBGroupDao;
import com.alexan.findevents.me.GroupAddActivity;
import com.alexan.findevents.me.GroupEditActivity;
import com.alexan.findevents.me.GroupInfoActivity;
import com.alexan.findevents.util.CharacterParser;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.PinyinComparator;
import com.alexan.findevents.util.SortModel;
import com.alexan.findevents.view.SideBar;
import com.alexan.findevents.view.SideBar.OnTouchingLetterChangedListener;

import de.greenrobot.dao.query.QueryBuilder;

public class GroupListActivity extends SherlockActivity {
	final CharSequence[] items = {"修改群组","删除群组"};
	private EditText vSearch;
	private Button vSearchBtn;
	private ListView vList;
	private SideBar vSidebar;
	private TextView vIndicator;
	
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SortAdapter sa;
	private List<SortModel> dataList;
	//created by crj
	private List<DBGroup> groupList;
	private long userID;
	
	long groupid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("群组列表");
		setContentView(R.layout.activity_spotlist);
		userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			    .getLong("curr_user_id", -1);
		groupList = DBHelper.getInstance(this).getGroupDao().queryBuilder()
				.where(DBGroupDao.Properties.UserID.eq(userID)).list();
		initView();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.menu_add, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			finish();
			return true;
		}
		case R.id.action_add: {
			Intent i = new Intent(this, GroupAddActivity.class);
			startActivity(i);
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}



	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vSearch = (EditText) findViewById(R.id.act_spotlist_searchet);
		vSearchBtn = (Button) findViewById(R.id.act_spotlist_searchbtn);
		vSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
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
		
		vList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(GroupListActivity.this);
				builder.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							groupid = dataList.get(position).getId();
							Intent intent = new Intent();
							intent.putExtra("groupid", groupid);
							intent.setClass(GroupListActivity.this, GroupEditActivity.class);
							startActivity(intent);
							break;
						case 1:
							groupid = dataList.get(position).getId();
							DBHelper.getInstance(GroupListActivity.this).getGroupDao().deleteByKey(groupid);
							Toast.makeText(GroupListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
							break;

						default:
							break;
						}
					}
				}).show();
				//builder.create();
				return true;
			}
			
		});
		
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(GroupListActivity.this, GroupInfoActivity.class);
				i.putExtra("groupId", dataList.get(position).getId());
				startActivity(i);
			}
		});
		
		dataList = filledData(groupList);
		Collections.sort(dataList, pinyinComparator);
		sa = new SortAdapter(this, dataList);
		vList.setAdapter(sa);
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
	
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = dataList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : dataList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		Collections.sort(filterDateList, pinyinComparator);
		sa.updateListView(filterDateList);
	}
}
