package com.alexan.findevents.friend;

import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBPickEvent;

public class FCEntity {
	private DBPickEvent event;
	private DBEvent oldevent;
	private DBComment comment;
	public DBPickEvent getEvent() {
		return event;
	}
	public void setEvent(DBPickEvent event) {
		this.event = event;
	}
	public DBComment getComment() {
		return comment;
	}
	public void setComment(DBComment comment) {
		this.comment = comment;
	}
	public FCEntity(DBPickEvent event, DBComment comment) {
		super();
		this.event = event;
		this.comment = comment;
	}
	
	public FCEntity(DBEvent event, DBComment comment) {
		super();
		this.oldevent = event;
		this.comment = comment;
	}
	
	public FCEntity() {
	}
}
