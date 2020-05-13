package com.young.note.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DBTemplate<T> {

    public DBOpenHelper dbOpenHelper;

    public DBTemplate() {
        this.dbOpenHelper = new DBOpenHelper();
    }

    public T queryOne(String sql, DBCallback<T> callback, String... args) {
        T t = null;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        if (cursor != null && cursor.moveToNext()) {
            t = callback.cursorToInstance(cursor);
            cursor.close();
        }
        return t;
    }

    public List<T> query(String sql, DBCallback<T> callback, String... args) {
        List<T> list = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                T t = callback.cursorToInstance(cursor);
                list.add(t);
            }
            cursor.close();
        }
        return list;
    }

    public long create(String table, ContentValues values) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        return db.insert(table, null, values);
    }

    public int remove(String table, String whereConditions, String... args) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        return db.delete(table, whereConditions, args);
    }

    public int getLatestId(String table) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "SELECT MAX(" + BaseColumns._ID + ") FROM" + table;
        Cursor cursor = db.rawQuery(sql, new String[]{});
        int result = -1;
        if (cursor != null && cursor.moveToNext()) {
            result = cursor.getInt(0);
            cursor.close();
        }
        return result;
    }

    public int update(String table, ContentValues contentValues, String whereConditions, String... args) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        return db.update(table, contentValues, whereConditions, args);
    }
}
