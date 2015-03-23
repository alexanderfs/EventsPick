package com.alexan.findevents.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.event.EventDetailActivity;

public class FriendMainPageActivity extends SherlockActivity {
	private ListView vMainList;
	private ImageView vImage;
	private TextView vName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("他的主页");
		setContentView(R.layout.activity_friendmp);
		initView();
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			finish();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}



	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vMainList = (ListView) findViewById(R.id.act_friendmp_funclist);
		View listHead = this.getLayoutInflater().inflate(R.layout.list_friendcircle_head, null);
		vImage = (ImageView) listHead.findViewById(R.id.list_fc_head_image);
		vName = (TextView) listHead.findViewById(R.id.list_fc_head_name);
		vMainList.addHeaderView(listHead);
		FriendCircleAdapter fca = new FriendCircleAdapter(this, null);
		vMainList.setAdapter(fca);
		vMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(FriendMainPageActivity.this, EventDetailActivity.class);
				startActivity(i);
			}
		});
	}
}
