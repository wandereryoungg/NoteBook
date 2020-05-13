package com.young.note.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.young.note.ClockActivity;
import com.young.note.dao.EventDao;
import com.young.note.entity.Event;
import com.young.note.util.WakeLockUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClockReceiver extends BroadcastReceiver {

    public static final String EXTRA_EVENT_ID = "extra.event.id";
    public static final String EXTRA_EVENT_REMIND_TIME = "extra.event.remind.time";
    public static final String EXTRA_EVENT = "extra.event";
    private EventDao eventDao = EventDao.getInstance();

    public ClockReceiver() {
        super();
        Log.e("young","ClockReceiver construction");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLockUtil.wakeUpAndUnlock();
        postToClockActivity(context, intent);
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
        context.startActivity(i);
    }
}
