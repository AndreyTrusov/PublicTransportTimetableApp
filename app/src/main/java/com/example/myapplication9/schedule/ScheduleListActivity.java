package com.example.myapplication9.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.ScheduleDAO;
import com.example.myapplication9.model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ScheduleListActivity extends AppCompatActivity implements ScheduleAdapter.ScheduleItemClickListener {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private ScheduleDAO scheduleDAO;
    private List<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        setTitle(R.string.schedules);

        scheduleDAO = new ScheduleDAO(this);

        recyclerView = findViewById(R.id.recycler_view_schedules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_schedule);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ScheduleListActivity.this, ScheduleEditActivity.class);
            startActivity(intent);
        });

        loadSchedules();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSchedules();
    }

    private void loadSchedules() {
        scheduleDAO.open();
        scheduleList = scheduleDAO.getAllSchedules();
        scheduleDAO.close();

        adapter = new ScheduleAdapter(this, scheduleList, this);
        recyclerView.setAdapter(adapter);

        View emptyView = findViewById(R.id.empty_view);
        if (scheduleList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScheduleClick(int position) {
        Schedule schedule = scheduleList.get(position);
        Intent intent = new Intent(ScheduleListActivity.this, ScheduleEditActivity.class);
        intent.putExtra("schedule_id", schedule.getId());
        startActivity(intent);
    }

    @Override
    public void onScheduleDeleteClick(int position) {
        Schedule schedule = scheduleList.get(position);
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    scheduleDAO.open();
                    boolean deleted = scheduleDAO.deleteSchedule(schedule.getId());
                    scheduleDAO.close();

                    if (deleted) {
                        scheduleList.remove(position);
                        adapter.notifyItemRemoved(position);

                        if (scheduleList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(ScheduleListActivity.this,
                                "Schedule deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
