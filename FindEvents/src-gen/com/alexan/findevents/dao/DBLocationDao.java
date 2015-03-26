package com.alexan.findevents.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.alexan.findevents.dao.DBLocation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DBLOCATION.
*/
public class DBLocationDao extends AbstractDao<DBLocation, Long> {

    public static final String TABLENAME = "DBLOCATION";

    /**
     * Properties of entity DBLocation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AddrName = new Property(1, String.class, "addrName", false, "ADDR_NAME");
        public final static Property AddrDetail = new Property(2, String.class, "addrDetail", false, "ADDR_DETAIL");
        public final static Property AddrCity = new Property(3, String.class, "addrCity", false, "ADDR_CITY");
        public final static Property AddrProvince = new Property(4, String.class, "addrProvince", false, "ADDR_PROVINCE");
        public final static Property Timestamp = new Property(5, Long.class, "timestamp", false, "TIMESTAMP");
    };


    public DBLocationDao(DaoConfig config) {
        super(config);
    }
    
    public DBLocationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DBLOCATION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ADDR_NAME' TEXT," + // 1: addrName
                "'ADDR_DETAIL' TEXT," + // 2: addrDetail
                "'ADDR_CITY' TEXT," + // 3: addrCity
                "'ADDR_PROVINCE' TEXT," + // 4: addrProvince
                "'TIMESTAMP' INTEGER);"); // 5: timestamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DBLOCATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBLocation entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String addrName = entity.getAddrName();
        if (addrName != null) {
            stmt.bindString(2, addrName);
        }
 
        String addrDetail = entity.getAddrDetail();
        if (addrDetail != null) {
            stmt.bindString(3, addrDetail);
        }
 
        String addrCity = entity.getAddrCity();
        if (addrCity != null) {
            stmt.bindString(4, addrCity);
        }
 
        String addrProvince = entity.getAddrProvince();
        if (addrProvince != null) {
            stmt.bindString(5, addrProvince);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(6, timestamp);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBLocation readEntity(Cursor cursor, int offset) {
        DBLocation entity = new DBLocation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // addrName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // addrDetail
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // addrCity
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // addrProvince
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // timestamp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBLocation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAddrName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAddrDetail(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddrCity(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAddrProvince(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTimestamp(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBLocation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBLocation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
