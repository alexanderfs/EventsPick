package com.alexan.findevents.event;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.R;
import com.alexan.findevents.crj.JSONparse;
import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBPerson;
import com.alexan.findevents.dao.DBPickEvent;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.DensityUtil;
import com.alexan.findevents.util.ImageUtil;
import com.alexan.findevents.util.StringFormatUtil;
import com.alexan.findevents.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EventDetailActivity extends SherlockActivity {
	
	private ActionBar vACBar;
	private TextView vTitle;
	private TextView vOtherDetail;
	private View vCollect;
	private View vShare;
	private View vComment;
	private View vReport;
	private ListViewForScrollView vDetail;
	private TextView vDesc;
	private ListViewForScrollView vCommentsList;
	private ScrollView vSV;
    private ViewPager vImage;
    private ImageView imagev;
    private List<ImageView> imageList = new ArrayList<ImageView>();
	private DBPickEvent currEvent = new DBPickEvent();
	private boolean isFake;
	private LinearLayout vDots;
	private String url = "http://123.57.45.183/event/AddFavorites";
	
	Handler mHandler=new Handler(){  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
        	Bundle bundle = msg.getData();
			String result = bundle.getString("params");
			
			try {
				// 处理结果
				JSONObject obj = new JSONObject(result);		
				
				String json_data = obj.getString("msg");
	
				if (obj.getString("code").equals("10000")) {
					
					Toast.makeText(EventDetailActivity.this, "收藏成功",
							Toast.LENGTH_SHORT).show();
					
					DBComment dc = new DBComment();
					dc.setEventID(currEvent.getId());
					dc.setCommentType(2);
					dc.setUserID(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0));
			        String username =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("curr_user", "none");
					dc.setUsername(username);
					dc.setComentContent(username + "收藏了一个活动");
					dc.setTimestamp(System.currentTimeMillis());
					DBHelper.getInstance(EventDetailActivity.this).getCommentDao().insert(dc);
					
					currEvent.setCollectionNum((currEvent.getCollectionNum() == null ? 0 : currEvent.getCollectionNum()) + 1);
					DBHelper.getInstance(EventDetailActivity.this).getPickEventDao().update(currEvent);
					vOtherDetail.setText(ImageUtil.getEventOtherDetail(currEvent, EventDetailActivity.this));
				}	
				else{
					Toast.makeText(EventDetailActivity.this, json_data,
							Toast.LENGTH_SHORT).show();
				} 
			} catch (Exception e) {
				Toast.makeText(EventDetailActivity.this, "错误",
						Toast.LENGTH_SHORT).show();
			}

			super.handleMessage(msg); 
        }         
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventdetail);
		
		initView();
		
		try {
			currEvent = DBHelper.getInstance(this).getPickEventDao().loadByRowId(getIntent().getExtras().getLong("event_id"));
			if(currEvent == null) {
				currEvent = new DBPickEvent();
			}
		} catch (Exception e) {
			currEvent = new DBPickEvent();
			e.printStackTrace();
		}
		
		initData();
	}
	
	private void initData() {
		vTitle.setText(currEvent.getTitle() == null ? "DEFAULT TITLE" : currEvent.getTitle());
		
		if(currEvent.getId() != null) {
			createPager();
		}
		
		vOtherDetail.setText(ImageUtil.getEventOtherDetail(currEvent, this));
		DetailAdapter da = new DetailAdapter();
		vDetail.setAdapter(da);
		
		vDesc = (TextView) findViewById(R.id.act_eventdetail_desc);
		vDesc.setText(currEvent.getDescription());
		
		/*vCommentsList = (ListViewForScrollView) findViewById(R.id.act_eventdetail_comments);
		CommentsAdapter ca;
		if(currEvent.getId() == null) {
			ca = new CommentsAdapter(this, new ArrayList<DBComment>());
		} else {
			ca = new CommentsAdapter(this,
					DBHelper.getInstance(this).getCommentDao().queryBuilder().where(DBCommentDao.Properties.EventID.eq(currEvent.getId())).list());
		}
		
		vCommentsList.setAdapter(ca);*/
		
	}
    private void createPager(){
        String photoUrl = currEvent.getPhoto();
        if(photoUrl!=null) {
            for(int i = 0;i<1;i++){
                imagev = (ImageView)this.getLayoutInflater().inflate(R.layout.imageview_item,null);
                
                DisplayImageOptions options = new DisplayImageOptions.Builder()   
                .cacheInMemory(true)  
                .cacheOnDisc(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
          
				ImageLoader.getInstance().displayImage(photoUrl, imagev, options);
	
                imageList.add(imagev);
            }
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
    		
            vImage.setCurrentItem(0);
            vImage.setAdapter(new PagerAdapter() {
            	
                @Override
                public int getCount() {
                   
                    return imageList.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object o) {
                    return view == o;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    //super.destroyItem(container, position, object);
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    try{
                        ((ViewPager)container).addView(imageList.get(position),0);
                    }catch (Exception e){
                    }
                    return imageList.get(position);
                }

                @Override
                public int getItemPosition(Object object) {
                    return super.getItemPosition(object);
                }
            });
            
            vImage.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					for(int i = 0; i < vDots.getChildCount(); i++) {
						vDots.getChildAt(i).setEnabled(false);
					}
					vDots.getChildAt(arg0).setEnabled(true);
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
        }
    }
	
	/*public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.menu_eventdetail, menu);
		MenuItem shareItem = menu.findItem(R.id.action_eventdetail_share);  
        ShareActionProvider mShareActionProvider = (ShareActionProvider)  
                shareItem.getActionProvider();  
        mShareActionProvider.setShareIntent(getDefaultIntent());  
		return true;
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}


	void initView() {
		vACBar = getSupportActionBar();
		setTitle("活动详情");
		vACBar.setDisplayHomeAsUpEnabled(true);
		vTitle = (TextView)findViewById(R.id.act_eventdetail_title);
		vImage = (ViewPager) findViewById(R.id.act_eventdetail_image);
		vOtherDetail = (TextView)findViewById(R.id.act_eventdetail_others);
		vCollect = findViewById(R.id.act_eventdetail_btn1);
		vCollect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFavorite();
			}
		});
		vShare = findViewById(R.id.act_eventdetail_btn2);
		vShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setAction(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_TEXT, currEvent.getTitle());
				i.setType("text/plain");
				startActivity(i);
			}
		});
		vComment = findViewById(R.id.act_eventdetail_btn3);
		vComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(EventDetailActivity.this, EventCommentActivity.class);
				i.putExtra("eventID", currEvent.getId());
				startActivityForResult(i, 0);
			}
		});
		vReport = findViewById(R.id.act_eventdetail_btn4);
		vReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EventDetailActivity.this, EventReportActivity.class);
				startActivityForResult(i, 1);
			}
		});
		vSV = (ScrollView) findViewById(R.id.act_eventdetail_scroll);
		
		vSV.smoothScrollTo(0, 0);
		vDetail = (ListViewForScrollView) findViewById(R.id.act_eventdetail_list);
		
	}
	
	
	protected void addFavorite() {
		// TODO Auto-generated method stub
		
		long userId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);
		long eventID = currEvent.getId();
		
		String params = "UserId="+userId+"&EventId="+eventID;
		JSONparse.postComment(mHandler, params, url);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0) {
			currEvent = DBHelper.getInstance(this).getPickEventDao().load(currEvent.getId());
			vOtherDetail.setText(ImageUtil.getEventOtherDetail(currEvent, this));
		}
	}



	private class DetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
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
			View currView;
			switch(position) {
			case 0: {
				currView = getLayoutInflater().inflate(R.layout.list_image_text_item, null);
				ImageView iv = (ImageView) currView.findViewById(R.id.list_it_icon);
				iv.setImageResource(R.drawable.item_time);
				TextView tv = (TextView) currView.findViewById(R.id.list_it_desc);
				tv.setText("【时间】："+currEvent.getStartt()+"-"+currEvent.getEndt());
			}
				break;
			case 1: {
				currView = getLayoutInflater().inflate(R.layout.list_image_text_item, null);
				ImageView iv = (ImageView) currView.findViewById(R.id.list_it_icon);
				iv.setImageResource(R.drawable.item_location);
			/*	iv.setImageDrawable(getResources().getDrawable(R.drawable.item_location));*/
				final TextView tv = (TextView) currView.findViewById(R.id.list_it_desc);
				tv.setText(StringFormatUtil.buildPickAddrString(currEvent));
				currView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(EventDetailActivity.this, ShowAddrActivity.class);
						intent.putExtra("addr", tv.getText());
						startActivity(intent);
					}
				});
			}
				break;
			case 2: {
				currView = getLayoutInflater().inflate(R.layout.list_image_text_item, null);
				ImageView iv = (ImageView) currView.findViewById(R.id.list_it_icon);
				iv.setImageResource(R.drawable.item_ticket);
				TextView tv = (TextView) currView.findViewById(R.id.list_it_desc);
				tv.setText("【票价】 NO TICKET PRICE ITEM IN PUBLISH");
			}
				break;
			case 3: {
				currView = getLayoutInflater().inflate(R.layout.list_image_text_item, null);
				ImageView iv = (ImageView) currView.findViewById(R.id.list_it_icon);
				iv.setImageResource(R.drawable.item_catagory);
				TextView tv = (TextView) currView.findViewById(R.id.list_it_desc);
				if(currEvent.getId() == null || currEvent.getCatagory() == null) {
					tv.setText("【类型】 DEFAULT CATEGORY");
				} else {
					/*StringBuilder sb = new StringBuilder();
					sb.append("【类型】 ");
					for(DBEventCategory eca: currEvent.getCategories()) {
						DBCategory ca = eca.getDBCategory();//DBHelper.getInstance(EventDetailActivity.this).getCategoryDao().loadByRowId(eca.getCategoryID());
						sb.append(ca.getName()).append(" ");
					}
					tv.setText(sb.toString());*/
					tv.setText("【类型】 "+currEvent.getCatagory());
				}
				
			} 
				break;
			default: {
				currView = getLayoutInflater().inflate(R.layout.list_image_text_item, null);
				ImageView iv = (ImageView) currView.findViewById(R.id.list_it_icon);
				iv.setImageResource(R.drawable.item_publisher);
				TextView tv = (TextView) currView.findViewById(R.id.list_it_desc);
				if(currEvent.getId() == null) {
					tv.setText("DEFAULT USER");
				} else {
					DBPerson p = DBHelper.getInstance(EventDetailActivity.this).getPersonDao().loadByRowId(currEvent.getUserID());
					if(p != null) {
						tv.setText("【发布人】 "+p.getNickname());
					} else {
						tv.setText("【发布人】 load error");
					}
				}
				
			} 
				break;
			}
			return currView;
		}
		
	}
}
