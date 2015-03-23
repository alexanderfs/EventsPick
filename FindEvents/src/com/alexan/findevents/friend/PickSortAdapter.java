package com.alexan.findevents.friend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.alexan.findevents.R;
import com.alexan.findevents.me.GroupAddActivity;
import com.alexan.findevents.me.GroupEditActivity;
import com.alexan.findevents.util.SortModel;

public class PickSortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private List<Long> idlist = null;
	private Context mContext;
	
	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		CheckBox cbSelect;
	}
	
	public PickSortAdapter(Context mContext, List<SortModel> list, List<Long> idlist) {
		this.mContext = mContext;
		this.list = list;
		this.idlist = idlist;
	}
	
	/**
	 * 当ListView数据发生变化�?调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.list_pick_contactitem, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.list_pickitem_name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.list_pickitem_category);
			viewHolder.cbSelect = (CheckBox) view.findViewById(R.id.list_pickitem_check);
			
			view.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.cbSelect.setOnCheckedChangeListener(null);
	
		viewHolder.tvTitle.setText(mContent.getName());
		viewHolder.cbSelect.setChecked(mContent.isSelected());
		
		if(mContext instanceof GroupEditActivity){
			if(idlist.contains(mContent.getId())){
				viewHolder.cbSelect.setChecked(true);
			}
			else
				viewHolder.cbSelect.setChecked(false);
		}
		
		viewHolder.cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mContent.setSelected(true);
					idlist.add(mContent.getId());
					Toast.makeText(mContext, "选中了的id为"+mContent.getId(), 500).show();
				}
				else{
					mContent.setSelected(false);
					idlist.remove(mContent.getId());
					Toast.makeText(mContext, "取消了的id为"+mContent.getId(), 500).show();
				}
				
			}
		});
		//根据position获取分类的首字母的Char ascii�?
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位�?，则认为是第�?��出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		
		return view;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii�?
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母�?代替�?
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}