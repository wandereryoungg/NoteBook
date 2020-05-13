package com.young.note.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.young.note.NoteApplication;

import static com.young.note.db.ColumnConstants.EVENT_TABLE_NAME;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "note.db";
    private static final int VERSION = 1;

    public DBOpenHelper() {
        super(NoteApplication.getContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                EVENT_TABLE_NAME + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ColumnConstants.EVENT_TITLE_COLUMN + " text,"
                + ColumnConstants.EVENT_CONTENT_COLUMN + " text,"
                + ColumnConstants.EVENT_CREATED_TIME_COLUMN + " datetime,"
                + ColumnConstants.EVENT_UPDATED_TIME_COLUMN + " datetime,"
                + ColumnConstants.EVENT_REMIND_TIME_COLUMN + " datetime,"
                + ColumnConstants.EVENT_IS_IMPORTANT_COLUMN + " INTEGER,"
                + ColumnConstants.EVENT_IS_CLOCKED + " INTEGER"
                + ")");
        String sql = "INSERT INTO " + EVENT_TABLE_NAME + " VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";
        db.beginTransaction();
        db.execSQL(sql, new Object[]{
                "young note",
                "Note是一个有闹钟提示的记事本",
                "2020-05-07 13:36",
                "2020-05-07 13:36",
                "2020-05-07 13:36",
                0, 0
        });
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
