package com.alexan.findevents;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.util.ImageUtil;
import com.alexan.findevents.util.StringFormatUtil;

public class PickListAdapter extends BaseAdapter implements OnScrollListener{

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
	private Set<BitmapWorkerTask> taskCollection;
	private List<DBPickEvent> eventList;
	private DBPickEvent currEvent;
	private LruCache<String, Bitmap> imgList;
	private Bitmap bitmap = null;
	private ListView lv = null;
	
	public PickListAdapter(Activity ctx, List<DBPickEvent> eventList, ListView lv) {
		mCtx = ctx;
		this.eventList = eventList;
		this.lv = lv;
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
	    // 使用最大可用内存值的1/32作为缓存的大小。  
	    int cacheSize = maxMemory / 32;
	    taskCollection = new HashSet<BitmapWorkerTask>();
	    imgList = new LruCache<String, Bitmap>(cacheSize){
			@SuppressLint("NewApi") @Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};  
	    
	    lv.setOnScrollListener(this);
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
				vh.img.setTag(purl);
				setImageView(purl, vh.img);
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
	
	private void setImageView(String imageUrl, ImageView imageView) {
		Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.touxiang);
		}
	}
	
	public Bitmap getBitmapFromMemoryCache(String key) {
		return imgList.get(key);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		// TODO Auto-generated method stub
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		// 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
		// 因此在这里为首次进入程序开启下载任务。
		if (isFirstEnter && visibleItemCount > 0) {
			loadBitmaps(firstVisibleItem, visibleItemCount);
			isFirstEnter = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
		} else {
			cancelAllTasks();
		}
	}
	
	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				String imageUrl = currEvent.getPhoto();
				Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
				if (bitmap == null) {
					BitmapWorkerTask task = new BitmapWorkerTask();
					taskCollection.add(task);
					task.execute(imageUrl);
				} else {
					ImageView imageView = (ImageView) lv.findViewWithTag(imageUrl);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancelAllTasks() {
		if (taskCollection != null) {
			for (BitmapWorkerTask task : taskCollection) {
				task.cancel(false);
			}
		}
	}
	
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String imageUrl;

		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			// 在后台开始下载图片
			Bitmap bitmap = downloadBitmap(params[0]);
			if (bitmap != null) {
				// 图片下载完成后缓存到LrcCache中
				addBitmapToMemoryCache(params[0], bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
			ImageView imageView = (ImageView) lv.findViewWithTag(imageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			taskCollection.remove(this);
		}

		/**
		 * 建立HTTP请求，并获取Bitmap对象。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 解析后的Bitmap对象
		 */
		private Bitmap downloadBitmap(String imageUrl) {
			Bitmap bitmap = null;
			HttpURLConnection con = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(30000);
				con.setReadTimeout(30000);
				con.setDoInput(true);
				con.setDoOutput(true);
				bitmap = BitmapFactory.decodeStream(con.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return bitmap;
		}

	}

}
