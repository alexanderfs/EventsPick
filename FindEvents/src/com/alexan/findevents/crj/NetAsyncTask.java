package com.alexan.findevents.crj;

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

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;

import com.alexan.findevents.FrameworkActivity;
import com.alexan.findevents.LoginActivity;
import com.alexan.findevents.StartupActivity;

public class NetAsyncTask extends AsyncTask{

	private Button button;
	private String userName;
	private String passWord;
	private int code;
	
	public NetAsyncTask(Button button, String userName, String passWord, int code) {
		super();
		this.button = button;
		this.userName = userName;
		this.passWord = passWord;
		this.code = code;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		button.setText("登录中...");
		System.out.println("Pre");
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//code = Integer.parseInt(result.toString());
		System.out.println("code为"+code);
		button.setText("登陆成功");
	
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		System.out.println("doInBack");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userlogin(userName, passWord);
	}

	public int userlogin(String emailAddr, String password){
		System.out.println("用户名密码为"+userName+passWord);
		HttpClient hc=new DefaultHttpClient();
		code=5;
		HttpGet hg=new HttpGet("http://10.103.243.101:8080/Crj/kao.txt");
		try {
			HttpResponse hr=hc.execute(hg);
			int code=hr.getStatusLine().getStatusCode();
			if(code==200){
				HttpEntity entity=hr.getEntity();
				InputStream is=entity.getContent();
				BufferedReader reader=new BufferedReader(new InputStreamReader(is));
				
				String line = reader.readLine();
				System.out.println(line);
				
			}
		} catch (ClientProtocolException e) {
			System.out.println("Normal5``````````");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Normal6``````````");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
}
