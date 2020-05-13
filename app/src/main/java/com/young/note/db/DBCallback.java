package com.young.note.db;

import android.database.Cursor;

public interface DBCallback<T> {
    T cursorToInstance(Cursor cursor);
}
