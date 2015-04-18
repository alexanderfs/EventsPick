package com.alexan.findevents.event;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alexan.findevents.AppConstant;
import com.alexan.findevents.R;
import com.alexan.findevents.crj.JSONparse;
import com.alexan.findevents.crj.Util;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBImage;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.DensityUtil;
import com.alexan.findevents.util.ImageUtil;

public class PublishEventActivity extends SherlockActivity {

    private ActionBar vACBar;
    private EditText vTitle;
    private EditText vDesc;
    private GridView vCategory;
    private Spinner vProvince;
    private Spinner vCity;
    private Spinner vDistrict;
    private TextView vAddrName;
    private TextView vAddrDetail;
    private Button vStartTime;
    private Button vStartDate;
    private Button vEndTime;
    private Button vEndDate;
    private GridView vPhoto;
    private Button vPublish1;
    private DBEvent currEvent;
    private Button vPickSpot;

    private List<Bitmap> eventPhotos = new ArrayList<Bitmap>();
    
    String imagePath=null;//2015/4/13
    
    private List<DBImage> bdPhotos = new ArrayList<DBImage>();
    private PhotoAdapter pa;
    
    private long visible;
    private long userID;
    private String url = "http://123.57.45.183/event/PostEvent";

   // private List<DBCategory> categorySet = new ArrayList<DBCategory>();
    private List<String> categorySet = new ArrayList<String>();
    private Calendar c = Calendar.getInstance();
    private int year = c.get(Calendar.YEAR);
    private int month = c.get(Calendar.MONTH);
    private int dayofmonth = c.get(Calendar.DAY_OF_MONTH);
    private int hour = c.get(Calendar.HOUR_OF_DAY);
    private int minute = c.get(Calendar.MINUTE);

    private int year2 = c.get(Calendar.YEAR);
    private int month2 = c.get(Calendar.MONTH);
    private int dayofmonth2 = c.get(Calendar.DAY_OF_MONTH);
    private int hour2 = c.get(Calendar.HOUR_OF_DAY);
    private int minute2 = c.get(Calendar.MINUTE);
    
    Handler hHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {  
            case 1:  
                Toast.makeText(PublishEventActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();  
                break;            
              
			case 2:
				Toast.makeText(PublishEventActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();  
                break;  
			}
			super.handleMessage(msg);
		}
    	
    };

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
					
					JSONObject data = obj.getJSONObject("data");
					final long eventid = (long)data.getInt("EventId");
					
					final File file = new File(imagePath);
			        if(file!=null){
			        	new Thread(){
			    			@Override
			    			public void run() {
			    				// TODO Auto-generated method stub		    				
			    				try {  
				                     if (JSONparse.uploadimage(eventid, file)) {  
				                    	 hHandler.sendEmptyMessage(1);
				                    }else {
				                    	hHandler.sendEmptyMessage(2);
				                        //将数据发送给服务器失败  
				                    }  
				                } catch (Exception e) {  
				                    // TODO Auto-generated catch block  
				                    e.printStackTrace();  
				                }    
			    			}			
			    		}.start();
			    		
			        	
			        }
					
					Toast.makeText(PublishEventActivity.this, "发布成功"+eventid,
							Toast.LENGTH_SHORT).show();
					/*Long userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);*/
					
