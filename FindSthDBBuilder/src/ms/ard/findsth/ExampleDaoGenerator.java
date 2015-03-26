/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ms.ard.findsth;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.alexan.findevents.dao");

        addImage(schema);
        addUser(schema);
        addPerson(schema);
        addFavCategory(schema);
        addEvent(schema);
        addPickEvent(schema);
        addLocation(schema);
        addComment(schema);
        addHotEvent(schema);
        addRTEvent(schema);
        addCategoryEvent(schema);
        addSearchResultEvent(schema);
        addFriendNewsEvent(schema);
        addEventMessage(schema);
        addFriendMessage(schema);
        addTimeRecorder(schema);
        new DaoGenerator().generateAll(schema, "../FindEvents/src-gen");
    }
    
    private static void addUser(Schema schema) {
    	Entity user = schema.addEntity("DBUser");
    	user.addIdProperty();
    	user.addLongProperty("userID");
    	user.addStringProperty("phoneNumber");
    	user.addStringProperty("emailAddr");
    	user.addStringProperty("username");
    	user.addBooleanProperty("gender");
    	user.addStringProperty("signature");
    	user.addLongProperty("timestamp");
    }
    
    private static void addPerson(Schema schema) {
    	Entity person = schema.addEntity("DBPerson");
    	person.addIdProperty();
    	//person.addStringProperty("userID");
    	person.addStringProperty("phoneNumber");
    	person.addStringProperty("emailAddr");
    	person.addStringProperty("nickname");
    	person.addLongProperty("locationID");
    	person.addBooleanProperty("gender");
    	person.addStringProperty("signature");
    	person.addLongProperty("icon");
    	person.addLongProperty("timestamp");
    	
    	Entity friend = schema.addEntity("DBFriend");
    	friend.addIdProperty().primaryKey().autoincrement();
    	friend.addLongProperty("userID");
    	//friend.addLongProperty("friendID");
    	Property friendID = friend.addLongProperty("friendID").notNull().getProperty();
    	friend.addStringProperty("friendname");
    	friend.addIntProperty("friendType");
    	friend.addLongProperty("timestamp");
    	friend.addToOne(person, friendID, "person");
    	
    	Entity group = schema.addEntity("DBGroup");
    	group.addIdProperty().primaryKey().autoincrement();
    	group.addLongProperty("userID");
    	group.addStringProperty("groupname");
    	group.addIntProperty("groupcapacity");
    	group.addLongProperty("timestamp");
    	
    	Entity groupfriend = schema.addEntity("DBGroupFriend");
    	groupfriend.addIdProperty().primaryKey().autoincrement();
    	Property thegroupID = groupfriend.addLongProperty("groupID").notNull().getProperty();
    	Property thefriendID = groupfriend.addLongProperty("friendID").notNull().getProperty();
    	groupfriend.addToOne(person, thefriendID, "friends");
    	groupfriend.addToOne(group, thegroupID, "groups");
    	
    	ToMany getFriend = person.addToMany(friend, friendID, "whosefriend");
    	ToMany getFriends = person.addToMany(groupfriend, thefriendID, "thegroups");
    	ToMany getGroups = group.addToMany(groupfriend, thegroupID, "thefriends");
    }
    
    private static void addFavCategory(Schema schema) {
    	Entity favCategory = schema.addEntity("DBFavCategory");
    	favCategory.addIdProperty();
    	favCategory.addLongProperty("personID");
    	favCategory.addLongProperty("categoryID");
    	favCategory.addLongProperty("timestamp");
    }
       
    private static void addImage(Schema schema) {
    	Entity image = schema.addEntity("DBImage");
    	image.addIdProperty();
    	image.addStringProperty("imageUrl");
    	image.addIntProperty("cacheLevel");
    	image.addLongProperty("referBigImageID");
    	image.addLongProperty("timestamp");
    	image.addLongProperty("eventID");
    	image.addLongProperty("personID");
    }
    
    private static void addPickEvent(Schema schema){
    	Entity pickEvent = schema.addEntity("DBPickEvent");
    	pickEvent.addIdProperty();
    	pickEvent.addLongProperty("userID");
    	
    	pickEvent.addStringProperty("title");
    	pickEvent.addStringProperty("description");
    	pickEvent.addStringProperty("address");
    	pickEvent.addStringProperty("province");
    	pickEvent.addStringProperty("city");
    	pickEvent.addStringProperty("addressdetail");
    	
    	pickEvent.addStringProperty("photo");
    	
    	pickEvent.addStringProperty("startt");
    	pickEvent.addStringProperty("endt");
    	pickEvent.addStringProperty("startd");
    	pickEvent.addStringProperty("endd");
    	
    	pickEvent.addIntProperty("collectionNum");
    	pickEvent.addIntProperty("attendNum");
    	pickEvent.addIntProperty("commentNum");
    	
    	pickEvent.addStringProperty("catagory");
    }
    
    private static void addEvent(Schema schema) {
    	Entity event = schema.addEntity("DBEvent");
    	event.addIdProperty();
    	event.addLongProperty("userID");
    	event.addLongProperty("locationID");
    	event.addStringProperty("title");
    	event.addStringProperty("description");
    	event.addStringProperty("address");
    	event.addStringProperty("province");
    	event.addStringProperty("city");
    	event.addStringProperty("district");
    	event.addStringProperty("addressdetail");
    	//ÐÞ¸Ä
    	event.addStringProperty("startt");
    	event.addStringProperty("endt");
    	event.addStringProperty("startd");
    	event.addStringProperty("endd");
    	//ÐÞ¸Ä
    	
    	event.addIntProperty("collectionNum");
    	event.addIntProperty("attendNum");
    	event.addIntProperty("commentNum");
    	event.addLongProperty("timestamp");
    	event.addLongProperty("visibility");
    	
    	event.addLongProperty("starttime");
    	event.addLongProperty("endtime");

        Entity category = schema.addEntity("DBCategory");
        category.addIdProperty();
        category.addStringProperty("name");
        category.addLongProperty("imageUrl");
        category.addLongProperty("timestamp");
    	
    	Entity eventCategory = schema.addEntity("DBEventCategory");
    	eventCategory.addIdProperty();
    	Property eID = eventCategory.addLongProperty("eventID").getProperty();
    	eventCategory.addToOne(event, eID);
    	//eventCategory.addLongProperty("categoryID");
        Property cID = eventCategory.addLongProperty("categoryID").getProperty();
        eventCategory.addToOne(category, cID);
    	eventCategory.addLongProperty("timestamp");
    	
    	ToMany eCategories = event.addToMany(eventCategory, eID);
    	eCategories.setName("categories");
    	
    }
    
    private static void addComment(Schema schema) {
    	Entity comment = schema.addEntity("DBComment");
    	comment.addIdProperty();
    	comment.addLongProperty("eventID");
    	comment.addLongProperty("userID");
    	comment.addStringProperty("username");
    	comment.addLongProperty("targetUserID");
    	comment.addLongProperty("targetCommentID");
    	comment.addStringProperty("comentContent");
    	comment.addIntProperty("commentType");
    	comment.addIntProperty("visibleScope");
    	comment.addLongProperty("timestamp");
    }
    
    private static void addLocation(Schema schema) {
    	Entity location = schema.addEntity("DBLocation");
    	location.addIdProperty();
    	location.addStringProperty("addrName");
    	location.addStringProperty("addrDetail");
    	location.addStringProperty("addrCity");
    	location.addStringProperty("addrProvince");
    	location.addLongProperty("timestamp");
    	
    	
    	Entity province = schema.addEntity("DBProvince");
    	province.addIdProperty();
    	province.addStringProperty("name").notNull();
    	province.addLongProperty("timestamp");
    	
    	Entity city = schema.addEntity("DBCity");
    	city.addIdProperty();
    	city.addStringProperty("name").notNull();
    	city.addLongProperty("timestamp");
    	Property proId = city.addLongProperty("province_id").notNull().getProperty();
    	city.addToOne(province, proId);
    
    	ToMany proCities = province.addToMany(city, proId);
    	proCities.setName("proCities");
    	
    }
    
    
    private static void addHotEvent(Schema schema) {
    	Entity hotEvent = schema.addEntity("DBHotEvent");
    	hotEvent.addIdProperty();
    	hotEvent.addLongProperty("eventID");
    	hotEvent.addLongProperty("timestamp");
    }
    
    private static void addRTEvent(Schema schema) {
    	Entity rtEvent = schema.addEntity("DBRTEvent");
    	rtEvent.addIdProperty();
    	rtEvent.addLongProperty("eventID");
    	rtEvent.addLongProperty("timestamp");
    }
    
    private static void addCategoryEvent(Schema schema) {
    	Entity categoryEvent = schema.addEntity("DBCategoryEvent");
    	categoryEvent.addIdProperty();
    	categoryEvent.addLongProperty("eventID");
    	categoryEvent.addLongProperty("timestamp");
    }
    
    private static void addSearchResultEvent(Schema schema) {
    	Entity searchResultTable = schema.addEntity("DBSearchResultEvent");
    	searchResultTable.addIdProperty();
    	searchResultTable.addLongProperty("eventID");
    	searchResultTable.addLongProperty("timestamp");
    }
    
    private static void addFriendNewsEvent(Schema schema) {
    	Entity friendEvent = schema.addEntity("DBFriendEvent");
    	friendEvent.addIdProperty();
    	friendEvent.addLongProperty("eventID");
    	friendEvent.addLongProperty("commentID");
    	friendEvent.addLongProperty("timestamp");
    }
    
    private static void addEventMessage(Schema schema) {
    	Entity eventMessage = schema.addEntity("DBEventMessage");
    	eventMessage.addIdProperty();
    	eventMessage.addLongProperty("commentID");
    	eventMessage.addLongProperty("timestamp");
    }
    
    private static void addFriendMessage(Schema schema) {
    	Entity friendMessage = schema.addEntity("DBFriendMessage");
    	friendMessage.addIdProperty();
    	friendMessage.addLongProperty("friendID");
    	friendMessage.addLongProperty("timestamp");
    }
    
    private static void addTimeRecorder(Schema schema) {
    	Entity timeRecorder = schema.addEntity("DBTimeRecorder");
    	timeRecorder.addIdProperty();
    	timeRecorder.addStringProperty("tableName");
    	timeRecorder.addLongProperty("lastModifyTime");
    }
    
}
