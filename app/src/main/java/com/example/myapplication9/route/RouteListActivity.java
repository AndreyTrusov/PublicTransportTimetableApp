package com.example.myapplication9.route;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.RouteDAO;
import com.example.myapplication9.model.Route;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RouteListActivity extends AppCompatActivity implements RouteAdapter.RouteItemClickListener {

    private RecyclerView recyclerView;
    private RouteAdapter adapter;
    private RouteDAO routeDAO;
    private List<Route> routeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        setTitle(R.string.routes);

        routeDAO = new RouteDAO(this);

        recyclerView = findViewById(R.id.recycler_view_routes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_route);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(RouteListActivity.this, RouteEditActivity.class);
            startActivity(intent);
        });

        loadRoutes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRoutes();
    }

    private void loadRoutes() {
        routeDAO.open();
        routeList = routeDAO.getAllRoutes();
        routeDAO.close();

        adapter = new RouteAdapter(this, routeList, this);
        recyclerView.setAdapter(adapter);

        View emptyView = findViewById(R.id.empty_view);
        if (routeList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRouteClick(int position) {
        Route route = routeList.get(position);
        Intent intent = new Intent(RouteListActivity.this, RouteEditActivity.class);
        intent.putExtra("route_id", route.getId());
        startActivity(intent);
    }

    @Override
    public void onRouteDeleteClick(int position) {
        Route route = routeList.get(position);
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    routeDAO.open();
                    boolean deleted = routeDAO.deleteRoute(route.getId());
                    routeDAO.close();

                    if (deleted) {
                        routeList.remove(position);
                        adapter.notifyItemRemoved(position);

                        if (routeList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(RouteListActivity.this,
                                "Route deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
