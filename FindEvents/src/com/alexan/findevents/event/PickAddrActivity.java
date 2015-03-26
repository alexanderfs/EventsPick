package com.alexan.findevents.event;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.AppConstant;
import com.alexan.findevents.R;
import com.alexan.findevents.crj.JSONparse;
import com.alexan.findevents.crj.Util;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.util.DBHelper;

public class PickAddrActivity extends SherlockActivity {

	private String addurl = "http://123.57.45.183/venue/CreateVenue";//post
	private String geturl = "http://123.57.45.183/venue/SearchVenues";//get
	String[] provinces, citys;
	ArrayAdapter<String> cityadapter;
	String countryId="1008", provinceId="1003", cityId="1004", venueId, pro, city;
	long venueid;//插入数据库的venueid
	boolean ispublic = true;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Toast.makeText(PickAddrActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
			vList.setAdapter(new LocationAdapter());
			
			super.handleMessage(msg);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.menu_add, menu);
		provinces = getResources().getStringArray(R.array.province);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
		case R.id.action_add: {
			View editView = getLayoutInflater().inflate(R.layout.dialog_addlocation, null);
			
			final EditText vAddr = (EditText) editView.findViewById(R.id.dlg_addloc_addr);
			final EditText vAddrDetail = (EditText) editView.findViewById(R.id.dlg_addloc_addrdetail);
/*			final EditText vCity = (EditText) editView.findViewById(R.id.dlg_addloc_addrcity);
			final EditText vDistrict = (EditText) editView.findViewById(R.id.dlg_addloc_addrdistrict);*/
			final Spinner aProvince = (Spinner)editView.findViewById(R.id.add_province);
			final Spinner aCity = (Spinner)editView.findViewById(R.id.add_city);
			aProvince.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parent, View view, 
			            int pos, long id) {
			    	//给city配adapter
			    	switch (pos){
			    	case 1:
			    		citys = getResources().getStringArray(R.array.city2);
			    		break;
			    	default:
			    		citys = getResources().getStringArray(R.array.city1);
			    		break;
			    	}	        
			        cityadapter = new ArrayAdapter<String>(PickAddrActivity.this, android.R.layout.simple_spinner_item, citys);
			        aCity.setAdapter(cityadapter);
			        //获取province的id
			        pro = provinces[pos];
			        provinceId = getProvinceId(pro);
			    }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub			
				}
			});
			
			aCity.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					city = citys[arg2];				
					cityId = getCityId(city);	
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			AlertDialog.Builder editDialogbuilder = new AlertDialog.Builder(this);
			editDialogbuilder.setTitle("请完整填写地址信息，否则无法保存");
			editDialogbuilder.setView(editView);
			editDialogbuilder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(vAddr.getText().length() == 0 || vAddrDetail.getText().length() == 0 /*||
							vCity.getText().length() == 0 || vDistrict.getText().length() == 0*/) {
						Toast.makeText(PickAddrActivity.this, "保存失败，请填写完整的地址", Toast.LENGTH_SHORT).show();
						return;
					}

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String params = "VenueCountryId="+countryId+"&VenueProvinceId="+provinceId+
									"&VenueCityId="+cityId+"&VenueName="+vAddr.getText().toString()+"&VenueAddress="+vAddrDetail.getText().toString()
									+"&IsPublic="+ispublic;
							String result = Util.httpPost(addurl, params);
							/*Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("params", result);
							message.setData(bundle);
							handler.sendMessage(message);*/
							
							try {
								JSONObject obresult = new JSONObject(result);
								String code = obresult.getString("code");
								if(!code.equals("10000")){
									Toast.makeText(PickAddrActivity.this, "保存失败，请填写完整的地址", Toast.LENGTH_SHORT).show();
								}
								else{
									JSONObject jo = obresult.getJSONObject("data");
									venueid = Long.parseLong(jo.getString("VenueId"));
									DBLocation ndbl = new DBLocation();
									ndbl.setId(venueid);
									ndbl.setAddrName(vAddr.getText().toString());
									ndbl.setAddrDetail(vAddrDetail.getText().toString());
									ndbl.setAddrProvince(pro);
									ndbl.setAddrCity(city);
									ndbl.setTimestamp(System.currentTimeMillis());
									DBHelper.getInstance(PickAddrActivity.this).getLocationDao().insert(ndbl);	
								/*	Toast.makeText(PickAddrActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();*/
									handler.sendEmptyMessage(0);
								}	
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
													
						}
					}).start();
								
					
				}
			});
			editDialogbuilder.setNegativeButton("取消", null);
			editDialogbuilder.create().show();
		}
		}
		return super.onOptionsItemSelected(item);
	}

	
	protected String getCityId(String city) {
		// TODO Auto-generated method stub
		return AppConstant.CITY_KV.get(city);
	}

	protected String getProvinceId(String pro) {
		// TODO Auto-generated method stub
		return AppConstant.PROVINCE_KV.get(pro);
	
	}

	private ListView vList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickaddr);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//init data,
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//从服务器获得数据。
				JSONparse.refreshFrom(geturl,  PickAddrActivity.this,  handler);
			}			
		}.start();
		
		vList = (ListView) findViewById(R.id.act_pickaddr_list);
		vList.setAdapter(new LocationAdapter());
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DBLocation dbl = locationList.get(position);
				Intent i = new Intent();
				i.putExtra("location_id", dbl.getId());
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
		
	}
	
	private List<DBLocation> locationList;
	
	private class LocationAdapter extends BaseAdapter {
		
		public LocationAdapter() {
			locationList = DBHelper.getInstance(PickAddrActivity.this).getLocationDao().loadAll();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return locationList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return locationList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vh;
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(android.R.layout.two_line_list_item, null);
				vh = new ViewHolder();
				vh.tv1 = (TextView) convertView.findViewById(android.R.id.text1);
				vh.tv2 = (TextView) convertView.findViewById(android.R.id.text2);
				convertView.setTag(vh);
			} 
			
			vh = (ViewHolder) convertView.getTag();
			vh.tv1.setText(locationList.get(position).getAddrName());
			vh.tv2.setText(locationList.get(position).getAddrDetail());
			return convertView;
		}
		
		private class ViewHolder {
			TextView tv1;
			TextView tv2;
		}
		
	}

}
