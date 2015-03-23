package com.alexan.findevents.event;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBCommentDao;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class EventCommentActivity extends SherlockActivity {
	
	private ListView vCommentList;
	private EditText vTextContent;
	private Button vSubmit;
	private long eventID;
	private DBEvent currEvent;

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			setResult(RESULT_OK);
			finish();
			break;
		}
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		eventID = getIntent().getLongExtra("eventID", 0);
		
		setContentView(R.layout.activity_eventcomment);
		setTitle("评论列表");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		vCommentList = (ListView) findViewById(R.id.act_eventcomm_list);
		CommentsAdapter cs = new CommentsAdapter(this, getEventCommentList());
		vCommentList.setAdapter(cs);
		
		vTextContent = (EditText) findViewById(R.id.act_eventcomm_editcomment);
		
		vSubmit = (Button) findViewById(R.id.act_eventcomm_submit);
		vSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(vTextContent.getText().length() == 0) {
					Toast.makeText(EventCommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				currEvent = DBHelper.getInstance(EventCommentActivity.this).getEventDao().load(eventID);
				DBComment dc = new DBComment();
				dc.setEventID(eventID);
				dc.setCommentType(4);
				dc.setUserID(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0));
				
		        String username =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("curr_user", "none");
				dc.setUsername(username);
				dc.setComentContent(vTextContent.getText().toString());
				dc.setTimestamp(System.currentTimeMillis());
				DBHelper.getInstance(EventCommentActivity.this).getCommentDao().insert(dc);
				
				currEvent.setCommentNum((currEvent.getCommentNum() == null ? 0 : currEvent.getCommentNum()) + 1);
				DBHelper.getInstance(EventCommentActivity.this).getEventDao().update(currEvent);
				CommentsAdapter cs = new CommentsAdapter(EventCommentActivity.this, getEventCommentList());
				vCommentList.setAdapter(cs);
				Toast.makeText(EventCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private List<DBComment> getEventCommentList() {
		List<DBComment> myList;
		QueryBuilder<DBComment> qbc = DBHelper.getInstance(this).getCommentDao().queryBuilder()
				.where(DBCommentDao.Properties.EventID.eq(eventID));
		myList = qbc.list();
		return myList;
	}

}
