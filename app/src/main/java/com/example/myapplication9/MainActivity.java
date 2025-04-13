package com.example.myapplication9;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication9.line.LineListActivity;
import com.example.myapplication9.route.RouteListActivity;
import com.example.myapplication9.schedule.ScheduleListActivity;
import com.example.myapplication9.stop.StopListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView linesCard = findViewById(R.id.card_lines);
        CardView stopsCard = findViewById(R.id.card_stops);
        CardView routesCard = findViewById(R.id.card_routes);
        CardView schedulesCard = findViewById(R.id.card_schedules);

        linesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LineListActivity.class);
            startActivity(intent);
        });

        stopsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StopListActivity.class);
            startActivity(intent);
        });

        routesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RouteListActivity.class);
            startActivity(intent);
        });

        schedulesCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleListActivity.class);
            startActivity(intent);
        });
    }
}