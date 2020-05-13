package com.young.note.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.young.note.ClockActivity;
import com.young.note.dao.EventDao;
import com.young.note.entity.Event;
import com.young.note.util.WakeLockUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClockService extends Service {

    public static final String EXTRA_EVENT_ID = "extra.event.id";
    public static final String EXTRA_EVENT_REMIND_TIME = "extra.event.remind.time";
    public static final String EXTRA_EVENT = "extra.event";
    private EventDao eventDao = EventDao.getInstance();

    public ClockService() {
        super();
        Log.e("young", "ClockService Construction");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not ready yet");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WakeLockUtil.wakeUpAndUnlock();
        postToClockActivity(getApplicationContext(), intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void postToClockActivity(Context context, Intent intent) {
        Intent i = new Intent(context, ClockActivity.class);
        i.putExtra(EXTRA_EVENT_ID, intent.getIntExtra(EXTRA_EVENT_ID, -1));
        Event event = eventDao.findById(intent.getIntExtra(EXTRA_EVENT_ID, -1));
        if (event == null) {
            return;
        }
        i.putExtra(EXTRA_EVENT_REMIND_TIME, intent.getStringExtra(EXTRA_EVENT_REMIND_TIME));
        i.putExtra(EXTRA_EVENT, event);
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
