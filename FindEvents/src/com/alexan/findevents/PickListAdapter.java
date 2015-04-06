package com.alexan.findevents;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.util.ImageUtil;
import com.alexan.findevents.util.StringFormatUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PickListAdapter extends BaseAdapter{

	private int mFirstVisibleItem;

	/**
	 * 一屏有多少张图片可见
	 */
	private int mVisibleItemCount;

	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 */
	private boolean isFirstEnter = true;
	
	private Activity mCtx;
	private List<DBPickEvent> eventList;
	private DBPickEvent currEvent;
	private LruCache<String, Bitmap> imgList;
	private Bitmap bitmap = null;
	private ListView lv = null;
	
	public PickListAdapter(Activity ctx, List<DBPickEvent> eventList, ListView lv) {
		mCtx = ctx;
		this.eventList = eventList;
		this.lv = lv;
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
	    if (getBitmapFromMemCache(key) == null) {  
	        imgList.put(key, bitmap);  
	    }  
	}  
	  
	public Bitmap getBitmapFromMemCache(String key) {  
	    return imgList.get(key);  
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if(convertView == null) {
			convertView = mCtx.getLayoutInflater().inflate(R.layout.list_hotevent, null);
			vh = new ViewHolder();
			vh.img = (ImageView) convertView.findViewById(R.id.list_hotevent_img);
			vh.title = (TextView) convertView.findViewById(R.id.list_hotevent_title);
			vh.time = (TextView) convertView.findViewById(R.id.list_hotevent_time);
			vh.addr = (TextView) convertView.findViewById(R.id.list_hotevent_addr);
			vh.others = (TextView) convertView.findViewById(R.id.list_hotevent_others);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		currEvent = eventList.get(position);
		if(currEvent.getId() != null) {
			String purl = currEvent.getPhoto();
			if(purl!=null){
				DisplayImageOptions options = new DisplayImageOptions.Builder()   
                .cacheInMemory(true)  
                .cacheOnDisc(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
          
				ImageLoader.getInstance().displayImage(purl, vh.img, options);
			}
			else
				vh.img.setImageResource(R.drawable.touxiang);
		}
		
		String tmp = currEvent.getTitle();
		vh.title.setText(tmp == null ? "DEFAULT TITLE" : tmp);
		String date = currEvent.getStartd()+"-"+currEvent.getEndd();
		String time = currEvent.getStartt()+"-"+currEvent.getEndt();
		
		vh.time.setText(date+", "+time);
		vh.addr.setText(StringFormatUtil.buildPickAddrString(eventList.get(position)));
		vh.others.setText(ImageUtil.getEventOtherDetail(eventList.get(position), mCtx));
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView img;
		TextView title;
		TextView time;
		TextView addr;
		TextView others;
	}

}
