package com.young.note.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AppUtil {
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert context != null;
        assert view != null;
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
