package com.alexan.findevents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.dao.DBGroup;
import com.alexan.findevents.dao.DBPerson;
import com.alexan.findevents.event.EventSearchActivity;
import com.alexan.findevents.event.PublishEventActivity;
import com.alexan.findevents.util.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class FrameworkActivity extends SlidingFragmentActivity {

	private Fragment mContent;
	static public List<DBPerson> personList;
	static public List<DBGroup> groupList;
	static public List<String> hotList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("");
		setContentView(R.layout.activity_framework);
		
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null) {
			mContent = new HotEventFragment();
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.act_framework_content, mContent).commit();

		// set the Behind View
		setBehindContentView(R.layout.fragment_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fgt_menu_content, new FrameMenuFragment()).commit();

		// customize the SlidingMenu
		setSlidingActionBarEnabled(false);
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindWidth(DensityUtil.dip2px(this, 240f));
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initData();
		
	}
	
	private void initData() {
		//created by crj
		//friendList = DataClass.friendList;
		/*if(groupList.size()==0) {		
			String[] data = getResources().getStringArray(R.array.circle);
			for(String s : data){
				DBGroup dg = new DBGroup();
				dg.setGroupname(s);
				dg.setGroupcapacity(20);
				dg.setTimestamp(System.currentTimeMillis());
				DBHelper.getInstance(this).getGroupDao().insert(dg);
			}
		}*/
		//created by crj!!
		String[] hotspots = getResources().getStringArray(R.array.hotspot);
		for(String st:hotspots){
			hotList.add(st);
		}
		currCity = hotList.get(0);
		
	}
	
	private class SpotListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return hotList.size();
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
			View v = getLayoutInflater().inflate(R.layout.list_simple_item_white, null);
			TextView tv = (TextView) v.findViewById(R.id.list_simple_item_white);
			tv.setText(hotList.get(position));
			return v;
		}
		
	}
	
	private long exitTime = 0;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()- exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	        	finish();
	        }
	        return true;   
	    }
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			toggle();
			return true;
		}
		case R.id.action_framework_add: {
			Intent i = new Intent(this, PublishEventActivity.class);
			startActivityForResult(i, 0);
			return true;
		}
		case R.id.action_framework_search: {
			Intent i = new Intent(this, EventSearchActivity.class);
			startActivity(i);
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private int pflag = 1;
	public void setPFlag(int value) {
		pflag = value;
	}
	
	private SpotListAdapter sla;
	private  String currCity;
	public  String getCurrCity (){
		return currCity;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		sla = new SpotListAdapter();
		switch(pflag) {
			case 0: {
				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			}
			break;
			case 1: case 2: case 3:	{
				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				getSupportActionBar().setListNavigationCallbacks(sla, new OnNavigationListener() {
					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {
						// TODO Auto-generated method stub
						//Toast.makeText(FrameworkActivity.this, hsList.get(itemPosition).getName(), Toast.LENGTH_SHORT).show();
						if(!hotList.get(itemPosition).equals("更多")){
							currCity = hotList.get(itemPosition);
						}
						updateHotList(itemPosition, hotList.get(itemPosition));
						
						return false;
					}
				});
			}
			break;			
			case 4: {
				final ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this, 
						R.array.friendfunc, R.layout.list_simple_item_white1);
				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				getSupportActionBar().setListNavigationCallbacks(aa, new OnNavigationListener() {
					
					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {
						// TODO Auto-generated method stub
						//Toast.makeText(FrameworkActivity.this, aa.getItem(itemPosition), Toast.LENGTH_SHORT).show();
						updateData(itemPosition);
						return false;
					}
				});
			}
			break;
			default: {
				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			}
			break;
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 0 && arg1 == Activity.RESULT_OK) {
			int index;
			index = arg2.getIntExtra("spot_index", -1);
			if(index!=-1){
				String pot = hotList.get(index);
				hotList.add(0, pot);
				hotList.remove(index+1);
			}
			
			supportInvalidateOptionsMenu();
		}
		else if(arg0 == 0 && arg1 == Activity.RESULT_CANCELED){
			supportInvalidateOptionsMenu();
		}
	}

	private void updateHotList(int position, String hs) {
		if(hs.equals("更多")) {
			Intent i = new Intent(this, PickSpotActivity.class);
			startActivityForResult(i, 0);
			return;
		}
		if(!hotList.get(0).equals(hs)){
			hotList.add(0, hs);
			hotList.remove(position+1);
		}
		
		
		switch(pflag) {
			case 1: {
				((HotEventFragment)mContent).reloadData(hs);
				break;
			}
			case 2: {
				((RealtimeFragment)mContent).reloadData(hs);
				break;
			}
			case 3: {
				((CategoryFragment)mContent).reloadData(hs);
				break;
			}
		}
	}
	
	private void updateData(int pos) {
		((FriendCircleFragment)mContent).reloadData(pos);
	}
	
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.act_framework_content, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	
	public void switchContent(Class<? extends Fragment> fClass, String tag, Bundle args) {
		FragmentManager fm = getSupportFragmentManager();
		mContent = fm.findFragmentByTag(tag);  
        boolean isFragmentExist = true;  
        if (mContent == null) {  
            try {  
                isFragmentExist = false;  
                mContent = fClass.newInstance();  
                mContent.setArguments(new Bundle());  
            } catch (java.lang.InstantiationException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            }  
        }  
        if(mContent.isAdded()){ 
        	getSlidingMenu().showContent();
            return;  
        }  
        if( args != null && !args.isEmpty() ) {  
        	mContent.getArguments().putAll(args);  
        }  
        FragmentTransaction ft = fm.beginTransaction();  
        /*ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,  
                android.R.anim.fade_in, android.R.anim.fade_out);*/  
        if( isFragmentExist ) {  
            ft.replace(R.id.act_framework_content, mContent);  
        } else {  
            ft.replace(R.id.act_framework_content, mContent, tag);  
        }  
          
        ft.addToBackStack(tag);  
        ft.commitAllowingStateLoss();  
        getSlidingMenu().showContent();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
			setBehindContentView(R.layout.fragment_menu);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fgt_menu_content, new FrameMenuFragment()).commit();
		}
	}

}
