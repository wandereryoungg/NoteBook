package com.young.note;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.young.note.constants.Constants;
import com.young.note.entity.Event;
import com.young.note.manager.ClockManager;
import com.young.note.manager.EventManager;
import com.young.note.receiver.ClockReceiver;
import com.young.note.service.ClockService;
import com.young.note.util.AlertDialogUtil;
import com.young.note.util.DateTimeUtil;
import com.young.note.util.StringUtil;

import java.util.Calendar;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class EventDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_IS_EDIT_EVENT = "extra.is.edit.event";
    public static final String EXTRA_EVENT_DATA = "extra.event.data";
    public static final String EXTRA_IS_ADD_EVENT = "extra.is.create.event";

    private boolean isEditEvent;
    private boolean isAddEvent;
    private EventManager eventManager = EventManager.getInstance();
    private ClockManager clockManager = ClockManager.getInstance();
    private LinearLayout llUpdateTime;
    private TextView tvLastUpdateTime;
    private EditText editTitle;
    private EditText editRemindTime;
    private CheckBox checkBoxIsImportant;
    private EditText editContent;
    private ImageView ivBack;
    private TextView tvConfirm;
    private ImageView ivDelete;
    private ImageView ivEdit;
    private ScrollView scrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_detail);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        editRemindTime.setOnClickListener(this);
        scrollView.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        llUpdateTime = findViewById(R.id.ll_update_time);
        tvLastUpdateTime = findViewById(R.id.tv_last_update_time);
        editTitle = findViewById(R.id.edit_title);
        editRemindTime = findViewById(R.id.edit_remind_time);
        checkBoxIsImportant = findViewById(R.id.check_box_is_important);
        editContent = findViewById(R.id.edit_contnet);
        ivBack = findViewById(R.id.iv_back);
        tvConfirm = findViewById(R.id.tv_confirm);
        ivDelete = findViewById(R.id.iv_footer_delete);
        ivEdit = findViewById(R.id.iv_footer_edit);
        scrollView = findViewById(R.id.scroll_view);
        isEditEvent = getIntent().getBooleanExtra(EXTRA_IS_EDIT_EVENT, false);
        isAddEvent = getIntent().getBooleanExtra(EXTRA_IS_ADD_EVENT, false);
        judgeOperate();
    }

    private void judgeOperate() {
        llUpdateTime.setVisibility(isEditEvent ? View.VISIBLE : View.GONE);
        setEditTextReadOnly(editTitle, !isEditEvent && !isAddEvent);
        setEditTextReadOnly(editContent, !isAddEvent && !isEditEvent);
        setEditTextReadOnly(editRemindTime, true);
        editRemindTime.setClickable(isAddEvent || isEditEvent);
        tvConfirm.setVisibility(isEditEvent || isAddEvent ? View.VISIBLE : View.GONE);
        ivEdit.setVisibility(!isAddEvent && !isEditEvent ? View.VISIBLE : View.GONE);
        ivDelete.setVisibility(!isAddEvent ? View.VISIBLE : View.GONE);
        checkBoxIsImportant.setClickable(isAddEvent || isEditEvent);


    }

    @Override
    protected void initData() {
        if (!isAddEvent) {
            Event event = getIntent().getParcelableExtra(EXTRA_EVENT_DATA);
            tvLastUpdateTime.setText(event.getmUpdatedTime());
            editTitle.setText(event.getmTitle());
            editContent.setText(event.getmContent());
            editRemindTime.setText(event.getmRemindTime());
            checkBoxIsImportant.setChecked(event.getmIsImportant() == Constants.EventFlag.IMPORTANT);
        }
    }

    private void setEditTextReadOnly(EditText editText, boolean readOnly) {
        editText.setFocusable(!readOnly);
        editText.setFocusableInTouchMode(!readOnly);
        editText.setCursorVisible(!readOnly);
        editText.setTextColor(getResources().getColor(readOnly ? R.color.gray3 : R.color.black));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_footer_delete:
                if (!isAddEvent) {
                    AlertDialogUtil.showDialog(this, "你去确定要删除这个备忘录吗？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Event event = getIntent().getParcelableExtra(EXTRA_EVENT_DATA);
                            if (eventManager.removeEvent(event.getmId())) {
                                Toast.makeText(EventDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                clockManager.cancelAlarm(buildIntent(event.getmId()));
                                eventManager.flushData();
                                postToMainActivity();
                            } else {
                                Toast.makeText(EventDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.edit_remind_time:
                if (isEditEvent || isAddEvent) {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String time = year + "-" + StringUtil.getLocalMonth(month) + "-" + StringUtil.getMultiNumber(dayOfMonth) +
                                            " " + StringUtil.getMultiNumber(hourOfDay) + ":" + StringUtil.getMultiNumber(minute);
                                    editRemindTime.setText(time);
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                            timePickerDialog.show();
                        }

                        ;
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                    dialog.show();
                }
                break;
            case R.id.scroll_view:
                if (isAddEvent || isEditEvent) {
                    setEditTextReadOnly(editContent, false);
                }
                break;
            case R.id.tv_confirm:
                if (isAddEvent || isEditEvent) {
                    Event event = buildEvent();
                    if (!eventManager.checkEventField(event)) {
                        return;
                    }
                    if (eventManager.saveOrUpdate(event)) {
                        if (isEditEvent) {
                            Toast.makeText(NoteApplication.getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NoteApplication.getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            event.setmId(eventManager.getLatestEventId());
                        }
                        clockManager.addAlarm(buildIntent(event.getmId()), DateTimeUtil.strToDate(event.getmRemindTime()));
                        eventManager.flushData();
                        postToMainActivity();
                    } else {
                        if (isEditEvent) {
                            Toast.makeText(NoteApplication.getContext(), "更新失败", Toast.LENGTH_SHORT).show();
                        } else if (isAddEvent) {
                            Toast.makeText(NoteApplication.getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.iv_edit:
                if(!isEditEvent){
                    Toast.makeText(NoteApplication.getContext(),"你已进入编辑模式",Toast.LENGTH_SHORT).show();
                    ivEdit.setVisibility(View.GONE);
                    isEditEvent = true;
                    judgeOperate();
                }
        }
    }

    private Event buildEvent() {
        Event event = new Event();
        if (isEditEvent) {
            event.setmId(((Event) getIntent().getParcelableExtra(EXTRA_EVENT_DATA)).getmId());
            event.setmCreateTime(((Event) getIntent().getParcelableExtra(EXTRA_EVENT_DATA)).getmCreateTime());
        }
        event.setmRemindTime(editRemindTime.getText().toString());
        event.setmTitle(editTitle.getText().toString());
        event.setmIsImportant(checkBoxIsImportant.isChecked() ? Constants.EventFlag.IMPORTANT : Constants.EventFlag.NORMAL);
        event.setmContent(editContent.getText().toString());
        event.setmUpdatedTime(DateTimeUtil.dateToStr(new Date()));
        return event;
    }

    private void postToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private PendingIntent buildIntent(int id) {
        Intent intent = new Intent(this, ClockService.class);
        intent.putExtra(ClockReceiver.EXTRA_EVENT_ID, id);
        return PendingIntent.getService(this, 0x001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
