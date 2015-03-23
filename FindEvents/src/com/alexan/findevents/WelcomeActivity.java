package com.alexan.findevents;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.alexan.findevents.R;
import com.alexan.findevents.util.DensityUtil;

public class WelcomeActivity extends Activity {
	
	private ViewPager vViewPager;
	private LinearLayout vDots;
	private List<View> imageList = new ArrayList<View>();
	private Button vLogin;
	private Button vRegister;
	private Button vVisit;
	private Button vEnter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		initView();
		
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
			.putBoolean("is_first", false).commit();
	}
	
	
	private void initView() {
		vDots = (LinearLayout) findViewById(R.id.act_welcome_dots);
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int mpx = DensityUtil.dip2px(this, 4f);
		lp.setMargins(mpx, mpx, mpx, mpx);
		for(int i = 0; i < 3; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.page_indicator_bg);
			vDots.addView(iv);
		}
		vDots.getChildAt(0).setEnabled(true);
		vDots.getChildAt(1).setEnabled(false);
		vDots.getChildAt(2).setEnabled(false);
		
		vViewPager = (ViewPager) findViewById(R.id.act_welcome_vp);
		LayoutInflater li = getLayoutInflater();
		View vTmp = li.inflate(R.layout.viewpager_item, null);
		vTmp.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcom3));
		imageList.add(vTmp);
		vTmp = li.inflate(R.layout.viewpager_item, null);
		vTmp.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcom2));
		imageList.add(vTmp);
		vTmp = li.inflate(R.layout.viewpager_item, null);
		vTmp.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcom1));
		imageList.add(vTmp);
		
		WelcomeVPAdapter wa = new WelcomeVPAdapter();
		vViewPager.setAdapter(wa);
		vViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				for(int i = 0; i < vDots.getChildCount(); i++) {
					vDots.getChildAt(i).setEnabled(false);
				}
				vDots.getChildAt(arg0).setEnabled(true);
				
				if(arg0 == imageList.size() - 1) {
					String currUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
							.getString("curr_user", "none");
					if(currUser.equals("none")) {
						/*vLogin.setVisibility(View.VISIBLE);
						vLogin.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
								i.putExtra("log_type", 0);
								startActivity(i);
							}
						});
						vRegister.setText("注册");
						vRegister.setVisibility(View.VISIBLE);
						vRegister.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class);
								i.putExtra("register_type", 0);
								startActivity(i);
							}
						});*/
						vVisit.setVisibility(View.VISIBLE);
						vVisit.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(WelcomeActivity.this, StartupActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("type", 0);
								startActivity(i);
							}
						});
					} /*else {
						vRegister.setText("立即进入");
						vRegister.setVisibility(View.VISIBLE);
						vRegister.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(WelcomeActivity.this, StartupActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("type", 0);
								startActivity(i);
							}
						});
					}
					*/
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//vViewPager.setCurrentItem(0);
		//vLogin = (Button) findViewById(R.id.act_welcome_login);
		//vRegister = (Button) findViewById(R.id.act_welcome_register);
		vVisit = (Button) findViewById(R.id.act_welcome_visit);
	}
	
	public class WelcomeVPAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		 @Override  
         public void destroyItem(ViewGroup container, int position,  
                 Object object) {  
             container.removeView(imageList.get(position));  

         }  

         @Override  
         public int getItemPosition(Object object) {  

             return super.getItemPosition(object);  
         }  

         @Override  
         public Object instantiateItem(ViewGroup container, int position) {  
             container.addView(imageList.get(position));  
             return imageList.get(position);  
         }  
	}
}
