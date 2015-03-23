package com.alexan.findevents.me;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;

public class SpotAddActivity extends SherlockActivity {
	private EditText vSearch;
	private Button vSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("添加地点");
		setContentView(R.layout.activity_spotadd);
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
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vSearch = (EditText) findViewById(R.id.act_spotadd_searchet);
		vSubmit = (Button) findViewById(R.id.act_spotadd_searchbtn);
		vSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(SpotAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
