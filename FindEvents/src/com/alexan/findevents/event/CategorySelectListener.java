package com.alexan.findevents.event;

import com.alexan.findevents.dao.DBCategory;

public interface CategorySelectListener {
	public void setSelectedCategory(String category, boolean checked);
}
