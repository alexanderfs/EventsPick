package com.alexan.findevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexan.findevents.crj.NetAsyncTask;
import com.alexan.findevents.crj.Util;
import com.alexan.findevents.dao.DBPerson;
import com.alexan.findevents.dao.DBPersonDao;
import com.alexan.findevents.dao.DBUser;
import com.alexan.findevents.dao.DBUserDao;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class LoginActivity extends Activity {

	private ImageView vImage;
	private EditText vUsername;
	private EditText vPassword;
	private TextView vForgotPwd;
	private Button vLogin;
	private String userName;
	private String passWord;
	private int logType = 0;
	
	private String url = "http://123.57.45.183/user/submit_login";
	/*private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg){
			Intent intent = new Intent(LoginActivity.this,
					FrameworkActivity.class);
			startActivity(intent);
			finish();
		}
	};*/
	
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
					.putString("curr_user", userName).commit();
					
					JSONObject data = obj.getJSONObject("data");
					long userid = (long)data.getInt("UserId");
					
					
					PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.edit().putLong("curr_user_id", userid).commit();
					Toast.makeText(LoginActivity.this, "登陆成功"+userid,
							Toast.LENGTH_SHORT).show();
					/*Long userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);*/
					
					finish();
				}	
				else{
					Toast.makeText(LoginActivity.this, json_data,
							Toast.LENGTH_SHORT).show();
				} 
			} catch (Exception e) {
				Toast.makeText(LoginActivity.this, "错误",
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
		setContentView(R.layout.activity_login);
		logType = getIntent().getIntExtra("log_type", 0);
		initView();
	}

	private void initView() {
		vImage = (ImageView) findViewById(R.id.act_login_icon);
		vUsername = (EditText) findViewById(R.id.act_login_username);
		vPassword = (EditText) findViewById(R.id.act_login_password);
		vLogin = (Button) findViewById(R.id.act_login_login);
		vLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkValid()) {
					
					
					int code=0;
					userName=vUsername.getText().toString();
					passWord=vPassword.getText().toString();
//					NetAsyncTask netAsyncTask = new NetAsyncTask(vLogin,userName,passWord, code);
//					netAsyncTask.execute();
//					storeUserData();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String params = "username="+userName+"&Password="+passWord;
							String result = Util.httpPost(url, params);
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("params", result);
							message.setData(bundle);
							mHandler.sendMessage(message);
							
						}
					}).start();
					if (logType == 0) {
						Intent mainInt = new Intent(LoginActivity.this,
								StartupActivity.class);
						mainInt.putExtra("type", 0);
						mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mainInt);
					} else {
						Intent mainInt = new Intent(LoginActivity.this,
								FrameworkActivity.class);
						// mainInt.putExtra("type", 0);
						mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mainInt);
					}
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "输入不完全，请重新输入",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		vForgotPwd = (TextView) findViewById(R.id.act_login_forgetpwd);
		vForgotPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	private boolean checkValid() {
		if (vUsername.getText().length() == 0
				|| vPassword.getText().length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/*private void storeUserData() {
		DBUser u = new DBUser();
		u.setUsername(vUsername.getText().toString());

		DBUserDao ud = DBHelper.getInstance(this).getUserDao();
		ud.deleteAll();
		ud.insert(u);
		
		QueryBuilder<DBPerson> qbp = DBHelper.getInstance(this).getPersonDao().queryBuilder()
				.where(DBPersonDao.Properties.Nickname.eq(u.getUsername()));
		long personID = 0;
		if(qbp.count() == 0) {
			DBPerson p = new DBPerson();
			p.setNickname(u.getUsername());
			personID = DBHelper.getInstance(this).getPersonDao().insert(p);
		}
		//created by CRJ找到原有用户
		else{
			DBPerson p = qbp.list().get(0);
			personID = p.getId();
		}

		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit().putString("curr_user", u.getUsername()).commit();
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit().putLong("curr_user_id", personID).commit();
	}*/
}
