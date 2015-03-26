package com.alexan.findevents.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
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
import com.alexan.findevents.dao.DBCity;
import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBEventCategory;
import com.alexan.findevents.dao.DBEventCategoryDao.Properties;
import com.alexan.findevents.dao.DBEventDao;
import com.alexan.findevents.dao.DBImage;
import com.alexan.findevents.dao.DBLocation;
import com.alexan.findevents.dao.DBProvince;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.util.DensityUtil;
import com.alexan.findevents.util.ImageUtil;

@SuppressLint("ResourceAsColor")
public class EventEditActivity extends SherlockActivity {

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
    private Button vPickSpot;

    private List<Bitmap> eventPhotos = new ArrayList<Bitmap>();
    private List<DBImage> bdPhotos = new ArrayList<DBImage>();
    private PhotoAdapter pa;
    private long visible;

    private DBLocation selectLo ;

    private List<String> categorySet = new ArrayList<String>();
    private int year;
    private int month;
    private int dayofmonth;
    private int hour;
    private int minute;

    private int year2;
    private int month2;
    private int dayofmonth2;
    private int hour2;
    private int minute2;

    private List<DBProvince> provinceList = new ArrayList<DBProvince>();
    private List<DBCity> cityList = new ArrayList<DBCity>();/*
    private List<DBDistrict> districtList = new ArrayList<DBDistrict>();*/

//    private ProvinceAdapter pad = new ProvinceAdapter();
//    private CityAdapter cad = new CityAdapter();
//    private DistrictAdapter dad = new DistrictAdapter();

    private DBEvent currEvent;
    private long eventID;
    private Calendar sdc, edc;
    
