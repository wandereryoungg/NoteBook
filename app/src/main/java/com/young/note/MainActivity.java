package com.young.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.young.note.entity.Event;
import com.young.note.manager.ClockManager;
import com.young.note.manager.EventManager;
import com.young.note.util.AlertDialogUtil;
import com.young.note.util.AppUtil;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    private MyEventAdapter myEventAdapter;
    private BaseHandler mBaseHandler = new BaseHandler(this);
    private EventManager eventManager = EventManager.getInstance();
    private ClockManager clockManager = ClockManager.getInstance();
    private DialogInterface.OnClickListener mConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            eventManager.setDeletedIds(myEventAdapter.getmSelectedEventIds());
            eventManager.removeEvents(mBaseHandler, myEventAdapter.getmSelectedEventIds());
            myEventAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "需要读写SD卡权限", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    @Override
    protected void setListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra(EventDetailActivity.EXTRA_IS_ADD_EVENT, true);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_delete) {
            if (myEventAdapter.getmIsDeleteMode()) {
                if (myEventAdapter.getmSelectedEventIds().size() == 0) {
                    Toast.makeText(NoteApplication.getContext(), "请至少选择一个再操作", Toast.LENGTH_SHORT).show();
                } else {
                    String msg = myEventAdapter.getmSelectedEventIds().size() == 1 ? "你确定要删除这个备忘录吗" : "你确定要删除这些备忘录吗";
                    AlertDialogUtil.showDialog(this, msg, mConfirmListener);
                }
            } else {
                myEventAdapter.setmIsDeleteMode(true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search_view);
        myEventAdapter = new MyEventAdapter(this);
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) searchView.getLayoutParams();
        params.bottomMargin = -3;
        searchView.setLayoutParams(params);
        searchView.onActionViewExpanded();
        initSearchView();
    }

    private void initSearchView() {
        searchView.clearFocus();
        Class<? extends SearchView> clazz = searchView.getClass();
        try {
            Field mSearchPlate = clazz.getDeclaredField("mSearchPlate");
            mSearchPlate.setAccessible(true);
            View view = (View) mSearchPlate.get(searchView);
            view.setBackgroundColor(getResources().getColor(R.color.transparent));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        AppUtil.hideSoftInput(this, searchView);
    }

    @Override
    protected void initData() {
        myEventAdapter.setmDatabases(eventManager.findAll());
        recyclerView.setAdapter(myEventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
