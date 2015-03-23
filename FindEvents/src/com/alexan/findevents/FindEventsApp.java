package com.alexan.findevents;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.alexan.findevents.dao.DaoMaster;
import com.alexan.findevents.dao.DaoSession;
import com.alexan.findevents.util.DBCopyOpenHelper;


public class FindEventsApp extends Application {
	private static final String TAG = "FindSthApp";
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	
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
}
