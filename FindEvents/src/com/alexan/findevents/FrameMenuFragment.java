package com.alexan.findevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FrameMenuFragment extends ListFragment {

//	private String[] funcList = new String[] { "请登陆", "热门活动", "实时发布", "分类浏览",
//			"伙伴圈", "系统设置" };
	private String[] funcList = new String[] {"请登陆", "热门活动", "实时公告栏", "分类浏览",
			"系统设置" };
	private String currUser;
	private int nouser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.menu_list, null);
	}
	
	public class ReceiveBroadCast extends BroadcastReceiver
	{
	 
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	            //得到广播中得到的数据，并显示出来
	            nouser = intent.getIntExtra("nouser", 0);
	            
	        }
	 
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FrameMenuAdapter fma = new FrameMenuAdapter();
		setListAdapter(fma);
		this.setSelection(1);
	}

	public class FrameMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return funcList.length;
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
			View currView = LayoutInflater.from(getActivity()).inflate(
					R.layout.menu_item, null);
			
			if(position == 0) {
				currView.setBackgroundColor(Color.rgb(0, 168, 255));
				getListView().setTag(currView);
			}
			
			TextView tv = (TextView) currView
					.findViewById(R.id.menu_item_title);
			tv.setText(funcList[position]);
			
			if(position == 0) {
				currUser = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
						.getString("curr_user", "none");
				if(!currUser.equals("none")){
					tv.setText(currUser);
				}
				else{
					tv.setText(funcList[position]);
				}
			}
		
/*			if(position == 4 && currUser.equals("none")) {
				currView.setClickable(false);
			}*/ 
			return currView;
		}

	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		if ((lv.getTag() != null)) {
			((View) (lv.getTag())).setBackgroundDrawable(null);
		}
		lv.setTag(v);
		v.setBackgroundColor(Color.rgb(0, 168, 255));
		
		switch (position) {
		case 0:
			switchFragment(PersonalFragment.class, "personal", null);
			break;
		case 1: {
			Bundle b = new Bundle();
			b.putString("curr_city", ((FrameworkActivity)getActivity()).getCurrCity());
			switchFragment(HotEventFragment.class, "hotevent", b);
			break;
		}
		case 2: {
			Bundle b = new Bundle();
			b.putString("curr_city", ((FrameworkActivity)getActivity()).getCurrCity());
			switchFragment(RealtimeFragment.class, "realtimeevent", b);
			break;
		}
		case 3: {
			Bundle b = new Bundle();
			b.putString("curr_city", ((FrameworkActivity)getActivity()).getCurrCity());
			switchFragment(CategoryFragment.class, "categoryevent", b);
			break;
		}
//		case 4:
//			switchFragment(FriendCircleFragment.class, "friendcircle", null);
//			break;
		default:
			switchFragment(SettingsFragment.class, "settings", null);
			break;
		}

	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		FrameworkActivity fca = (FrameworkActivity) getActivity();
		fca.switchContent(fragment);
	}
	
	private void switchFragment(Class<? extends Fragment> fClass, String tag, Bundle data) {
		((FrameworkActivity)getActivity()).switchContent(fClass, tag, data);
	}

}
