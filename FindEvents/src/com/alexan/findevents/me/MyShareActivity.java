package com.alexan.findevents.me;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBCommentDao.Properties;
import com.alexan.findevents.event.EventDetailActivity;
import com.alexan.findevents.friend.FCEntity;
import com.alexan.findevents.friend.FriendCircleAdapter;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class MyShareActivity extends SherlockActivity {
	private ListView vMainList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("我的分享");
		setContentView(R.layout.activity_myshare);
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
		vMainList = (ListView) findViewById(R.id.act_myshare_mainlist);
		eventList = getEventList();
		FriendCircleAdapter fca = new FriendCircleAdapter(this, eventList);
		vMainList.setAdapter(fca);
		vMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", eventList.get(position).getEvent().getId());
				Intent i = new Intent(MyShareActivity.this, EventDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
	}
	
	private List<FCEntity> eventList;
	
	private long getUserID() {
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);
	}
	
	private List<FCEntity> getEventList() {
		Long userID = getUserID();
		List<FCEntity> eventList = new ArrayList<FCEntity>();
		QueryBuilder<DBEvent> qbe = DBHelper.getInstance(this).getEventDao().queryBuilder()
				.where(DBEventDao.Properties.UserID.eq(userID));
		List<DBEvent> t1 = qbe.list();
		for(DBEvent e: t1) {
			QueryBuilder<DBComment> qbc = DBHelper.getInstance(this).getCommentDao().queryBuilder()
					.where(Properties.EventID.eq(e.getId()), Properties.CommentType.eq(3));
			List<DBComment> t2 = qbc.list();
			for(DBComment c: t2) {
				FCEntity fe = new FCEntity(e, c);
				eventList.add(fe);
			}
		}
		return eventList;
	}
}
