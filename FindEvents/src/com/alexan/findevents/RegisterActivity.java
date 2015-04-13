package com.alexan.findevents;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexan.findevents.crj.Util;

public class RegisterActivity extends Activity {
	private EditText vUsername;
	private EditText vEmail;
	private EditText vPassword;
	private EditText vConfirmPwd;
	private Button vRegister;
	
	private String url = "http://123.57.45.183/user/CreateUser";
	
	private int registerType = 0;
	Handler mHandler=new Handler(){
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
        	Bundle bundle = msg.getData();
			String result = bundle.getString("params");
			
			try {
				// 处理结果
				JSONObject obj = new JSONObject(result);		
				
				String json_data = obj.getString("msg");
	
				if (obj.getString("code").equals("10000")) {
					
					PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
					.putString("curr_user", vEmail.getText().toString()).commit();
					
					JSONObject data = obj.getJSONObject("data");
					long userid = (long)data.getInt("UserId");
					
					PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.edit().putLong("curr_user_id", userid).commit();
					
					/*Long userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);*/
					Toast.makeText(RegisterActivity.this, "注册成功"+userid,
							Toast.LENGTH_SHORT).show();
					finish();
				}	
				else{
					Toast.makeText(RegisterActivity.this, json_data,
							Toast.LENGTH_SHORT).show();
				} 
			} catch (Exception e) {
				Toast.makeText(RegisterActivity.this, "错误",
						Toast.LENGTH_SHORT).show();
			}

			super.handleMessage(msg); 
        }         
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		registerType = getIntent().getIntExtra("register_type", 0);
		initView();
		
		
	}

	private void initView() {
		vUsername = (EditText) findViewById(R.id.act_register_username);
		vPassword = (EditText) findViewById(R.id.act_register_password);
		vEmail = (EditText) findViewById(R.id.act_register_email);
		vConfirmPwd = (EditText) findViewById(R.id.act_register_confirm_password);
		vRegister = (Button) findViewById(R.id.act_register);
		vRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkValid() == 0) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String params = "NickName="+vUsername.getText().toString()+"&email="+vEmail.getText().toString()+/*"&mobileNo="+""+*/"&password="+vPassword.getText().toString()+"&devise="+android.os.Build.MODEL;
							String result = Util.httpPost(url, params);
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("params", result);
							message.setData(bundle);
							mHandler.sendMessage(message);
							
						}
					}).start();
					//storeUserData();
					if (registerType == 0) {
						Intent mainInt = new Intent(RegisterActivity.this,
								StartupActivity.class);
						mainInt.putExtra("type", 0);
						mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mainInt);
					} else {
						Intent mainInt = new Intent(RegisterActivity.this,
								FrameworkActivity.class);
						// mainInt.putExtra("type", 0);
						mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mainInt);
					}
				} else {
					Toast.makeText(RegisterActivity.this, "输入错误请重新输入",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private int checkValid() {
		if (vUsername.getText().length() == 0
				|| vPassword.getText().length() == 0
				|| vEmail.getText().length() == 0
				|| vConfirmPwd.getText().length() == 0
				|| !vPassword.getText().toString()
						.equals(vConfirmPwd.getText().toString())) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private void storeUserData() {
		/*DBUser u = new DBUser();
		u.setUsername(vUsername.getText().toString());
		u.setEmailAddr(vEmail.getText().toString());
		
		DBUserDao ud = DBHelper.getInstance(this).getUserDao();
		ud.deleteAll();
		ud.insert(u);
		
		QueryBuilder<DBPerson> qbp = DBHelper.getInstance(this).getPersonDao().queryBuilder()
				.where(DBPersonDao.Properties.Nickname.eq(u.getUsername()));
		if(qbp.count() == 0) {
			DBPerson p = new DBPerson();
			p.setNickname(u.getUsername());
			p.setEmailAddr(u.getEmailAddr());
			DBHelper.getInstance(this).getPersonDao().insert(p);
		}

		
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
			.putString("curr_user", u.getUsername()).commit();*/
	}
}
