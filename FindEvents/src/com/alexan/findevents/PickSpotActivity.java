package com.alexan.findevents;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.actionbarsherlock.app.SherlockActivity;

public class PickSpotActivity extends SherlockActivity implements SearchView.OnQueryTextListener{
	
	private ListView listView;
	private SearchView searchView;
	private Object[] names;   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickspot);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initActionbar(); 
        names = FrameworkActivity.hotList.toArray(); 
        listView = (ListView) findViewById(R.id.list);  
        listView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),  
                android.R.layout.simple_expandable_list_item_1, names));  
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.putExtra("spot_index", position);
				PickSpotActivity.this.setResult(Activity.RESULT_OK, i);
				finish();
			}
		});
  
        /*listView.setTextFilterEnabled(true); */ 
        searchView.setOnQueryTextListener(this);  
        searchView.setSubmitButtonEnabled(false);  
	}

	private void initActionbar() {
		// TODO Auto-generated method stub\
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(true);
		LayoutInflater minflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = minflater.inflate(R.layout.crj_action_bar, null);
		getActionBar().setCustomView(mTitleView,new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,  
                LayoutParams.WRAP_CONTENT));
		searchView = (SearchView) mTitleView.findViewById(R.id.search_view);
		
	}
	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(newText)) {  
            // Clear the text filter.  
            listView.clearTextFilter();
            update(names);
        } else {  
            // Sets the initial value for the text filter.  
            Object[] obj = searchItem(newText);
            update(obj);
        }
		return false;
	}
	private void update(Object[] obj) {
		// TODO Auto-generated method stub
		listView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),  
                android.R.layout.simple_expandable_list_item_1, obj)); 
	}
	private Object[] searchItem(String newText) {
		// TODO Auto-generated method stub
		List<String> st = new ArrayList<String>();
		for(Object str: names){
			if(str.toString().indexOf(newText)!=-1){
				st.add(str.toString());
			}
		}
		return st.toArray();
	}
	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
