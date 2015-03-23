package com.alexan.findevents.event;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.util.DBHelper;
import com.alexan.findevents.dao.DBCategory;

public class CategoryCBAdapter extends BaseAdapter {
	
	private Activity mCtx;
	private List<DBCategory> caList, categorySet;
	private CategorySelectListener mcsl;

	public CategoryCBAdapter(Activity ctx, CategorySelectListener csl, List<DBCategory> categorySet) {
		this.mCtx = ctx;
		this.mcsl = csl;
        this.categorySet = categorySet;
		caList = DBHelper.getInstance(mCtx).getCategoryDao().loadAll();
		caList.remove(0);
		if(caList == null) {
			caList = new ArrayList<DBCategory>();
		}
		//System.out.println("asdlfjasd");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return caList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mCtx.getLayoutInflater().inflate(R.layout.grid_checkbox_item, null);
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.grid_checkbox);
		cb.setText(caList.get(position).getName());
        if(categorySet!=null){
            if(categorySet.contains(caList.get(position))){
                cb.setChecked(true);
            }
        }
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mcsl.setSelectedCategory(caList.get(position), arg1);
			}
		});
		return convertView;
	}

}
