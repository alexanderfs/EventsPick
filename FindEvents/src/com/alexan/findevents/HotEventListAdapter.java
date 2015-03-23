package com.alexan.findevents;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBImage;
import com.alexan.findevents.dao.DBImageDao;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.DensityUtil;
import com.alexan.findevents.util.ImageUtil;
import com.alexan.findevents.util.StringFormatUtil;
import com.alexan.findevents.util.TimeUtil;

import de.greenrobot.dao.query.QueryBuilder;

public class HotEventListAdapter extends BaseAdapter {
	
	private Activity mCtx;
	private List<DBEvent> eventList;
	private LruCache<String, Bitmap> imgList;
	
	public HotEventListAdapter(Activity ctx, List<DBEvent> eventList) {
		mCtx = ctx;
		this.eventList = eventList;
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
	    // 使用最大可用内存值的1/32作为缓存的大小。  
	    int cacheSize = maxMemory / 32;  
	    imgList = new LruCache<String, Bitmap>(cacheSize);  
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
		
		DBEvent currEvent = eventList.get(position);
		if(currEvent.getId() != null) {
			QueryBuilder<DBImage> qbimg = DBHelper.getInstance(mCtx).getImageDao()
					.queryBuilder().where(DBImageDao.Properties.EventID.eq(currEvent.getId()));
			List<DBImage> dbimgList = qbimg.list();
			if(dbimgList.size() > 0) {
				DBImage currImg = dbimgList.get(0);
				Bitmap bm;
				if((bm = getBitmapFromMemCache(currImg.getImageUrl())) == null) {
					bm = ImageUtil.decodeSampledBitmapFromPath(currImg.getImageUrl(), DensityUtil.dip2px(mCtx, 96f), 
							DensityUtil.dip2px(mCtx, 96f));
					addBitmapToMemoryCache(currImg.getImageUrl(), bm);
				}
				vh.img.setImageBitmap(bm);
			}
		}
		
		String tmp = eventList.get(position).getTitle();
		vh.title.setText(tmp == null ? "DEFAULT TITLE" : tmp);
		
		vh.time.setText(TimeUtil.getDateSpanString(eventList.get(position).getStarttime(), 
				eventList.get(position).getEndtime()));
		vh.addr.setText(StringFormatUtil.buildAddrString(eventList.get(position)));
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
