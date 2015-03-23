package com.alexan.findevents.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alexan.findevents.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class ShowAddrActivity extends Activity implements OnGetGeoCoderResultListener {
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	BaiduMap mBaiduMap = null;
	
	MapView mMapView = null;
	EditText editGeoCodeKey = null;
	EditText editCity = null;
	
	String details = null;
	String citys = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext()); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showaddr);
		
		String addr = getIntent().getStringExtra("addr");
		
		String citys = addr.split("\\(")[1].split(",")[0];
		String details = addr.split(",")[2].split("\\)")[0];
		
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mSearch.geocode(new GeoCodeOption().city(
				citys).address(details));
	}
	
	@Override
	protected void onPause() {
	mMapView.onPause();
	super.onPause();
	}
	
	@Override
	protected void onResume() {
	mMapView.onResume();
	super.onResume();
	}
	
	@Override
	protected void onDestroy() {
	mMapView.onDestroy();
	mSearch.destroy();
	super.onDestroy();
	}
	
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(ShowAddrActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		
	//	mBaiduMap.setMaxAndMinZoomLevel(13, 15);
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(ShowAddrActivity.this, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

}