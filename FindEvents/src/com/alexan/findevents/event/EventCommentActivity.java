package com.alexan.findevents.event;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.alexan.findevents.crj.JSONparse;
import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBCommentDao;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class EventCommentActivity extends SherlockActivity {
	
	private ListView vCommentList;
	private EditText vTextContent;
	private Button vSubmit;
	private long eventID;
	private DBPickEvent currEvent;

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
					
					JSONObject data = obj.getJSONObject("data");
					long eventid = (long)data.getInt("EventId");
					
					Toast.makeText(EventCommentActivity.this, "发布成功"+eventid,
							Toast.LENGTH_SHORT).show();
					/*Long userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);*/
					
					finish();
				}	
				else{
					Toast.makeText(EventCommentActivity.this, json_data,
							Toast.LENGTH_SHORT).show();
				} 
			} catch (Exception e) {
				Toast.makeText(EventCommentActivity.this, "错误",
						Toast.LENGTH_SHORT).show();
			}

			super.handleMessage(msg); 
        }         
    };

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
				currEvent = DBHelper.getInstance(EventCommentActivity.this).getPickEventDao().load(eventID);
				DBComment dc = new DBComment();
				dc.setEventID(eventID);
				dc.setCommentType(4);
				long userId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);
				dc.setUserID(userId);
				
		        String username =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("curr_user", "none");
				dc.setUsername(username);
				dc.setComentContent(vTextContent.getText().toString());
				dc.setTimestamp(System.currentTimeMillis());
				DBHelper.getInstance(EventCommentActivity.this).getCommentDao().insert(dc);
				
				String params = "UserId="+userId+"&EventId="+eventID+
						"&ParentCommentId="+0+"&CommentDetails="+vTextContent.getText().toString();
				
				JSONparse.postComment(mHandler, params);
				
				
				currEvent.setCommentNum((currEvent.getCommentNum() == null ? 0 : currEvent.getCommentNum()) + 1);
				DBHelper.getInstance(EventCommentActivity.this).getPickEventDao().update(currEvent);
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