    int reqWidth ;
    int lastimglen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishevent);
        Intent intent = getIntent();
        eventID = intent.getLongExtra("eventid", -1);
        reqWidth = DensityUtil.dip2px(this, 64f);
        List<DBEvent> eventlist = DBHelper.getInstance(this).getEventDao().
        		queryBuilder().where(DBEventDao.Properties.Id.eq(eventID)).list();
        if(eventlist.size()>0){
            currEvent = eventlist.get(0);
        }
        else{
            Log.d("findeventerror", "事件错误");
            return;
        }

        initData();
        initView();

    }
    
    private void initData() {
    	
    	selectLo = DBHelper.getInstance(this).getLocationDao().load(currEvent.getLocationID());
    	
        Date sd = new Date(currEvent.getStarttime());
        sdc = Calendar.getInstance();
        sdc.setTime(sd);
        Date ed = new Date(currEvent.getEndtime());
        edc = Calendar.getInstance();
        edc.setTime(ed);

        year = sdc.get(Calendar.YEAR);
        month = sdc.get(Calendar.MONTH);
        dayofmonth = sdc.get(Calendar.DAY_OF_MONTH);
        hour = sdc.get(Calendar.HOUR_OF_DAY);
        minute = sdc.get(Calendar.MINUTE);

        year2 = edc.get(Calendar.YEAR);
        month2 = edc.get(Calendar.MONTH);
        dayofmonth2 = edc.get(Calendar.DAY_OF_MONTH);
        hour2 = edc.get(Calendar.HOUR_OF_DAY);
        minute2 = edc.get(Calendar.MINUTE);
        
        categorySet.clear();
       /* for(DBEventCategory ec : currEvent.getCategories()){
            categorySet.add(ec.getDBCategory());
        }*/
        //actionbar的设置
        eventPhotos.clear();
        bdPhotos.clear();
        bdPhotos = DBHelper.getInstance(this).getImageDao().queryBuilder().where(Properties.EventID.eq(eventID)).list();
        for(DBImage ima:bdPhotos){
        	String imapath = ima.getImageUrl();
        	eventPhotos.add(ImageUtil.decodeSampledBitmapFromPath(imapath, reqWidth, reqWidth));
        }
        lastimglen = bdPhotos.size();
        
    }

    private void initView() {
        //获取事件的事件

        vACBar = getSupportActionBar();
        vACBar.setDisplayHomeAsUpEnabled(true);
        vACBar.setTitle("修改活动");
        //标题，描述
        vTitle = (EditText) findViewById(R.id.act_publishevent_etitle);
        vTitle.setText(currEvent.getTitle());
        vDesc = (EditText) findViewById(R.id.act_publishevent_edesc);
        vDesc.setText(currEvent.getDescription());
        //类别
        vCategory = (GridView) findViewById(R.id.act_publishevent_category);
        CategoryCBAdapter ca = new CategoryCBAdapter(this, new CategoryRecorder(), categorySet);
        vCategory.setAdapter(null);
        vCategory.setAdapter(ca);
        //地址名称和细节
        vAddrName = (TextView) findViewById(R.id.act_publishevent_addresstitle);
        vAddrName.setText(currEvent.getAddress());
        vAddrDetail = (TextView) findViewById(R.id.act_publishevent_address);
        vAddrDetail.setText(currEvent.getAddressdetail());
        //地址选择按钮
        vPickSpot = (Button) findViewById(R.id.act_publishevent_pickaddrbtn);
        vPickSpot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(EventEditActivity.this, PickAddrActivity.class);
                startActivityForResult(i, 2);
            }
        });
        //起止时间与日期
        vStartTime = (Button) findViewById(R.id.act_publishevent_starttime);
        vStartTime.setText("" + hour + " : " + minute);
        vStartTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new TimePickerDialog(EventEditActivity.this,
                        new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // TODO Auto-generated method stub
                                vStartTime.setText("" + hourOfDay + " : " + minute);
                                hour = hourOfDay;
                                EventEditActivity.this.minute = minute;
                            }
                        }, sdc.get(Calendar.HOUR_OF_DAY), sdc.get(Calendar.MINUTE), true).show();
            }
        });

        vStartDate = (Button) findViewById(R.id.act_publishevent_startdate);
        vStartDate.setText("" + year + "-" + (month + 1) + "-" + dayofmonth);
        vStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EventEditActivity.this,
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                vStartDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                EventEditActivity.this.year = year;
                                month = monthOfYear;
                                dayofmonth = dayOfMonth;
                            }
                        }, sdc.get(Calendar.YEAR), sdc.get(Calendar.MONTH), sdc.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        vEndTime = (Button) findViewById(R.id.act_publishevent_endtime);
        vEndTime.setText("" + hour2 + " : " + minute2);
        vEndTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(EventEditActivity.this,
                        new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // TODO Auto-generated method stub
                                vEndTime.setText("" + hourOfDay + " : " + minute);
                                hour2 = hourOfDay;
                                EventEditActivity.this.minute2 = minute;
                            }
                        }, edc.get(Calendar.HOUR_OF_DAY), edc.get(Calendar.MINUTE), true).show();
            }
        });

        vEndDate = (Button) findViewById(R.id.act_publishevent_enddate);
        vEndDate.setText("" + year + "-" + (month2 + 1) + "-" + dayofmonth2);
        vEndDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EventEditActivity.this,
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                vEndDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                EventEditActivity.this.year2 = year;
                                month2 = monthOfYear;
                                dayofmonth2 = dayOfMonth;
                            }
                        }, edc.get(Calendar.YEAR), edc.get(Calendar.MONTH), edc.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //照片
        vPhoto = (GridView) findViewById(R.id.act_publishevent_photo);
        pa = new PhotoAdapter();
        vPhoto.setAdapter(pa);
        //发布按钮
        vPublish1 = (Button) findViewById(R.id.act_publishevent_pub1);
        vPublish1.setText("完成修改");
        
        vPublish1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //getStoreEvent();
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

                getStoreEvent();
            }
        });
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
    //省份，没用
