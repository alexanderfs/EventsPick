package com.alexan.findevents;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.alexan.findevents.dao.DBCategoryDao;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventCategory;
import com.alexan.findevents.dao.DBHotSpotDao;
import com.alexan.findevents.dao.DBImage;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.dao.DBLocationDao;
import com.alexan.findevents.dao.DBPersonDao;
import com.alexan.findevents.util.DBCopyOpenHelper;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.ImageUtil;

public class LaunchActivity extends Activity {

	long adminID;
	private MainHandler mh;
	private List<DBImage> bdPhotos = new ArrayList<DBImage>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setBackgroundDrawableResource(R.drawable.ic_welcome);
		isFirst = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.getBoolean("is_first", true);
		mh = new MainHandler(this);
		DBHelper.getInstance(this);
		if(isFirst) {
			initStorageDir();
			initData();
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
		    int month = c.get(Calendar.MONTH);
		    int dayofmonth = c.get(Calendar.DAY_OF_MONTH);
		    
		    
		    
			for(int k=1;k<=20;k++){
				DBEvent event = new DBEvent();
				event.setDescription(getResources().getString(R.string.event_desc));
				event.setTitle("管理员事件"+k);
				event.setUserID(adminID);
				event.setLocationID((long) (k%3+1));
				DBLocation dblo = DBHelper.getInstance(this).getLocationDao().load((long) (k%3+1));
				event.setAddress(dblo.getAddrName());
				event.setAddressdetail(dblo.getAddrDetail());
				event.setCity(dblo.getAddrCity());
				event.setDistrict(dblo.getAddrDistrict());
				event.setStarttime(new GregorianCalendar(year, month, k, 9, 30).getTime().getTime());
		        event.setEndtime(new GregorianCalendar(year, month, k+2, 17, 20).getTime().getTime());
		        event.setTimestamp(System.currentTimeMillis());
		        long eventID = DBHelper.getInstance(this).getEventDao().insert(event);
		        int[] sequence = new int[9];
	            int[] output = new int[9];
	 
	            for (int i = 0; i < 9; i++)
	            {
	                sequence[i] = i+2;
	            }
	 
	            Random random = new Random();
	            int count = random.nextInt(3);
	            int end = 9 - 1;
	 
	            for (int i = 0; i <= count; i++)
	            {
	                int num = random.nextInt(end + 1);
	                output[i] = sequence[num];
	                sequence[num] = sequence[end];
	                end--;
	            }
	            
	            for (int i = 0; i <= count; i++)
	            {
	            	DBEventCategory ec = new DBEventCategory();
		            ec.setEventID(eventID);
		            ec.setCategoryID((long) output[i]);
		            ec.setTimestamp(Calendar.getInstance().getTimeInMillis());
		            DBHelper.getInstance(this).getEventCategoryDao().insert(ec);
	            }
	            
	            Bitmap[] bgImg = new Bitmap[3];
	            bgImg[0] = getImageFromAssetFile( "37.jpg" );    
	            bgImg[1] = getImageFromAssetFile( "73.jpg" ); 
	            bgImg[2] = getImageFromAssetFile( "72.jpg" ); 
	  
	            for(int i=0;i<3;i++){
	            	StringBuilder sb = new StringBuilder();
	                String dirName, fileName;
	                dirName = Environment.getExternalStorageDirectory() + AppConstant.folderName;
	                fileName = System.currentTimeMillis() + ".jpg";
	                sb.append(dirName + "/" + fileName);
	                File dir=new File(dirName); 
	                if(!dir.exists()) dir.mkdirs();
	                try {
	                	FileOutputStream out = new FileOutputStream(new File(dir, fileName));  
	                    bgImg[i].compress(Bitmap.CompressFormat.JPEG, 100, out);  
	                    out.flush();  
	                    out.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
	                DBImage imgTemp = new DBImage();
		            imgTemp.setImageUrl(sb.toString());
		            imgTemp.setTimestamp(System.currentTimeMillis());
		            imgTemp.setEventID(eventID);
	                DBHelper.getInstance(this).getImageDao().insert(imgTemp);
	            }
           
			}
		}
		mh.sendEmptyMessageDelayed(0, 2000);
	}
	
	

