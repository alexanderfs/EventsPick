package com.alexan.findevents.util;

import android.content.Context;

import com.alexan.findevents.FindEventsApp;
import com.alexan.findevents.dao.DBCategoryDao;
import com.alexan.findevents.dao.DBCategoryEventDao;
import com.alexan.findevents.dao.DBCityDao;
import com.alexan.findevents.dao.DBCommentDao;
import com.alexan.findevents.dao.DBDistrictDao;
import com.alexan.findevents.dao.DBEventCategoryDao;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBEventMessageDao;
import com.alexan.findevents.dao.DBFavCategoryDao;
import com.alexan.findevents.dao.DBFriendDao;
import com.alexan.findevents.dao.DBFriendEventDao;
import com.alexan.findevents.dao.DBFriendMessageDao;
import com.alexan.findevents.dao.DBGroupDao;
import com.alexan.findevents.dao.DBGroupFriendDao;
import com.alexan.findevents.dao.DBHotEventDao;
import com.alexan.findevents.dao.DBImageDao;
import com.alexan.findevents.dao.DBLocationDao;
import com.alexan.findevents.dao.DBPersonDao;
import com.alexan.findevents.dao.DBPickEventDao;
import com.alexan.findevents.dao.DBProvinceDao;
import com.alexan.findevents.dao.DBRTEventDao;
import com.alexan.findevents.dao.DBSearchResultEventDao;
import com.alexan.findevents.dao.DBTimeRecorderDao;
import com.alexan.findevents.dao.DBUserDao;
import com.alexan.findevents.dao.DaoSession;

public class DBHelper {
	private static Context mCtx;
	private static DBHelper instance;

	private   DBImageDao imageDao;
	private   DBUserDao userDao;
	private   DBPersonDao personDao;
	private   DBCategoryDao categoryDao;
	private   DBFavCategoryDao favCategoryDao;
	private   DBFriendDao friendDao;
	private	  DBGroupDao groupDao;
	private	  DBGroupFriendDao groupfriendDao;
	private   DBEventDao eventDao;
	private   DBPickEventDao pickEventDao;
	private   DBEventCategoryDao eventCategoryDao;
	private   DBLocationDao locationDao;
	private   DBProvinceDao provinceDao;
	private   DBCityDao cityDao;
	private   DBDistrictDao districtDao;
	private   DBCommentDao commentDao;
	private   DBHotEventDao hotEventDao;
	private   DBRTEventDao rTEventDao;
	private   DBCategoryEventDao categoryEventDao;
	private	  DBSearchResultEventDao searchResultEventDao;
	private   DBFriendEventDao friendEventDao;
	private   DBEventMessageDao eventMessageDao;
	private   DBFriendMessageDao friendMessageDao;
	private   DBTimeRecorderDao timeRecorderDao;

	private DBHelper() {

	}

	public static DBHelper getInstance(Context ctx) {
		if (instance == null) {
			instance = new DBHelper();
			if (mCtx == null) {
				mCtx = ctx;
			}
			DaoSession ds = FindEventsApp.getdaosSession(mCtx);
			instance.categoryDao = ds.getDBCategoryDao();
			instance.categoryEventDao = ds.getDBCategoryEventDao();
			instance.cityDao = ds.getDBCityDao();
			instance.commentDao = ds.getDBCommentDao();
			instance.districtDao = ds.getDBDistrictDao();
			instance.eventCategoryDao = ds.getDBEventCategoryDao();
			instance.eventDao = ds.getDBEventDao();
			instance.pickEventDao = ds.getDBPickEventDao();
			instance.eventMessageDao = ds.getDBEventMessageDao();
			instance.favCategoryDao = ds.getDBFavCategoryDao();
			instance.friendDao = ds.getDBFriendDao();
			instance.groupDao = ds.getDBGroupDao();
			instance.groupfriendDao = ds.getDBGroupFriendDao();
			instance.friendEventDao = ds.getDBFriendEventDao();
			instance.friendMessageDao = ds.getDBFriendMessageDao();
			instance.hotEventDao = ds.getDBHotEventDao();
			instance.locationDao = ds.getDBLocationDao();
			instance.imageDao = ds.getDBImageDao();
			instance.personDao = ds.getDBPersonDao();
			instance.provinceDao = ds.getDBProvinceDao();
			instance.rTEventDao = ds.getDBRTEventDao();
			instance.searchResultEventDao = ds.getDBSearchResultEventDao();
			instance.timeRecorderDao = ds.getDBTimeRecorderDao();
			instance.userDao = ds.getDBUserDao();
		}
		return instance;
	}
	
