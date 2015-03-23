package com.alexan.findevents.settings;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexan.findevents.R;
import com.alexan.findevents.util.NetworkTool;

public class FeedbackActivity extends Activity {

	private Button vbutton;
	private EditText vtext;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		/*vbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断标题和内容是否够长
				if (vtext.getText().length() > 0) {
					// 采用异步Task进行联网请求
					new AsyncTask<Void, Void, String>() {

						@Override
						protected void onPreExecute() {
							//pd.show();
						};

						@Override
						protected String doInBackground(Void... params) {
							// 构造参数
							String param = "&content="
									+ vtext.getText()
									+ "&username="
									+ sharedPreferences.getString("username",
											"") + "&name="
									+ sharedPreferences.getString("name", "");
							String result = new NetworkTool().httpPost(url,
									param);
							return result;
						}

						@Override
						protected void onPostExecute(String result) {
							try {
								// 处理结果
								JSONObject obj = new JSONObject(result);
								if (obj.getString("status").equals("5")) {
									Toast.makeText(FeedbackActivity.this,
											obj.getString("mes"),
											Toast.LENGTH_SHORT).show();
									finish();
									
								} else {
									Toast.makeText(FeedbackActivity.this,
											obj.getString("mes"),
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								Toast.makeText(FeedbackActivity.this,
										"Configure.OTHER_ERROR",
										Toast.LENGTH_SHORT).show();
							}
						};

					}.execute();
				} else {
					
					if (vtext.getText().length() < 1) {
						Toast.makeText(FeedbackActivity.this, "内容不能为空！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});*/
	}
	

}
