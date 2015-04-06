package com.alexan.findevents;

import com.alexan.findevents.R;
import com.alexan.findevents.settings.AboutUsActivity;
import com.alexan.findevents.settings.AccountAttachActivity;
import com.alexan.findevents.settings.AccountInfoActivity;
import com.alexan.findevents.settings.FeedbackActivity;
import com.alexan.findevents.settings.NotifySettingActivity;
import com.alexan.findevents.settings.PrivacySettingActivity;
import com.alexan.findevents.settings.SafeSettingActivity;
import com.alexan.findevents.settings.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
	private TextView vTV1;
	private TextView vTV2;
	private TextView vTV3;
	private TextView vTV4;
	private TextView vTV5;
	private TextView vTV6;
	private TextView vTV7;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_settings, container, false);
		initView(v);
		((FrameworkActivity)getActivity()).setPFlag(5);
		((FrameworkActivity)getActivity()).supportInvalidateOptionsMenu();
		getActivity().setTitle("系统设置");
		return v;
	}
	
	private void initView(View v) {
		vTV1 = (TextView) v.findViewById(R.id.fgt_setting_account);
		long curruser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong("curr_user_id", 0);
		if(curruser_id == 0){
			vTV1.setVisibility(View.GONE);
		}
		else
			vTV1.setVisibility(View.VISIBLE);
		vTV1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AccountInfoActivity.class);
				getActivity().startActivityForResult(i, 1);
			}
		});
		/*vTV2 = (TextView) v.findViewById(R.id.fgt_setting_safe);
		vTV2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), SafeSettingActivity.class);
				startActivity(i);
			}
		});
		vTV3 = (TextView) v.findViewById(R.id.fgt_setting_accountattach);
		vTV3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AccountAttachActivity.class);
				startActivity(i);
			}
		});
		vTV4 = (TextView) v.findViewById(R.id.fgt_setting_notify);
		vTV4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), NotifySettingActivity.class);
				startActivity(i);
			}
		});
		vTV5 = (TextView) v.findViewById(R.id.fgt_setting_privacy);
		vTV5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), PrivacySettingActivity.class);
				startActivity(i);
			}
		});*/
		vTV6 = (TextView) v.findViewById(R.id.fgt_setting_feedback);
		vTV6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), FeedbackActivity.class);
				startActivity(i);
			}
		});
		vTV7 = (TextView) v.findViewById(R.id.fgt_setting_about);
		vTV7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AboutUsActivity.class);
				startActivity(i);
			}
		});
		
	}
}