	public DBPickEventDao getPickEventDao(){
		return pickEventDao;
	}
	
	public void setPickEventDao(){
		this.pickEventDao = pickEventDao;
	}

	public DBLocationDao getLocationDao() {
		return locationDao;
	}

	public void setLocationDao(DBLocationDao locationDao) {
		this.locationDao = locationDao;
	}

	public DBUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(DBUserDao userDao) {
		this.userDao = userDao;
	}

	public DBImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(DBImageDao imageDao) {
		this.imageDao = imageDao;
	}

	public DBPersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(DBPersonDao personDao) {
		this.personDao = personDao;
	}

	public DBCategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(DBCategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public DBFavCategoryDao getFavCategoryDao() {
		return favCategoryDao;
	}

	public void setFavCategoryDao(DBFavCategoryDao favCategoryDao) {
		this.favCategoryDao = favCategoryDao;
	}

	public DBFriendDao getFriendDao() {
		return friendDao;
	}

	public void setFriendDao(DBFriendDao friendDao) {
		this.friendDao = friendDao;
	}
	
	public DBGroupDao getGroupDao() {
		return groupDao;
	}
	
	public void setGroupDao(DBGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public DBGroupFriendDao getGroupFriendDao() {
		return groupfriendDao;
	}
	
	public void setGroupFriendDao(DBGroupFriendDao groupfriendDao) {
		this.groupfriendDao = groupfriendDao;
	}

	public DBEventDao getEventDao() {
		return eventDao;
	}

	public void setEventDao(DBEventDao eventDao) {
		this.eventDao = eventDao;
	}

	public DBEventCategoryDao getEventCategoryDao() {
		return eventCategoryDao;
	}

	public void setEventCategoryDao(DBEventCategoryDao eventCategoryDao) {
		this.eventCategoryDao = eventCategoryDao;
	}


	public DBProvinceDao getProvinceDao() {
		return provinceDao;
	}

	public void setProvinceDao(DBProvinceDao provinceDao) {
		this.provinceDao = provinceDao;
	}

	public DBCityDao getCityDao() {
		return cityDao;
	}

	public void setCityDao(DBCityDao cityDao) {
		this.cityDao = cityDao;
	}

	public DBDistrictDao getDistrictDao() {
		return districtDao;
	}

	public void setDistrictDao(DBDistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	public DBCommentDao getCommentDao() {
		return commentDao;
	}

	public void setCommentDao(DBCommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public DBHotEventDao getHotEventDao() {
		return hotEventDao;
	}

	public void setHotEventDao(DBHotEventDao hotEventDao) {
		this.hotEventDao = hotEventDao;
	}

	public DBRTEventDao getrTEventDao() {
		return rTEventDao;
	}

	public void setrTEventDao(DBRTEventDao rTEventDao) {
		this.rTEventDao = rTEventDao;
	}

	public DBCategoryEventDao getCategoryEventDao() {
		return categoryEventDao;
	}

	public void setCategoryEventDao(DBCategoryEventDao categoryEventDao) {
		this.categoryEventDao = categoryEventDao;
	}

	public DBSearchResultEventDao getSearchResultEventDao() {
		return searchResultEventDao;
	}

	public void setSearchResultEventDao(DBSearchResultEventDao searchResultEventDao) {
		this.searchResultEventDao = searchResultEventDao;
	}

	public DBFriendEventDao getFriendEventDao() {
		return friendEventDao;
	}

	public void setFriendEventDao(DBFriendEventDao friendEventDao) {
		this.friendEventDao = friendEventDao;
	}

	public DBEventMessageDao getEventMessageDao() {
		return eventMessageDao;
	}

	public void setEventMessageDao(DBEventMessageDao eventMessageDao) {
		this.eventMessageDao = eventMessageDao;
	}

	public DBFriendMessageDao getFriendMessageDao() {
		return friendMessageDao;
	}

	public void setFriendMessageDao(DBFriendMessageDao friendMessageDao) {
		this.friendMessageDao = friendMessageDao;
	}

	public DBTimeRecorderDao getTimeRecorderDao() {
		return timeRecorderDao;
	}

	public void setTimeRecorderDao(DBTimeRecorderDao timeRecorderDao) {
		this.timeRecorderDao = timeRecorderDao;
	}

}
