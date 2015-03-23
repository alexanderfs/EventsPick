package com.alexan.findevents.event;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.util.DBHelper;

public class PickAddrActivity extends SherlockActivity {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.menu_add, menu);
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
			final EditText vCity = (EditText) editView.findViewById(R.id.dlg_addloc_addrcity);
			final EditText vDistrict = (EditText) editView.findViewById(R.id.dlg_addloc_addrdistrict);
			AlertDialog.Builder editDialogbuilder = new AlertDialog.Builder(this);
			editDialogbuilder.setTitle("请完整填写地址信息，否则无法保存");
			editDialogbuilder.setView(editView);
			editDialogbuilder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(vAddr.getText().length() == 0 || vAddrDetail.getText().length() == 0 ||
							vCity.getText().length() == 0 || vDistrict.getText().length() == 0) {
						Toast.makeText(PickAddrActivity.this, "保存失败，请填写完整的地址", Toast.LENGTH_SHORT).show();
						return;
					}
					DBLocation ndbl = new DBLocation();
					ndbl.setAddrName(vAddr.getText().toString());
					ndbl.setAddrDetail(vAddrDetail.getText().toString());
					ndbl.setAddrCity(vCity.getText().toString());
					ndbl.setAddrDistrict(vDistrict.getText().toString());
					ndbl.setTimestamp(System.currentTimeMillis());
					DBHelper.getInstance(PickAddrActivity.this).getLocationDao().insert(ndbl);
					vList.setAdapter(new LocationAdapter());
				}
			});
			editDialogbuilder.setNegativeButton("取消", null);
			editDialogbuilder.create().show();
		}
		}
		return super.onOptionsItemSelected(item);
	}

	
	private ListView vList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickaddr);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
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
