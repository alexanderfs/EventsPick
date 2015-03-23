package com.alexan.findevents.friend;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBFriend;
import com.alexan.findevents.dao.DBFriendDao;
import com.alexan.findevents.dao.DBPerson;
import com.alexan.findevents.dao.DBPersonDao;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class FriendAddActivity extends SherlockActivity {
	
	private EditText vSearch;
	private Button vSubmit;
	private LinearLayout vSearchLocal;
	private LinearLayout vSearchWeibo;
	private LinearLayout vSearchQQ;

	private long userID;
	private String userName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("添加好友");
		setContentView(R.layout.activity_friendadd);
		userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			    .getLong("curr_user_id", -1);

		userName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			    .getString("curr_user", "");
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



	private void initView() {
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vSearch = (EditText) findViewById(R.id.act_friendadd_searchet);
		vSubmit = (Button) findViewById(R.id.act_friendadd_searchbtn);
		vSearchLocal = (LinearLayout) findViewById(R.id.act_friendadd_local);
		vSearchLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		vSubmit.setOnClickListener(new OnClickListener() {
			String input;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				input = vSearch.getText().toString();
				if(input == "" || input.equals(userName)){
					Toast.makeText(FriendAddActivity.this, "输入为空或无效", Toast.LENGTH_SHORT).show();
				}
				else{
					QueryBuilder<DBFriend> qb = DBHelper.getInstance(FriendAddActivity.this).getFriendDao().queryBuilder()
							.where(DBFriendDao.Properties.UserID.eq(userID), DBFriendDao.Properties.Friendname.eq(input));
					List<DBFriend> df = qb.list();
					if(df.size()!=0){
						Toast.makeText(FriendAddActivity.this, "好友已存在", Toast.LENGTH_SHORT).show();
					}
					else{
						List<DBPerson> dp = DBHelper.getInstance(FriendAddActivity.this).getPersonDao().queryBuilder()
								.where(DBPersonDao.Properties.Nickname.eq(input)).list();
						if(dp.size()==0){
							Toast.makeText(FriendAddActivity.this, "查无此人", Toast.LENGTH_SHORT).show();
						}
						else{
							DBFriend newfriend = new DBFriend();
							newfriend.setFriendname(input);
							newfriend.setUserID(userID);
//							newfriend.setFriendID(dp.get(0).getId());
							newfriend.setPerson(dp.get(0));
							newfriend.setTimestamp(System.currentTimeMillis());
							DBHelper.getInstance(FriendAddActivity.this).getFriendDao().insert(newfriend);
							Toast.makeText(FriendAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
						}
						
					}
				}
			}
		});
		vSearchWeibo = (LinearLayout) findViewById(R.id.act_friendadd_weibo);
		vSearchWeibo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		vSearchQQ = (LinearLayout) findViewById(R.id.act_friendadd_qq);
		vSearchQQ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
