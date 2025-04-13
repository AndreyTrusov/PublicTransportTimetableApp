package com.example.myapplication9.line;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.LineDAO;
import com.example.myapplication9.model.Line;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LineListActivity extends AppCompatActivity implements LineAdapter.LineItemClickListener {

    private RecyclerView recyclerView;
    private LineAdapter adapter;
    private LineDAO lineDAO;
    private List<Line> lineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_list);

        setTitle(R.string.lines);

        lineDAO = new LineDAO(this);

        recyclerView = findViewById(R.id.recycler_view_lines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_line);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(LineListActivity.this, LineEditActivity.class);
            startActivity(intent);
        });

        loadLines();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLines();
    }

    private void loadLines() {
        lineDAO.open();
        lineList = lineDAO.getAllLines();
        lineDAO.close();

        adapter = new LineAdapter(this, lineList, this);
        recyclerView.setAdapter(adapter);

        View emptyView = findViewById(R.id.empty_view);
        if (lineList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLineClick(int position) {
        Line line = lineList.get(position);
        Intent intent = new Intent(LineListActivity.this, LineEditActivity.class);
        intent.putExtra("line_id", line.getId());
        startActivity(intent);
    }

    @Override
    public void onLineDeleteClick(int position) {
        Line line = lineList.get(position);
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_line_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    lineDAO.open();
                    boolean deleted = lineDAO.deleteLine(line.getId());
                    lineDAO.close();

                    if (deleted) {
                        lineList.remove(position);
                        adapter.notifyItemRemoved(position);

                        if (lineList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(LineListActivity.this,
                                "Line " + line.getName() + " deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