	private Bitmap getImageFromAssetFile(String string) {
		// TODO Auto-generated method stub
		 Bitmap image = null;    
         AssetManager am = getResources().getAssets();    
         try    
         {    
             InputStream is = am.open(string);    
             image = BitmapFactory.decodeStream(is);    
             is.close();    
         }    
         catch (IOException e)    
         {    
             e.printStackTrace();    
         }     
         return image;   
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        this.setResult(RESULT_CANCELED);
	        this.finish();
	        return true;   
	    }
		return super.onKeyUp(keyCode, event);
	}
	
	private void initData() {
		DBCopyOpenHelper oh = new DBCopyOpenHelper(this, AppConstant.DATABASE_NAME, null);
		SQLiteDatabase sdb = oh.getWritableDatabase();
		sdb.beginTransaction();
		try {
			String[] categoryList = getResources().getStringArray(R.array.category);
			for(String ca: categoryList) {
				ContentValues cv = new ContentValues();
				cv.put("name", ca);
				cv.put("timestamp", System.currentTimeMillis());
				sdb.insert(DBCategoryDao.TABLENAME, null, cv);
			}
			
			String[] locationList = getResources().getStringArray(R.array.location_name);
			String[] loDescList = getResources().getStringArray(R.array.location_desc);
			String[] loCityList = getResources().getStringArray(R.array.location_city);
			String[] loDistrictList = getResources().getStringArray(R.array.location_district);
			for(int i = 0; i < locationList.length; i++) {
				ContentValues cv = new ContentValues();
				cv.put(DBLocationDao.Properties.AddrName.columnName, locationList[i]);
				cv.put(DBLocationDao.Properties.AddrDetail.columnName, loDescList[i]);
				cv.put(DBLocationDao.Properties.AddrCity.columnName, loCityList[i]);
				cv.put(DBLocationDao.Properties.AddrDistrict.columnName, loDistrictList[i]);
				cv.put(DBLocationDao.Properties.Timestamp.columnName, System.currentTimeMillis());
				sdb.insert(DBLocationDao.TABLENAME, null, cv);
			}
					
			String[] data = getResources().getStringArray(R.array.names);
			for(String s : data){
				ContentValues cv = new ContentValues();
				cv.put(DBPersonDao.Properties.Nickname.columnName, s);
				cv.put(DBPersonDao.Properties.Timestamp.columnName,System.currentTimeMillis());
				sdb.insert(DBPersonDao.TABLENAME, null, cv);
			}
			
			ContentValues cv = new ContentValues();
			cv.put(DBPersonDao.Properties.Nickname.columnName, "管理员");
			cv.put(DBPersonDao.Properties.Timestamp.columnName,System.currentTimeMillis());
			adminID = sdb.insert(DBPersonDao.TABLENAME, null, cv);
			
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
			.edit().putLong("admin_id", adminID).commit();
			sdb.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "数据库初始化异常", Toast.LENGTH_SHORT).show();
		} finally {
			sdb.endTransaction();
		}
		sdb.close();
	}
	
	private void initStorageDir() {
		boolean mkdirStatus = true;
		if(ImageUtil.getSDCardStatus()) {
			File appDir = new File(Environment.getExternalStorageDirectory() + 
					AppConstant.folderName);
			if(appDir.exists() == false) {
				if(appDir.mkdir()) {
					AppConstant.localStorage = Environment.getExternalStorageDirectory().getPath();
				} else {
					appDir = new File(Environment.getDataDirectory() + 
							AppConstant.folderName);
					if(appDir.exists() == false) {
						if(appDir.mkdir()) {
							AppConstant.localStorage = Environment.getDataDirectory().getPath();
						} else {
							mkdirStatus = false;
						}
					} else {
						AppConstant.localStorage = Environment.getDataDirectory().getPath();
					}
				}
			} else {
				AppConstant.localStorage = Environment.getExternalStorageDirectory().getPath();
			}
		} else {
			File appDir = new File(Environment.getDataDirectory() + 
					AppConstant.folderName);
			if(appDir.exists() == false) {
				if(appDir.mkdir()) {
					AppConstant.localStorage = Environment.getDataDirectory().getPath();
				} else {
					mkdirStatus = false;
				}
			} else {
				AppConstant.localStorage = Environment.getDataDirectory().getPath();
			}
		}
		if(mkdirStatus == false) {
			Toast.makeText(this, "本地文件夹初始化失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	private static class MainHandler extends Handler {
		private WeakReference<Activity> mCtx;
		
		public MainHandler(Activity ctx) {
			mCtx = new WeakReference<Activity>(ctx);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Activity act = mCtx.get();
			if(act == null) {
				return;
			}
			switch(msg.what) {
			case 0: {
				((LaunchActivity)act).startApp();
				break;
			}
			}
		}
	}
	
	private boolean isFirst;
	
	private void startApp() {
		
		if(isFirst) {
			Intent i = new Intent();
			i.setClass(LaunchActivity.this, WelcomeActivity.class);
			startActivity(i);
		} else {
			Intent i = new Intent(LaunchActivity.this, FrameworkActivity.class);
			startActivity(i);
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
	}

}
