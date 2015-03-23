package com.alexan.findevents.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;

public class SettingsActivity extends SherlockActivity {

	private ActionBar vACBar;
	private TextView vTV1;
	private TextView vTV2;
	private TextView vTV3;
	private TextView vTV4;
	private TextView vTV5;
	private TextView vTV6;
	private TextView vTV7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
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
		vACBar = getSupportActionBar();
		vACBar.setDisplayHomeAsUpEnabled(true);
	/*	vTV1 = (TextView) findViewById(R.id.fgt_setting_account);
		vTV1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, AccountInfoActivity.class);
				startActivity(i);
			}
		});
		vTV2 = (TextView) findViewById(R.id.fgt_setting_safe);
		vTV2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, SafeSettingActivity.class);
				startActivity(i);
			}
		});
		vTV3 = (TextView) findViewById(R.id.fgt_setting_accountattach);
		vTV3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, AccountAttachActivity.class);
				startActivity(i);
			}
		});
		vTV4 = (TextView) findViewById(R.id.fgt_setting_notify);
		vTV4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, NotifySettingActivity.class);
				startActivity(i);
			}
		});
		vTV5 = (TextView) findViewById(R.id.fgt_setting_privacy);
		vTV5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, PrivacySettingActivity.class);
				startActivity(i);
			}
		});*/
		vTV6 = (TextView) findViewById(R.id.fgt_setting_feedback);
		vTV6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, FeedbackActivity.class);
				startActivity(i);
			}
		});
		vTV7 = (TextView) findViewById(R.id.fgt_setting_about);
		vTV7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SettingsActivity.this, AboutUsActivity.class);
				startActivity(i);
			}
		});
	}
}
