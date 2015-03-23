package com.alexan.findevents.friend;

import com.alexan.findevents.dao.DBComment;
import com.alexan.findevents.dao.DBEvent;

public class FCEntity {
	private DBEvent event;
	private DBComment comment;
	public DBEvent getEvent() {
		return event;
	}
	public void setEvent(DBEvent event) {
		this.event = event;
	}
	public DBComment getComment() {
		return comment;
	}
	public void setComment(DBComment comment) {
		this.comment = comment;
	}
	public FCEntity(DBEvent event, DBComment comment) {
		super();
		this.event = event;
		this.comment = comment;
	}
	
	public FCEntity() {
	}
}
