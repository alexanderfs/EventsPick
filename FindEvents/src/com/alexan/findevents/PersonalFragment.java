package com.alexan.findevents;



import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexan.findevents.crj.Util;
import com.alexan.findevents.friend.ContactListActivity;
import com.alexan.findevents.friend.GroupListActivity;
import com.alexan.findevents.me.MeMainPageActivity;
import com.alexan.findevents.me.MyCollectionActivity;
import com.alexan.findevents.me.MyShareActivity;
import com.alexan.findevents.me.PublishListActivity;
import com.alexan.findevents.me.SpotListActivity;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class PersonalFragment extends Fragment {
	
	private RelativeLayout vBgImage;
	private LinearLayout vUnlog;
	private LinearLayout vLogged;
	private LinearLayout vFuncList;
	private Button vLogin;
	private Button vRegister;
	private ImageView vImage;
	private TextView vName;
	private TextView vSignature;
	private TextView vHomePage;
	private TextView vMyPublish;
	private TextView vMyCollection;
	private TextView vMyShare;
	private TextView vContactBook;
	private TextView vGroup;
	private TextView vSpots;
	
	private String currUser;
	
	//created by crj
	private UserInfo mInfo;
	public static Tencent mTencent;
	private ImageButton qqLogin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_personalinfo, container, false);
		currUser = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
				.getString("curr_user", "none");
		String mAppid = "222222";
		mTencent = Tencent.createInstance(mAppid, getActivity());
		initView(v);
		getActivity().setTitle("个人中心");
		((FrameworkActivity)getActivity()).setPFlag(0);
		((FrameworkActivity)getActivity()).supportInvalidateOptionsMenu();
		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		currUser = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
				.getString("curr_user", "none");
		if(currUser.equals("none")) {
			vLogged.setVisibility(View.GONE);
			vUnlog.setVisibility(View.VISIBLE);
			vLogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), LoginActivity.class);
					i.putExtra("log_type", 1);
					startActivity(i);
				}
			});
			vRegister.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), RegisterActivity.class);
					i.putExtra("register_type", 1);
					startActivity(i);
				}
			});
			vFuncList.setVisibility(View.INVISIBLE);
		} else {
			vLogged.setVisibility(View.VISIBLE);
			vUnlog.setVisibility(View.GONE);
			vName.setText(currUser);
			vImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), MeMainPageActivity.class);
					startActivity(i);
				}
			});
			//这不是一个好设计。需要禁用/启用下列按钮
			vFuncList.setVisibility(View.VISIBLE);
		}
	}



	private void initView(View mainView) {
		vBgImage = (RelativeLayout) mainView.findViewById(R.id.fgt_personal_bg);
		vUnlog = (LinearLayout) mainView.findViewById(R.id.fgt_personal_unlog);
		vLogin = (Button) mainView.findViewById(R.id.fgt_personal_login);
		qqLogin = (ImageButton)mainView.findViewById(R.id.fgt_personal_qqlogin);
		vRegister = (Button) mainView.findViewById(R.id.fgt_personal_register);
		vLogged = (LinearLayout) mainView.findViewById(R.id.fgt_personal_logged);
		vImage = (ImageView) mainView.findViewById(R.id.fgt_personal_icon);
		vName = (TextView) mainView.findViewById(R.id.fgt_personal_nickname);
		
		
		vSignature = (TextView) mainView.findViewById(R.id.fgt_personal_signature);
		
		
		vFuncList = (LinearLayout) mainView.findViewById(R.id.fgt_personal_funclist);
		
		vHomePage = (TextView) mainView.findViewById(R.id.fgt_personal_homepage);
		vHomePage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), MeMainPageActivity.class);
				startActivity(i);
			}
		});
		vMyShare = (TextView) mainView.findViewById(R.id.fgt_personal_share);
		vMyShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), MyShareActivity.class);
				startActivity(i);
			}
		});
		vMyPublish = (TextView) mainView.findViewById(R.id.fgt_personal_publish);
		vMyPublish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), PublishListActivity.class);
				startActivity(i);
			}
		});
		vMyCollection = (TextView) mainView.findViewById(R.id.fgt_personal_collect);
		vMyCollection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), MyCollectionActivity.class);
				startActivity(i);
			}
		});
		

		vContactBook = (TextView) mainView.findViewById(R.id.fgt_personal_contactbook);
		vContactBook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), ContactListActivity.class);
				startActivity(i);
			}
		});
		
		vGroup = (TextView) mainView.findViewById(R.id.fgt_personal_group);
		vGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), GroupListActivity.class);
				startActivity(i);
			}
		});
		
		vSpots = (TextView) mainView.findViewById(R.id.fgt_personal_spots);
		vSpots.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), SpotListActivity.class);
				startActivity(i);
			}
		});
		
		//Created by Crj
		qqLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mTencent.isSessionValid()) {
					mTencent.login(PersonalFragment.this, "all",  new IUiListener() {
						 
						@Override
						public void onError(UiError e) {
							Util.toastMessage(getActivity(), "onError: " + e.errorDetail);
							Util.dismissDialog();
						}

						@Override
						public void onCancel() {
							Util.toastMessage(getActivity(), "onCancel: ");
							Util.dismissDialog();
						}

						@Override
						public void onComplete(Object arg0) {
							// TODO Auto-generated method stub
							Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
							updateUserInfo();
							updateLoginButton();
							
						}
			        });
					Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
				} else {
				    mTencent.logout(getActivity());
					updateUserInfo();
					updateLoginButton();
				}
			}

			
		});
	}

	protected void updateLoginButton() {
		// TODO Auto-generated method stub
		System.out.println("Come here update Login");
		vLogged.setVisibility(View.VISIBLE);
		vUnlog.setVisibility(View.GONE);
		//vName.setText(currUser);
		vImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), MeMainPageActivity.class);
				startActivity(i);
			}
		});
		//这不是一个好设计。需要禁用/启用下列按钮
		vFuncList.setVisibility(View.VISIBLE);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						vName.setVisibility(android.view.View.VISIBLE);
						vName.setText(response.getString("nickname"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}else if(msg.what == 1){
				Bitmap bitmap = (Bitmap)msg.obj;
				vImage.setImageBitmap(bitmap);
				vImage.setVisibility(android.view.View.VISIBLE);
			}
		}

	};
	
	protected void updateUserInfo() {
		// TODO Auto-generated method stub
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				
				@Override
				public void onError(UiError e) {
				}
				
				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread(){

						@Override
						public void run() {
							JSONObject json = (JSONObject)response;
							if(json.has("figureurl")){
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
								} catch (JSONException e) {
									
								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}
						
					}.start();
				}
				
				@Override
				public void onCancel() {
					
				}
			};
			mInfo = new UserInfo(getActivity(), mTencent.getQQToken());
			mInfo.getUserInfo(listener);
			
			
		} else {
			vName.setText("");
			vName.setVisibility(android.view.View.GONE);
			vImage.setVisibility(android.view.View.GONE);
		}
	}
	
}
