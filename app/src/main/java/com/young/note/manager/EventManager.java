package com.young.note.manager;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.young.note.NoteApplication;
import com.young.note.constants.Constants;
import com.young.note.dao.EventDao;
import com.young.note.entity.Event;
import com.young.note.exception.YoungException;
import com.young.note.util.DateTimeUtil;
import com.young.note.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventManager {
    public static final String TAG = "EventManager";
    private static EventManager mEventManager = new EventManager();
    private EventDao eventDao = EventDao.getInstance();

    private List<Event> events = new ArrayList<>();
    private List<Integer> deletedIds = new ArrayList<>();

    private EventManager() {
    }

    public static EventManager getInstance() {
        return mEventManager;
    }

    public void setDeletedIds(List<Integer> ids) {
        this.deletedIds = ids;
    }

    public List<Event> findAll() {
        return eventDao.findAll();
    }

    public boolean removeEvent(Integer id) {
        return eventDao.remove(Collections.singletonList(id)) != 0;
    }

    public void removeEvents(final Handler handler, final List<Integer> ids) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int result = eventDao.remove(ids);
                    Message message = new Message();
                    message.what = Constants.HANDLER_SUCCESS;
                    message.obj = result;
                    message.setTarget(handler);
                    message.sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.obtainMessage(Constants.HANDLER_FAILED, new YoungException(e)).sendToTarget();
                }
            }
        }).start();
    }

    public void flushData() {
        events = eventDao.findAll();
    }

    public boolean checkEventField(Event event) {
        if (event == null) {
            return false;
        }
        if (StringUtil.isBlank(event.getmTitle())) {
            Toast.makeText(NoteApplication.getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isBlank(event.getmContent())) {
            Toast.makeText(NoteApplication.getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isBlank(event.getmRemindTime())) {
            Toast.makeText(NoteApplication.getContext(), "提醒时间不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (DateTimeUtil.strToDate(event.getmRemindTime()) == null) {
            Toast.makeText(NoteApplication.getContext(), "提醒时间格式错误，请重新选择", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (new Date().getTime() > DateTimeUtil.strToDate(event.getmRemindTime()).getTime()) {
            Toast.makeText(NoteApplication.getContext(), "提醒时间无效，请重新选择", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean saveOrUpdate(Event event) {
        try {
            if (event.getmId() != null) {
                eventDao.update(event);
            } else {
                eventDao.create(event);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getLatestEventId() {
        return eventDao.getLatestEventId();
    }


}
