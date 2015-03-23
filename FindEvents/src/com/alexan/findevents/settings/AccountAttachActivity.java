package com.alexan.findevents.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alexan.findevents.R;

public class AccountAttachActivity extends Activity {
	private Button vBack;
	private LinearLayout vAttachPhone;
	private LinearLayout vAttachWeixin;
	private LinearLayout vAttachWeibo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_accountattach);
		initView();
	}

	private void initView() {
		vBack = (Button) findViewById(R.id.act_accountattach_back);
		vBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		vAttachPhone = (LinearLayout) findViewById(R.id.act_accountattach_phone);
		vAttachPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vAttachWeixin = (LinearLayout) findViewById(R.id.act_accountattach_weixin);
		vAttachWeixin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		vAttachWeibo = (LinearLayout) findViewById(R.id.act_accountattach_weibo);
		vAttachWeibo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
