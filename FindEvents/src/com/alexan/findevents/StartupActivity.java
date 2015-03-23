package com.alexan.findevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = new Intent(this, LaunchActivity.class);
		startActivityForResult(i, 1);
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == RESULT_CANCELED) {
			finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
			int intType = intent.getIntExtra("type", 0);
			switch(intType) {
			case 0: {
				Intent i = new Intent(this, FrameworkActivity.class);
				startActivity(i);
				finish();
				break;
			}
			case 1: {
				finish();
				break;
			}
			}
		}
	}
	
	
}
