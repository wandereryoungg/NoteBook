package com.young.note.dao;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.young.note.db.ColumnConstants;
import com.young.note.db.DBCallback;
import com.young.note.entity.Event;

public class EventCallback implements DBCallback<Event> {
    @Override
    public Event cursorToInstance(Cursor cursor) {
        Event event = new Event();
        event.setmId(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
        event.setmTitle(cursor.getString(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_TITLE_COLUMN)));
        event.setmContent(cursor.getString(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_CONTENT_COLUMN)));
        event.setmCreateTime(cursor.getString(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_CREATED_TIME_COLUMN)));
        event.setmUpdatedTime(cursor.getString(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_UPDATED_TIME_COLUMN)));
        event.setmRemindTime(cursor.getString(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_REMIND_TIME_COLUMN)));
        event.setmIsImportant(cursor.getInt(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_IS_IMPORTANT_COLUMN)));
        event.setmIsClocked(cursor.getInt(cursor.getColumnIndexOrThrow(ColumnConstants.EVENT_IS_CLOCKED)));
        return event;
    }
}
