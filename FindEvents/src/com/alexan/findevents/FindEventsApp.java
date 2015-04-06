package com.alexan.findevents;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.database.SQLException;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.alexan.findevents.dao.DaoMaster;
import com.alexan.findevents.dao.DaoSession;
import com.alexan.findevents.util.DBCopyOpenHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class FindEventsApp extends Application {
	private static final String TAG = "FindSthApp";
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}*/

		super.onCreate();

		initImageLoader(getApplicationContext());
	}
	
	public static DaoMaster getDaoMaster(Context ctx) {
		if(daoMaster == null) {
			DBCopyOpenHelper oh = new DBCopyOpenHelper(ctx, AppConstant.DATABASE_NAME, null);
			try 
	        {
	            oh.createDataBase();
	        } 
	        catch (IOException mIOException) 
	        {
	            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
	            throw new Error("UnableToCreateDatabase");
	        }
			try 
	        {
	            oh.openDataBase();
	            oh.close();
	        } 
	        catch (SQLException mSQLException) 
	        {
	            Log.e(TAG, "open >>"+ mSQLException.toString());
	            throw mSQLException;
	        }
			daoMaster = new DaoMaster(oh.getWritableDatabase());
		}
		return daoMaster;
	}
	
	public static DaoSession getdaosSession(Context ctx) {
		if(daoSession == null) {
			if(daoMaster == null) {
				daoMaster = getDaoMaster(ctx);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
