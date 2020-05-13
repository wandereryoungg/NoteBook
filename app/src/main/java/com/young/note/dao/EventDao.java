package com.young.note.dao;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.young.note.db.ColumnConstants;
import com.young.note.db.DBTemplate;
import com.young.note.entity.Event;
import com.young.note.util.DateTimeUtil;

import java.util.Date;
import java.util.List;

public class EventDao {

    private DBTemplate<Event> mTemplate = new DBTemplate<>();
    private EventCallback mCallback = new EventCallback();
    private static EventDao mEventDao = new EventDao();

    public EventDao() {
    }

    public static EventDao getInstance() {
        return mEventDao;
    }

    public List<Event> findAll() {
        String sql = "SELECT * FROM " + ColumnConstants.EVENT_TABLE_NAME + " ORDER BY " + ColumnConstants.EVENT_IS_IMPORTANT_COLUMN + " DESC, " + ColumnConstants.EVENT_CREATED_TIME_COLUMN + " DESC";
        return mTemplate.query(sql, mCallback);
    }

    public Event findById(Integer id) {
        String sql = "SELECT * FROM " + ColumnConstants.EVENT_TABLE_NAME + " WHERE " + BaseColumns._ID + "=?";
        return mTemplate.queryOne(sql, mCallback, Integer.toString(id));
    }

    public int remove(List<Integer> ids) {
        StringBuilder whereConditions = new StringBuilder(BaseColumns._ID + " IN(");
        for (Integer id : ids) {
            whereConditions.append(id).append(",");
        }
        whereConditions.deleteCharAt(whereConditions.length() - 1).append(")");
        return mTemplate.remove(ColumnConstants.EVENT_TABLE_NAME, whereConditions.toString());
    }

    public int create(Event event) {
        return (int) mTemplate.create(ColumnConstants.EVENT_TABLE_NAME, generateContentValues(event, false));
    }

    public int update(Event event) {
        return mTemplate.update(ColumnConstants.EVENT_TABLE_NAME, generateContentValues(event, true),
                BaseColumns._ID + "=?", Integer.toString(event.getmId()));
    }

    public int getLatestEventId() {
        return mTemplate.getLatestId(ColumnConstants.EVENT_TABLE_NAME);
    }


    private ContentValues generateContentValues(Event event, boolean isUpdate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnConstants.EVENT_TITLE_COLUMN, event.getmTitle());
        contentValues.put(ColumnConstants.EVENT_CONTENT_COLUMN, event.getmContent());
        if (!isUpdate) {
            contentValues.put(ColumnConstants.EVENT_CREATED_TIME_COLUMN, DateTimeUtil.dateToStr(new Date()));
        } else {
            contentValues.put(ColumnConstants.EVENT_CREATED_TIME_COLUMN, event.getmCreateTime());
        }
        contentValues.put(ColumnConstants.EVENT_IS_CLOCKED, event.getmIsClocked());
        contentValues.put(ColumnConstants.EVENT_UPDATED_TIME_COLUMN, DateTimeUtil.dateToStr(new Date()));
        contentValues.put(ColumnConstants.EVENT_REMIND_TIME_COLUMN, event.getmRemindTime());
        contentValues.put(ColumnConstants.EVENT_IS_IMPORTANT_COLUMN, event.getmIsImportant());
        return contentValues;
    }


}
