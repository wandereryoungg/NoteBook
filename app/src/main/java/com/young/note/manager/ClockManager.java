package com.young.note.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.young.note.NoteApplication;

import java.util.Date;

public class ClockManager {

    private static ClockManager instance = new ClockManager();

    private ClockManager() {
    }

    public static ClockManager getInstance() {
        return instance;
    }

    private static AlarmManager getAlarmManager() {
        return (AlarmManager) NoteApplication.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void cancelAlarm(PendingIntent pendingIntent) {
        getAlarmManager().cancel(pendingIntent);
    }

    public void addAlarm(PendingIntent pendingIntent, Date performTime) {
        cancelAlarm(pendingIntent);
        getAlarmManager().set(AlarmManager.RTC_WAKEUP, performTime.getTime(), pendingIntent);
    }

}
