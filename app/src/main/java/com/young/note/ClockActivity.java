package com.young.note;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.young.note.entity.Event;
import com.young.note.service.ClockService;
import com.young.note.util.AlertDialogUtil;

public class ClockActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_clock);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        clock();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        clock();
    }

    private void clock() {
        mediaPlayer.start();
        long[] pattern = new long[]{1500, 1000};
        vibrator.vibrate(pattern, 0);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_alarm_layout, null);
        TextView textView = view.findViewById(R.id.tv_event);
        textView.setText("待办事项: " + event.getmTitle());
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        final AlertDialog alertDialog = AlertDialogUtil.showDialog(this, view);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                vibrator.cancel();
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.clock);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        event = getIntent().getParcelableExtra(ClockService.EXTRA_EVENT);
        if (event == null) {
            finish();
        }
    }

}
