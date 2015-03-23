package com.alexan.findevents.friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.alexan.findevents.R;
import com.alexan.findevents.util.CharacterParser;
import com.alexan.findevents.util.PinyinComparator;
import com.alexan.findevents.util.SortModel;
import com.alexan.findevents.view.SideBar;
import com.alexan.findevents.view.SideBar.OnTouchingLetterChangedListener;

public class CircleListActivity extends SherlockActivity {
	private EditText vSearch;
	private Button vSearchBtn;
	private ListView vList;
	private SideBar vSidebar;
	private TextView vIndicator;
	
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SortAdapter sa;
	private List<SortModel> dataList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("Ⱥ���б�");
		setContentView(R.layout.activity_circlelist);
		initView();
	}

	private void initView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vSearch = (EditText) findViewById(R.id.act_circlelist_searchet);
		vSearchBtn = (Button) findViewById(R.id.act_circlelist_searchbtn);
		vSearchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		vList = (ListView) findViewById(R.id.act_circlelist_list);

		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		vSidebar = (SideBar) findViewById(R.id.act_circlelist_sidebar);
		vIndicator = (TextView) findViewById(R.id.act_circlelist_indicator);
		vSidebar.setTextView(vIndicator);

		// �����Ҳഥ������
		vSidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = sa.getPositionForSection(s.charAt(0));
				if (position != -1) {
					vList.setSelection(position);
				}

			}
		});
		
		vList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition���Ӧ�Ķ���
				Intent i = new Intent(CircleListActivity.this, FriendInfoActivity.class);
				startActivity(i);
			}
		});
		
		dataList = filledData(getResources().getStringArray(R.array.circle));
		
		// ���a-z��������Դ���
		Collections.sort(dataList, pinyinComparator);
		sa = new SortAdapter(this, dataList);
		vList.setAdapter(sa);
	}
	
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * ���������е�ֵ��������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = dataList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : dataList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// ���a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		sa.updateListView(filterDateList);
	}
}
