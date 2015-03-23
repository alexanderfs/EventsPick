package com.alexan.findevents.me;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBGroupFriend;
import com.alexan.findevents.dao.DBGroupFriendDao;
import com.alexan.findevents.friend.FriendInfoActivity;
import com.alexan.findevents.friend.SortAdapter;
import com.alexan.findevents.util.CharacterParser;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.PinyinComparator;
import com.alexan.findevents.util.SortModel;
import com.alexan.findevents.view.SideBar;
import com.alexan.findevents.view.SideBar.OnTouchingLetterChangedListener;

import de.greenrobot.dao.query.QueryBuilder;

public class GroupInfoActivity extends SherlockActivity {
	private EditText vSearch;
	private Button vSearchBtn;
	private ListView vList;
	private SideBar vSidebar;
	private TextView vIndicator;
	
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SortAdapter sa;
	private List<SortModel> dataList;
	
	private long groupId;
	List<DBGroupFriend> memberList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("ͨ群组详情");
		setContentView(R.layout.activity_contactlist);
		Intent intent = getIntent();
		groupId = intent.getLongExtra("groupId", -1);
		System.out.println("infoCreate,groupid"+groupId);
		memberList = new ArrayList<DBGroupFriend>();
		QueryBuilder<DBGroupFriend> qbGroupFriend = DBHelper.getInstance(this)
				.getGroupFriendDao().queryBuilder().where(DBGroupFriendDao.Properties.GroupID.eq(groupId));
		memberList = qbGroupFriend.list();
		for(int i=0;i<memberList.size();i++){
			System.out.println(memberList.get(i).getFriendID());
		}
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
		
		}
		return super.onOptionsItemSelected(item);
	}



	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vSearch = (EditText) findViewById(R.id.act_contactlist_searchet);
		vSearchBtn = (Button) findViewById(R.id.act_contactlist_searchbtn);
		vSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		vList = (ListView) findViewById(R.id.act_contactlist_list);

		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		vSidebar = (SideBar) findViewById(R.id.act_contactlist_sidebar);
		vIndicator = (TextView) findViewById(R.id.act_contactlist_indicator);
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
		
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(GroupInfoActivity.this, FriendInfoActivity.class);
				startActivity(i);
			}
		});
		
		dataList = filledData(memberList);
		
		Collections.sort(dataList, pinyinComparator);
		sa = new SortAdapter(this, dataList);
		vList.setAdapter(sa);
	}
	
	private List<SortModel> filledData(List<DBGroupFriend> date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).getFriends().getNickname());
			String pinyin = characterParser.getSelling(date.get(i).getFriends().getNickname());
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
