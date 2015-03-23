package com.alexan.findevents.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;

public class FriendInfoActivity extends SherlockActivity {
	private Button vMainpage;
	private Button vAddFriend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("好友详情");
		setContentView(R.layout.activity_friendinfo);
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
		vMainpage = (Button) findViewById(R.id.act_friendinfo_mainpage);
		vMainpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FriendInfoActivity.this, FriendMainPageActivity.class);
				startActivity(i);
			}
		});
		vAddFriend = (Button) findViewById(R.id.act_friendinfo_addfriend);
		vAddFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(FriendInfoActivity.this, "好友请求已发出，等待确认", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