					finish();
				}	
				else{
					Toast.makeText(PublishEventActivity.this, json_data,
							Toast.LENGTH_SHORT).show();
				} 
			} catch (Exception e) {
				Toast.makeText(PublishEventActivity.this, "错误",
						Toast.LENGTH_SHORT).show();
			}

			super.handleMessage(msg); 
        }         
    };

    private DBLocation selectLo = new DBLocation();
    private long locationID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publishevent);
        initView();
        initData();
    }


    private void initData() {
        currEvent = new DBEvent();
        currEvent.setProvince("北京");
        currEvent.setCity("北京");
        currEvent.setDistrict("海淀");
        currEvent.setStarttime(Calendar.getInstance().getTime().getTime());
        currEvent.setEndtime(Calendar.getInstance().getTime().getTime());
    }

    private class CategoryRecorder implements CategorySelectListener {

        @Override
        public void setSelectedCategory(String category, boolean checked) {
            // TODO Auto-generated method stub
            if(checked) {
                categorySet.add(category);
            } else {
                categorySet.remove(category);
            }
        }

    }

    private int GetCatagoryId(List<String> categorySet){
    	for(String cate:categorySet){
    		switch (cate){
    		case "展会":
    			return 1009;
    		case "文娱":
    			return 1012;
    		case "体育":
    			return 1010;
    		case "商业":
    			return 1013;
    		case "医药":
    			return 1011;
    		case "科技":
    			return 1014;
    		case "亲子":
    			return 1005;
    		case "风俗":
    			return 1017;
    		case "文教":
    			return 1001;
    		case "其他":
    			return 1016;
    		default:
    			return 0;	
    		}
    	}
    	return 0;
    }

    @SuppressLint("ResourceAsColor")
	private void initView() {
        eventPhotos.clear();
        bdPhotos.clear();
        vACBar = getSupportActionBar();
        vACBar.setDisplayHomeAsUpEnabled(true);
        vACBar.setTitle("发布活动");

        vTitle = (EditText) findViewById(R.id.act_publishevent_etitle);
        vDesc = (EditText) findViewById(R.id.act_publishevent_edesc);

        vCategory = (GridView) findViewById(R.id.act_publishevent_category);
        CategoryCBAdapter ca = new CategoryCBAdapter(this, new CategoryRecorder(), null);
        vCategory.setAdapter(ca);

        vAddrName = (TextView) findViewById(R.id.act_publishevent_addresstitle);
        vAddrDetail = (TextView) findViewById(R.id.act_publishevent_address);

        vPickSpot = (Button) findViewById(R.id.act_publishevent_pickaddrbtn);
        vPickSpot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(PublishEventActivity.this, PickAddrActivity.class);
                startActivityForResult(i, 2);
            }
        });

        vStartTime = (Button) findViewById(R.id.act_publishevent_starttime);
        vStartTime.setText("" + hour + " : " + minute);
        vStartTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new TimePickerDialog(PublishEventActivity.this,
                        new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteofDay) {
                                // TODO Auto-generated method stub
                                vStartTime.setText("" + hourOfDay + " : " + minuteofDay);
                                hour = hourOfDay;
                                minute = minuteofDay;
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        vStartDate = (Button) findViewById(R.id.act_publishevent_startdate);
        vStartDate.setText("" + year + "-" + (month + 1) + "-" + dayofmonth);
        vStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PublishEventActivity.this,
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                vStartDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                PublishEventActivity.this.year = year;
                                month = monthOfYear;
                                dayofmonth = dayOfMonth;
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        vEndTime = (Button) findViewById(R.id.act_publishevent_endtime);
        vEndTime.setText("" + hour2 + " : " + minute2);
        vEndTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(PublishEventActivity.this,
                        new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteofDay) {
                                // TODO Auto-generated method stub
                                vEndTime.setText("" + hourOfDay + " : " + minuteofDay);
                                hour2 = hourOfDay;
                                minute2 = minuteofDay;
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        vEndDate = (Button) findViewById(R.id.act_publishevent_enddate);
        vEndDate.setText("" + year + "-" + (month2 + 1) + "-" + dayofmonth2);
        vEndDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PublishEventActivity.this,
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                vEndDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                PublishEventActivity.this.year2 = year;
                                month2 = monthOfYear;
                                dayofmonth2 = dayOfMonth;
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        vPhoto = (GridView) findViewById(R.id.act_publishevent_photo);
        pa = new PhotoAdapter();
        vPhoto.setAdapter(pa);

        vPublish1 = (Button) findViewById(R.id.act_publishevent_pub1);
        vPublish1.setText("发布");
        
        vPublish1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //getStoreEvent();
            	long curr_user_id = PreferenceManager.getDefaultSharedPreferences(PublishEventActivity.this).getLong("curr_user_id", -1);
            	if(curr_user_id == 0){
                    Toast.makeText(getBaseContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
            	}
                String strTmp = vTitle.getText().toString();
                if(strTmp == null || strTmp.equals("")) {
                    Toast.makeText(getBaseContext(), "请填写活动标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                currEvent.setTitle(strTmp);
                strTmp = vDesc.getText().toString();
                if(strTmp == null || strTmp.equals("")) {
                    Toast.makeText(getBaseContext(), "请填写活动描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                currEvent.setDescription(strTmp);
                if(categorySet == null || categorySet.size() == 0) {
                    Toast.makeText(getBaseContext(), "请选择活动分类", Toast.LENGTH_SHORT).show();
                    return;
                }

                strTmp = vAddrName.getText().toString();
                if(strTmp == null || strTmp.equals("请选择活动地点")) {
                    Toast.makeText(getBaseContext(), "请选择活动地点", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(PublishEventActivity.this).setTitle("选择发送目标")
                        .setItems(new String[]{"发送至公告栏", "发送至我的页面", "发送至伙伴群"},
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        switch(which) {
                                            case 0:
                                            case 1:
                                                getStoreEvent();
                                                break;
                                            case 2: {
                                                Intent i = new Intent(PublishEventActivity.this, PickGroupActivity.class);
                                                startActivityForResult(i, 3);
                                            }
                                            break;
                                        }
                                    }
                                }).show();
            }
        });
    }

    private void getStoreEvent() {
    	currEvent.setLocationID(locationID);
        currEvent.setAddress(selectLo.getAddrName());
        currEvent.setAddressdetail(selectLo.getAddrDetail());
        currEvent.setCity(selectLo.getAddrCity());
       // currEvent.setDistrict(selectLo.getAddrDistrict());
        currEvent.setStarttime(new GregorianCalendar(year, month, dayofmonth, hour, minute).getTime().getTime());
        currEvent.setEndtime(new GregorianCalendar(year2, month2, dayofmonth2, hour2, minute2).getTime().getTime());
        currEvent.setTimestamp(System.currentTimeMillis());
        //DBUser u= DBHelper.getInstance(this).getUserDao().loadAll().get(0);
		/*QueryBuilder<DBPerson> qbp = DBHelper.getInstance(this).getPersonDao().queryBuilder()
				.where(DBPersonDao.Properties.Nickname.eq(currUser));
		
		DBPerson p = qbp.list().get(0);*/
        month = month+1;
        month2 = month2+1;
        final String starttime = hour+":"+minute+":00";
        final String endtime = hour2+":"+minute2+":00";
        final String startday = year+"-"+month+"-"+dayofmonth;
        final String endday = year2+"-"+month2+"-"+dayofmonth2;
        userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);
        final int cateId = GetCatagoryId(categorySet);
        //String currUser =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("curr_user", "none");
        currEvent.setUserID(userID);

        currEvent.setVisibility(visible);

        long eventID = DBHelper.getInstance(this).getEventDao().insert(currEvent);

        for(DBImage img: bdPhotos) {
            img.setEventID(eventID);
            DBHelper.getInstance(this).getImageDao().insert(img);
        }
        
        new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String params = "UserId="+userID+"&EventTitle="+vTitle.getText().toString()+
						"&EventDescription="+vDesc.getText().toString()+"&EventStartDate="+startday+"&EventEndDate="+endday+"&EventStartTime="+starttime+"&EventEndTime="+endtime
						+"&EventCategoryId="+cateId+"&HasPictures="+1+"&EventVenueId="+locationID;
				String result = Util.httpPost(url, params);
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("params", result);
				message.setData(bundle);
				mHandler.sendMessage(message);
				
			}
		}).start();

    	this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()) {
            case android.R.id.home: {
                new AlertDialog.Builder(this)
                        .setTitle("退出")
                        .setMessage("确定放弃本次编辑？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                PublishEventActivity.this.finish();
                            }
                        }).setNegativeButton("返回继续编辑", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int reqWidth = DensityUtil.dip2px(this, 64f);
        
        if(resultCode == RESULT_OK && requestCode == 0) {
            Uri imageUri = data.getData();
            if(DocumentsContract.isDocumentUri(this, imageUri)){
                String wholeID = DocumentsContract.getDocumentId(imageUri);
                String id = wholeID.split(":")[1];
                String[] column = { MediaStore.Images.Media.DATA };
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                        sel, new String[] { id }, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    imagePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            else{
            	imagePath = getUriPath(imageUri);
            }
            if(imagePath!=null){
            	DBImage imgTemp = new DBImage();
                imgTemp.setImageUrl(imagePath);
                imgTemp.setTimestamp(System.currentTimeMillis());
                bdPhotos.add(imgTemp);
                eventPhotos.add(ImageUtil.decodeSampledBitmapFromPath(imagePath, reqWidth, reqWidth));
                
                pa.notifyDataSetChanged();
            }
            else{
            	System.out.println("图片路径错误！！");
            }
            
        } else if(resultCode == RESULT_OK && requestCode == 1) {
            //将保存在本地的图片取出并缩小后显示在界面上
            //String imagePath = Environment.getExternalStorageDirectory()+"/worktemp.jpg";
        	Bitmap picture = ImageUtil.decodeSampledBitmapFromPath(picPathTemp, reqWidth, reqWidth);
            if(picture != null) {
                eventPhotos.add(picture);
                pa.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "获取照片失败", Toast.LENGTH_SHORT).show();
            }
        } else if(resultCode == RESULT_OK && requestCode == 2) {
            locationID = data.getLongExtra("location_id", 0);
            if(locationID == 0) {
                vAddrName.setText("data error");
                vAddrDetail.setText("data error");
            } else {
                selectLo = DBHelper.getInstance(this).getLocationDao().load(locationID);
                vAddrName.setText(selectLo.getAddrName());
                vAddrDetail.setText(selectLo.getAddrDetail());
            }
        } else if(resultCode == RESULT_OK && requestCode == 3) {
            visible = data.getLongExtra("addr", -1);
            System.out.println("本事件可见范围是"+visible);
            getStoreEvent();
        }
    }

    private String getUriPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
       // Cursor cursor = managedQuery(uri, projection, null, null, null);
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String picPathTemp;
    private DBImage picTemp;

    private class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(eventPhotos.size() >= 3) {
                return eventPhotos.size();
            }
            return eventPhotos.size() + 1;
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

        @SuppressLint("NewApi")
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View currView = getLayoutInflater().inflate(R.layout.grid_photo_item, null);
            if(position == eventPhotos.size()) {
            	 ImageView currImg = (ImageView) currView.findViewById(R.id.grid_photo_item);
            	 currImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_attend));
                 currImg.setOnClickListener(new OnClickListener() {

                     @Override
                     public void onClick(View v) {
                         // TODO Auto-generated method stub
                         new AlertDialog.Builder(PublishEventActivity.this).setTitle("选择图片")
                                 .setItems(new String[]{"从相册选取", "拍照"}, 
                                		 new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     // TODO Auto-generated method stub
                                     switch(which) {
                                         case 0: {
                                             Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                             openAlbumIntent.setType("image/*");
                                             startActivityForResult(openAlbumIntent, 0);
                                         }
                                         break;
                                         case 1: {
                                         	String status=Environment.getExternalStorageState(); 
                                         	if(status.equals(Environment.MEDIA_MOUNTED)){
                                         		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                 StringBuilder sb = new StringBuilder();
                                                 String dirName, fileName;
                                                 dirName = Environment.getExternalStorageDirectory() + AppConstant.folderName;
                                                 fileName = System.currentTimeMillis() + ".jpg";
                                                 sb.append(dirName + "/" + fileName);
                                                 File dir=new File(dirName); 
                                                 if(!dir.exists())
                                                	 dir.mkdirs();
                                                 picPathTemp = sb.toString();
                                                 picTemp = new DBImage();
                                                 picTemp.setImageUrl(picPathTemp);
                                                 picTemp.setTimestamp(System.currentTimeMillis());
                                                 bdPhotos.add(picTemp);
                                                 Uri imageUri = Uri.fromFile(new File(dir, fileName));
                                                 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                 startActivityForResult(cameraIntent, 1);
                                         	}
                                         }
                                         break;
                                         default:
                                             break;
                                     
                                     }
                                 }
                             }).show();
                        }
                 });        	
            } 
            else {
                ImageView currImg = (ImageView) currView.findViewById(R.id.grid_photo_item);
                currImg.setImageBitmap(eventPhotos.get(position));
            }
            return currView;
        }
        
    }
}
