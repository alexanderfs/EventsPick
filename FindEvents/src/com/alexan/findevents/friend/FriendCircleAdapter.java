package com.alexan.findevents.friend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBImage;
import com.alexan.findevents.dao.DBImageDao;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.event.EventDetailActivity;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.DensityUtil;
import com.alexan.findevents.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.dao.query.QueryBuilder;

public class FriendCircleAdapter extends BaseAdapter {

	private Activity mCtx;
	private List<FCEntity> eventList;
	private LruCache<String, Bitmap> imgList;
	
	public FriendCircleAdapter(Activity ctx, List<FCEntity> eventList) {
		this.mCtx = ctx;
		if(eventList == null) {
			eventList = new ArrayList<FCEntity>();
		}
		this.eventList = eventList;
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
	    // 使用最大可用内存值的1/32作为缓存的大小。  
	    int cacheSize = maxMemory / 32;  
	    imgList = new LruCache<String, Bitmap>(cacheSize);  
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if(convertView == null) {
			convertView = mCtx.getLayoutInflater().inflate(R.layout.list_friendcircle_item, null);
			vh = new ViewHolder();
			vh.image = (ImageView) convertView.findViewById(R.id.list_fc_item_image);
			vh.title = (TextView) convertView.findViewById(R.id.list_fc_item_title);
			vh.comment = (TextView) convertView.findViewById(R.id.list_fc_item_comment);
			vh.event = (LinearLayout) convertView.findViewById(R.id.list_fc_item_event);
			vh.eventImage = (ImageView) convertView.findViewById(R.id.list_fc_item_eventimage);
			vh.eventDesc = (TextView) convertView.findViewById(R.id.list_fc_item_eventdesc);
			vh.time = (TextView) convertView.findViewById(R.id.list_fc_item_time);
			//vh.operation = (Button) convertView.findViewById(R.id.list_fc_item_operation);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		DBPickEvent currEvent = eventList.get(position).getEvent();
		String purl = currEvent.getPhoto();
		if(purl!=null){
			DisplayImageOptions options = new DisplayImageOptions.Builder()   
            .cacheInMemory(true)  
            .cacheOnDisc(true)  
            .bitmapConfig(Bitmap.Config.RGB_565)  
            .build();  
      
			ImageLoader.getInstance().displayImage(purl, vh.eventImage, options);
		}
		else
			vh.eventImage.setImageResource(R.drawable.touxiang);
		vh.image.setImageResource(R.drawable.touxiang);
		
		vh.title.setText(eventList.get(position).getEvent().getTitle());
		//vh.comment.setText(eventList.get(position).getComment().getComentContent());
		vh.comment.setText("你收藏了？");
		vh.eventDesc.setText(eventList.get(position).getEvent().getDescription());
		
		//Date date = new Date(eventList.get(position).getComment().getTimestamp());
		Date date = new Date();
		
		vh.time.setText(new SimpleDateFormat("yyyy年MM月DD日 HH:mm", Locale.CHINA).format(date));
		vh.event.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putLong("event_id", eventList.get(position).getEvent().getId());
				Intent i = new Intent(mCtx, EventDetailActivity.class);
				i.putExtras(b);
				mCtx.startActivity(i);
			}
		});
		return convertView;
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
	    if (getBitmapFromMemCache(key) == null) {  
	        imgList.put(key, bitmap);  
	    }  
	}  
	  
	public Bitmap getBitmapFromMemCache(String key) {  
	    return imgList.get(key);  
	}

	private static class ViewHolder {
		ImageView image;
		TextView title;
		TextView comment;
		LinearLayout event;
		ImageView eventImage;
		TextView eventDesc;
		TextView time;
		//Button operation;
	}
}
