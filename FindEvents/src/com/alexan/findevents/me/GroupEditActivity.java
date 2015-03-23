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
import com.alexan.findevents.dao.DBGroupDao;
import com.alexan.findevents.dao.DBGroupFriend;
import com.alexan.findevents.dao.DBGroupFriendDao;
import com.alexan.findevents.friend.PickSortAdapter;
import com.alexan.findevents.util.CharacterParser;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.PinyinComparator;
import com.alexan.findevents.util.SortModel;
import com.alexan.findevents.view.SideBar;
import com.alexan.findevents.view.SideBar.OnTouchingLetterChangedListener;

import de.greenrobot.dao.query.QueryBuilder;

public class GroupEditActivity extends SherlockActivity{
	public List<Long> idlist = new ArrayList<Long>();
	
	private EditText vSearch;
	private Button vSearchBtn;
	
	List<DBGroupFriend> result;
	private DBGroup group;
	private long userID;
	
	private ListView vList;
	private SideBar vSidebar;
	private TextView vIndicator;
	
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private PickSortAdapter sa;
	private List<SortModel> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("ͨ编辑群组");
		setContentView(R.layout.activity_groupedit);
		Intent intent = getIntent();
		long groupId = intent.getLongExtra("groupid", -1);
		userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			    .getLong("curr_user_id", -1); 
		group = DBHelper.getInstance(this).getGroupDao().queryBuilder().where(DBGroupDao.Properties
				.Id.eq(groupId)).list().get(0);
		result = DBHelper.getInstance(this).getGroupFriendDao().queryBuilder().where(DBGroupFriendDao.Properties
				.GroupID.eq(groupId)).list();
		idlist.clear();
		for(int i=0;i<result.size();i++){
			idlist.add(result.get(i).getFriendID());
			
		}
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
		// TODO Auto-generated method stub
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vSearch = (EditText) findViewById(R.id.act_groupedit_groupname);
		vSearch.setText(group.getGroupname());
		vSearchBtn = (Button) findViewById(R.id.act_groupedit_searchbtn);
		vSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String gname = vSearch.getText().toString();
				if(gname!=null){//刷新数据库里的群组信息
					group.setGroupname(gname);
					group.setGroupcapacity(idlist.size());
					DBHelper.getInstance(GroupEditActivity.this).getGroupDao().update(group);
					
					QueryBuilder<DBGroupFriend> qbgf = DBHelper.getInstance(GroupEditActivity.this).getGroupFriendDao()
							.queryBuilder().where(DBGroupFriendDao.Properties.GroupID.eq(group.getId()));
				
					DBHelper.getInstance(GroupEditActivity.this).getGroupFriendDao().deleteInTx(qbgf.list());
					
					for(long id : idlist){
						DBGroupFriend dgf = new DBGroupFriend();
						dgf.setGroupID(group.getId());
						dgf.setFriendID(id);
						DBHelper.getInstance(GroupEditActivity.this).getGroupFriendDao().insert(dgf);
					}
					
					Toast.makeText(GroupEditActivity.this, "修改成功,ID为"+group.getId(), Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
		
		vList = (ListView) findViewById(R.id.act_groupedit_list);

		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		vSidebar = (SideBar) findViewById(R.id.act_groupedit_sidebar);
		vIndicator = (TextView) findViewById(R.id.act_groupedit_indicator);
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
			sortModel.setId(date.get(i).getFriendID());
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
