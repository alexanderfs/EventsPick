package com.alexan.findevents.me;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBFriend;
import com.alexan.findevents.dao.DBFriendDao;
import com.alexan.findevents.dao.DBGroup;
import com.alexan.findevents.dao.DBGroupFriend;
import com.alexan.findevents.friend.FriendInfoActivity;
import com.alexan.findevents.friend.PickSortAdapter;
import com.alexan.findevents.util.CharacterParser;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.PinyinComparator;
import com.alexan.findevents.util.SortModel;
import com.alexan.findevents.view.SideBar;
import com.alexan.findevents.view.SideBar.OnTouchingLetterChangedListener;

public class GroupAddActivity extends SherlockActivity {
	public List<Long> idlist = new ArrayList<Long>();
	
	private EditText vSearch;
	private Button vSearchBtn;
	private ListView vList;
	private SideBar vSidebar;
	private TextView vIndicator;
	
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private PickSortAdapter sa;
	private List<SortModel> dataList;
	
	private long userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		idlist.clear();
		setTitle("ͨ新建群组");
		setContentView(R.layout.activity_groupadd);
		userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			    .getLong("curr_user_id", -1); 
		initView();
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
		vSearch = (EditText) findViewById(R.id.act_groupadd_groupname);
		vSearchBtn = (Button) findViewById(R.id.act_groupadd_searchbtn);
		vSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String gname = vSearch.getText().toString();
				if(!gname.equals("") && gname != null){
					DBGroup dg = new DBGroup();
					dg.setGroupname(gname);
					dg.setGroupcapacity(idlist.size());
					dg.setUserID(userID);
					DBHelper.getInstance(GroupAddActivity.this).getGroupDao().insert(dg);
					dg.setId(DBHelper.getInstance(GroupAddActivity.this).getGroupDao().getKey(dg));
					for(long id : idlist){
						DBGroupFriend dgf = new DBGroupFriend();
						dgf.setGroupID(dg.getId());
						dgf.setFriendID(id);
						DBHelper.getInstance(GroupAddActivity.this).getGroupFriendDao().insert(dgf);
					}
					Toast.makeText(GroupAddActivity.this, "创建成功,ID="+dg.getId(), Toast.LENGTH_SHORT).show();
					finish();
				}
				else{
					Toast.makeText(GroupAddActivity.this, "请输入群组名", Toast.LENGTH_SHORT).show();
				}
			}
		});
		vList = (ListView) findViewById(R.id.act_groupadd_list);

		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		vSidebar = (SideBar) findViewById(R.id.act_groupadd_sidebar);
		vIndicator = (TextView) findViewById(R.id.act_groupadd_indicator);
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
				Intent i = new Intent(GroupAddActivity.this, FriendInfoActivity.class);
				startActivity(i);
			}
		});	
		
		List<DBFriend> df = DBHelper.getInstance(this).getFriendDao().queryBuilder().where(DBFriendDao.Properties
				.UserID.eq(userID)).list();
		dataList = filledData(df);
		Collections.sort(dataList, pinyinComparator);
		sa = new PickSortAdapter(this, dataList, idlist);
		vList.setAdapter(sa);
	}
	
	private List<SortModel> filledData(List<DBFriend> date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setId(date.get(i).getPerson().getId());
			sortModel.setName(date.get(i).getFriendname());
			String pinyin = characterParser.getSelling(date.get(i).getFriendname());
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
