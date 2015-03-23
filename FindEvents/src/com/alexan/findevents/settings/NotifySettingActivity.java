package com.alexan.findevents.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.alexan.findevents.R;

public class NotifySettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notifysetting);
		
		initView();
	}

	private void initView() {
		
	}
}
