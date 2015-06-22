package com.alexan.findevents.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.FrameworkActivity;
import com.alexan.findevents.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AccountInfoActivity extends SherlockActivity {
	
	private RelativeLayout vIcon;
	private TextView vNickname;
	private TextView vGender;
	private TextView vArea;
	private TextView vSign;
	
	private Button vChangePwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountinfo);
		
		initView();
		
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			Intent mainInt = new Intent(this,
					FrameworkActivity.class);
			mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainInt);
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}



	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vNickname = (TextView)findViewById(R.id.nickname);
		String currUser =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
				getString("curr_user", "none");
		vNickname.setText(currUser);
		vIcon = (RelativeLayout) findViewById(R.id.act_accountinfo_icon);
		vIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		ImageView touxiang = (ImageView)findViewById(R.id.touxiang);
		DisplayImageOptions options = new DisplayImageOptions.Builder()   
        .cacheInMemory(true)  
        .cacheOnDisc(true)  
        .bitmapConfig(Bitmap.Config.RGB_565)  
        .build();  
  
		/*ImageLoader.getInstance().displayImage(photoUrl, touxiang, options);*/
		vChangePwd = (Button) findViewById(R.id.act_accountinfo_logout);
		vChangePwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.edit().putString("curr_user", "none").commit();
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit().putLong("curr_user_id", 0).commit();
				Toast.makeText(AccountInfoActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
				
				/*Intent intent = new Intent();  //Itent就是我们要发送的内容
	            intent.putExtra("nouser", 1);   
	            intent.setAction("nouser");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
	            sendBroadcast(intent);   //发送广播
	            
*/	            
				setResult(1);
				finish();
			}
		});
	}
	
}