//    private class ProvinceAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return provinceList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            View v = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, null);
//            TextView tv = (TextView) v.findViewById(android.R.id.text1);
//            tv.setText(provinceList.get(position).getName());
//            return v;
//        }
//
//    }
//
//    private class CityAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return cityList == null ? 0 : cityList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            View v = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, null);
//            TextView tv = (TextView) v.findViewById(android.R.id.text1);
//            tv.setText(cityList.get(position).getName());
//            return v;
//        }
//
//    }
//
//    private class DistrictAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return districtList == null ? 0 : districtList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            View v = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, null);
//            TextView tv = (TextView) v.findViewById(android.R.id.text1);
//            tv.setText(districtList.get(position).getName());
//            return v;
//        }
//    }

    private void getStoreEvent() {

    	for(DBEventCategory ec :currEvent.getCategories()){
    		DBHelper.getInstance(this).getEventCategoryDao().delete(ec);
    	}   
        
        currEvent.setAddress(selectLo.getAddrName());
        currEvent.setAddressdetail(selectLo.getAddrDetail());
        currEvent.setCity(selectLo.getAddrCity());
   //     currEvent.setDistrict(selectLo.getAddrDistrict());

        currEvent.setStarttime(new GregorianCalendar(year, month, dayofmonth, hour, minute).getTime().getTime());
        currEvent.setEndtime(new GregorianCalendar(year2, month2, dayofmonth2, hour2, minute2).getTime().getTime());

        Long userID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("curr_user_id", 0);
        String currUser =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("curr_user", "none");

        DBHelper.getInstance(this).getEventDao().update(currEvent);
        
    /*    for(DBCategory ca: categorySet) {
            DBEventCategory ec = new DBEventCategory();
            ec.setEventID(eventID);
            ec.setCategoryID(ca.getId());
            ec.setTimestamp(Calendar.getInstance().getTimeInMillis());
            DBHelper.getInstance(this).getEventCategoryDao().insert(ec);
        }*/
        
        currEvent.resetCategories();
        
        for(int i = lastimglen;i<bdPhotos.size();i++) {
            bdPhotos.get(i).setEventID(eventID);
            DBHelper.getInstance(this).getImageDao().insert(bdPhotos.get(i));
        }

        DBComment dc = new DBComment();
        dc.setUserID(userID);
        dc.setEventID(eventID);
        dc.setCommentType(1);
        dc.setComentContent(currUser + "修改了一个活动");
        dc.setUsername(currUser);
        dc.setTimestamp(System.currentTimeMillis());
        DBHelper.getInstance(this).getCommentDao().insert(dc);

        DBHelper.getInstance(this).getEventDao().update(currEvent);
//        int code=0;
//        NetAsyncTask task = new NetAsyncTask(vPublish1, code);
//        task.execute(currEvent);

        //	Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    class NetAsyncTask extends AsyncTask{

        private Button button;
        private int code;

        public NetAsyncTask(Button button, int code) {
            super();
            this.button = button;
            this.code = code;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            button.setText("正在发布。..");
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            code = Integer.parseInt(result.toString());
            if(code==5){
                button.setText("发布成功");
                finish();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
            System.out.println("doInBack");
            DBEvent curr = (DBEvent)params[0];
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return publishEvent(currEvent);
        }

        public int publishEvent(DBEvent currEvent){

            HttpClient hc=new DefaultHttpClient();
            hc.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
                    8000); // 超时设置
            hc.getParams().setIntParameter(
                    HttpConnectionParams.CONNECTION_TIMEOUT, 8000);// 连接超时
            HttpGet hg=new HttpGet("http://10.103.243.101:8080/Crj/kao.txt");
            try {
                HttpResponse hr=hc.execute(hg);
                int code=hr.getStatusLine().getStatusCode();
                if(code==200){
                    HttpEntity entity=hr.getEntity();
                    InputStream is=entity.getContent();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(is));

                    String line = reader.readLine();
                    System.out.println(line);

                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return 5;
        }
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
                                EventEditActivity.this.finish();
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
        
        if(resultCode == RESULT_OK && requestCode == 0) {
        	Uri imageUri = data.getData();
        	String imagePath=null;
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
            Bitmap pic = ImageUtil.decodeSampledBitmapFromPath(picPathTemp, reqWidth, reqWidth);
            if(pic != null) {
                eventPhotos.add(pic);
                pa.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "获取照片失败", Toast.LENGTH_SHORT).show();
            }
        } else if(resultCode == RESULT_OK && requestCode == 2) {
            long id = data.getLongExtra("location_id", 0);
            if(id == 0) {
                vAddrName.setText("data error");
                vAddrDetail.setText("data error");
            } else {
                selectLo = DBHelper.getInstance(this).getLocationDao().load(id);
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
        Cursor cursor = managedQuery(uri, projection, null, null, null);
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
                        new AlertDialog.Builder(EventEditActivity.this).setTitle("选择图片")
                                .setItems(new String[]{"从相册选取", "拍照"}, new DialogInterface.OnClickListener() {

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
                                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(AppConstant.localStorage + AppConstant.folderName + "/" + System.currentTimeMillis() + ".jpg");
                                                picPathTemp = sb.toString();
                                                picTemp = new DBImage();
                                                picTemp.setImageUrl(picPathTemp);
                                                picTemp.setTimestamp(System.currentTimeMillis());
                                                bdPhotos.add(picTemp);
                                                Uri imageUri = Uri.fromFile(new File(picPathTemp));
                                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                startActivityForResult(cameraIntent, 1);
                                            }
                                            default:
                                                break;
                                        }
                                    }
                                }).show();
                    }
                });
            } else {
                ImageView currImg = (ImageView) currView.findViewById(R.id.grid_photo_item);
                currImg.setImageBitmap(eventPhotos.get(position));
            }
            return currView;
        }

    }
}
