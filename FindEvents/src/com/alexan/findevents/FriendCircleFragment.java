package com.alexan.findevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBCommentDao.Properties;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBGroupFriend;
import com.alexan.findevents.dao.DBPerson;
import com.alexan.findevents.dao.DBPersonDao;
import com.alexan.findevents.friend.FCEntity;
import com.alexan.findevents.friend.FriendCircleAdapter;
import com.alexan.findevents.util.DBHelper;

import de.greenrobot.dao.query.QueryBuilder;

public class FriendCircleFragment extends Fragment {
	
	private ListView vMainList;
	private ImageView vImage;
	private TextView vName;
	
	private long userID;
	private List<Long> visibleList;
	private List<FCEntity> eventList;
 	//code由Crj添加
	int code = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		visibleList = new ArrayList<Long>();
		eventList = new ArrayList<FCEntity>();
		View v = inflater.inflate(R.layout.activity_friendcircle, container, false);
		initView(v);
		((FrameworkActivity)getActivity()).setPFlag(4);
		((FrameworkActivity)getActivity()).supportInvalidateOptionsMenu();
		getActivity().setTitle("");
		return v;
	}
	
	private void initView(View v) {
		
		vMainList = (ListView) v.findViewById(R.id.act_friendcircle_mainlist);
		View listHead = getActivity().getLayoutInflater().inflate(R.layout.list_friendcircle_head, null);
		vImage = (ImageView) listHead.findViewById(R.id.list_fc_head_image);
		vName = (TextView) listHead.findViewById(R.id.list_fc_head_name);
		vMainList.addHeaderView(listHead);
		getEventList();
		FriendCircleAdapter fca = new FriendCircleAdapter(getActivity(), eventList);
		vMainList.setAdapter(fca);
		/*vMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", eventList.get(position).getEvent().getId());
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});*/
	}
	
	private long getUserID() {
		return PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getLong("curr_user_id", 0);
	}
	
	private void getVisible(long userID){
		visibleList.clear();
		DBPerson person = DBHelper.getInstance(getActivity()).getPersonDao().queryBuilder().where(DBPersonDao.Properties
				.Id.eq(userID)).list().get(0);
		List<DBGroupFriend> list = person.getThegroups();
		for(int i=0;i<list.size();i++){
			visibleList.add(list.get(i).getGroupID());
		}
	}
	
	class NetAsyncTask extends AsyncTask{
		
		public NetAsyncTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			System.out.println("在Pre里加载活动");
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			code = Integer.parseInt(result.toString());
			if(code==5)
				Toast.makeText(getActivity(),"刷新成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
				
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			System.out.println("doInBack");
			long userId = Long.valueOf(params[0].toString());;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getFriendCircle(userId);
		}

		public int getFriendCircle(long userId){
			
			HttpClient hc=new DefaultHttpClient();
			hc.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					8000); // 超时设置
			hc.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 8000);// 连接超时
			HttpGet hg=new HttpGet("http://10.103.243.101:8080/Crj/kao.txt");
			try {
				HttpResponse hr=hc.execute(hg);
				int code=hr.getStatusLine().getStatusCode();
				if(code==200){
					HttpEntity entity=hr.getEntity();
					InputStream is=entity.getContent();
					BufferedReader reader=new BufferedReader(new InputStreamReader(is));
					
					String line = reader.readLine();
					System.out.println(line+"这里是"+userId+"的所有朋友圈活动");
					
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				return 0;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
			
			return 5;
		}
	}

	
	private void getEventList() {
		
//		NetAsyncTask task = new NetAsyncTask();
//		task.execute(getUserID());
		eventList.clear();
		List<DBEvent> t1 = null;
		userID = getUserID();
		getVisible(userID);
		//eventList = new ArrayList<FCEntity>();
		QueryBuilder<DBEvent> qbe = DBHelper.getInstance(getActivity()).getEventDao().queryBuilder()
				.where(DBEventDao.Properties.Visibility.in(visibleList));
		t1 = qbe.list();
		for(DBEvent e: t1) {
			QueryBuilder<DBComment> qbc = DBHelper.getInstance(getActivity()).getCommentDao().queryBuilder()
					.where(Properties.EventID.eq(e.getId()));
			List<DBComment> t2 = qbc.list();
			for(DBComment c: t2) {
				FCEntity fe = new FCEntity(e, c);
				eventList.add(fe);
			}
		}
	}
	
	private void getEventList(int pos) {
		Long userID = getUserID();
		getVisible(getUserID());
		eventList.clear();
		//eventList = new ArrayList<FCEntity>();
		QueryBuilder<DBEvent> qbe = DBHelper.getInstance(getActivity()).getEventDao().queryBuilder()
				.where(DBEventDao.Properties.Visibility.in(visibleList));
		List<DBEvent> t1 = qbe.list();
		for(DBEvent e: t1) {
			QueryBuilder<DBComment> qbc = DBHelper.getInstance(getActivity()).getCommentDao().queryBuilder()
					.where(Properties.EventID.eq(e.getId()), Properties.CommentType.eq(pos));
			List<DBComment> t2 = qbc.list();
			for(DBComment c: t2) {
				FCEntity fe = new FCEntity(e, c);
				eventList.add(fe);
			}
		}
	}
	
	public void reloadData(int pos) {
		switch(pos) {
		case 0: {
			getEventList();
			FriendCircleAdapter fca = new FriendCircleAdapter(getActivity(), eventList);
			vMainList.setAdapter(fca);
			break;
		}
		case 1: {
			getEventList(1);
			FriendCircleAdapter fca = new FriendCircleAdapter(getActivity(), eventList);
			vMainList.setAdapter(fca);
			break;
		}
		case 2: {
			getEventList(3);
			FriendCircleAdapter fca = new FriendCircleAdapter(getActivity(), eventList);
			vMainList.setAdapter(fca);
			break;
		}
		case 3: {
			getEventList(4);
			FriendCircleAdapter fca = new FriendCircleAdapter(getActivity(), eventList);
			vMainList.setAdapter(fca);
			break;
		}
		}
	}
}
