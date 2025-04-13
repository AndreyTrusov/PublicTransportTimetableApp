package com.example.myapplication9.stop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.StopDAO;
import com.example.myapplication9.model.Stop;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class StopListActivity extends AppCompatActivity implements StopAdapter.StopItemClickListener {

    private RecyclerView recyclerView;
    private StopAdapter adapter;
    private StopDAO stopDAO;
    private List<Stop> stopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);

        setTitle(R.string.stops);

        stopDAO = new StopDAO(this);

        recyclerView = findViewById(R.id.recycler_view_stops);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_stop);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(StopListActivity.this, StopEditActivity.class);
            startActivity(intent);
        });

        loadStops();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStops();
    }

    private void loadStops() {
        stopDAO.open();
        stopList = stopDAO.getAllStops();
        stopDAO.close();

        adapter = new StopAdapter(this, stopList, this);
        recyclerView.setAdapter(adapter);

        View emptyView = findViewById(R.id.empty_view);
        if (stopList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStopClick(int position) {
        Stop stop = stopList.get(position);
        Intent intent = new Intent(StopListActivity.this, StopEditActivity.class);
        intent.putExtra("stop_id", stop.getId());
        startActivity(intent);
    }

    @Override
    public void onStopDeleteClick(int position) {
        Stop stop = stopList.get(position);
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_stop_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    stopDAO.open();
                    boolean deleted = stopDAO.deleteStop(stop.getId());
                    stopDAO.close();

                    if (deleted) {
                        stopList.remove(position);
                        adapter.notifyItemRemoved(position);

                        if (stopList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(StopListActivity.this,
                                "Stop " + stop.getName() + " deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}

