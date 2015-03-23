package com.alexan.findevents.event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBComment;

public class CommentsAdapter extends BaseAdapter {

	private Activity mCtx;
	private List<DBComment> commList;
	public CommentsAdapter(Activity ctx, List<DBComment> commList) {
		if(commList == null) {
			commList = inflateData(commList);
		}
		this.mCtx = ctx;
		this.commList = commList;
	}
	
	private List<DBComment> inflateData(List<DBComment> commList) {
		commList = new ArrayList<DBComment>();
		for(int i = 0; i < 3; i++) {
			DBComment comm = new DBComment();
			comm.setUsername("小苹果");
			comm.setTimestamp(System.currentTimeMillis());
			comm.setComentContent("我今天吃了一个小苹果感觉自己萌萌哒！");
			commList.add(comm);
		}
		return commList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commList == null ? 0 : commList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		final int pos = position;
		if(convertView == null) {
			convertView = mCtx.getLayoutInflater().inflate(R.layout.list_comment_item, null);
			vh = new ViewHolder();
			vh.icon = (ImageView) convertView.findViewById(R.id.list_comment_icon);
			vh.name = (TextView) convertView.findViewById(R.id.list_comment_name);
			vh.time = (TextView) convertView.findViewById(R.id.list_comment_time);
			vh.content = (TextView) convertView.findViewById(R.id.list_comment_content);
			vh.comment = (Button) convertView.findViewById(R.id.list_comment_comment);
			vh.share = (Button) convertView.findViewById(R.id.list_comment_share);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		//vh.icon.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.u91));
		vh.name.setText(commList.get(position).getUsername());
		vh.time.setText(new SimpleDateFormat("yyyy-MM-DD HH:mm", Locale.CHINA).format(commList.get(position).getTimestamp()));
		vh.content.setText(commList.get(position).getComentContent());
		vh.comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(mCtx, "" + pos, Toast.LENGTH_SHORT).show();
			}
		});
		vh.share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(mCtx, "2: " + pos, Toast.LENGTH_SHORT).show();	
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView time;
		TextView content;
		Button comment;
		Button share;
	}
}
