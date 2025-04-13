package com.example.myapplication9.route;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.LineDAO;
import com.example.myapplication9.dao.RouteDAO;
import com.example.myapplication9.dao.StopDAO;
import com.example.myapplication9.model.Line;
import com.example.myapplication9.model.Route;
import com.example.myapplication9.model.Stop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteEditActivity extends AppCompatActivity {

    private Spinner spinnerLine;
    private Spinner spinnerStop;
    private EditText editTextSequence;
    private EditText editTextDistance;
    private Button buttonSave;

    private LineDAO lineDAO;
    private StopDAO stopDAO;
    private RouteDAO routeDAO;

    private List<Line> lineList;
    private List<Stop> stopList;

    private Route currentRoute;
    private long routeId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        spinnerLine = findViewById(R.id.spinner_line);
        spinnerStop = findViewById(R.id.spinner_stop);
        editTextSequence = findViewById(R.id.edit_text_sequence);
        editTextDistance = findViewById(R.id.edit_text_distance);
        buttonSave = findViewById(R.id.button_save);

        lineDAO = new LineDAO(this);
        stopDAO = new StopDAO(this);
        routeDAO = new RouteDAO(this);

        loadLinesAndStops();

        if (getIntent().hasExtra("route_id")) {
            routeId = getIntent().getLongExtra("route_id", -1);
            isEditMode = true;
            setTitle(R.string.edit_route);
            loadRouteData();
        } else {
            setTitle(R.string.add_route);
        }

        buttonSave.setOnClickListener(v -> saveRouteData());
    }

    private void loadLinesAndStops() {
        lineDAO.open();
        lineList = lineDAO.getAllLines();
        lineDAO.close();

        List<String> lineNames = new ArrayList<>();
        for (Line line : lineList) {
            lineNames.add(line.getName() + " (" + line.getType() + ")");
        }

        ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, lineNames);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLine.setAdapter(lineAdapter);

        stopDAO.open();
        stopList = stopDAO.getAllStops();
        stopDAO.close();

        List<String> stopNames = new ArrayList<>();
        for (Stop stop : stopList) {
            stopNames.add(stop.getName());
        }

        ArrayAdapter<String> stopAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, stopNames);
        stopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStop.setAdapter(stopAdapter);
    }

    private void loadRouteData() {
        routeDAO.open();
        currentRoute = routeDAO.getRouteById(routeId);
        routeDAO.close();

        if (currentRoute != null) {
            editTextSequence.setText(String.valueOf(currentRoute.getStopSequence()));
            editTextDistance.setText(String.valueOf(currentRoute.getDistanceFromStart()));

            for (int i = 0; i < lineList.size(); i++) {
                if (lineList.get(i).getId() == currentRoute.getLineId()) {
                    spinnerLine.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < stopList.size(); i++) {
                if (stopList.get(i).getId() == currentRoute.getStopId()) {
                    spinnerStop.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveRouteData() {
        if (spinnerLine.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Please select a line", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerStop.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Please select a stop", Toast.LENGTH_SHORT).show();
            return;
        }

        Line selectedLine = lineList.get(spinnerLine.getSelectedItemPosition());
        Stop selectedStop = stopList.get(spinnerStop.getSelectedItemPosition());

        String sequenceStr = editTextSequence.getText().toString().trim();
        if (TextUtils.isEmpty(sequenceStr)) {
            editTextSequence.setError("Sequence number is required");
            return;
        }

        String distanceStr = editTextDistance.getText().toString().trim();
        if (TextUtils.isEmpty(distanceStr)) {
            editTextDistance.setError("Distance is required");
            return;
        }

        int sequence;
        double distance;
        try {
            sequence = Integer.parseInt(sequenceStr);
            if (sequence < 1) {
                editTextSequence.setError("Sequence must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            editTextSequence.setError("Invalid sequence number");
            return;
        }

        try {
            distance = Double.parseDouble(distanceStr);
            if (distance < 0) {
                editTextDistance.setError("Distance cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            editTextDistance.setError("Invalid distance");
            return;
        }

        routeDAO.open();
        boolean success;

        if (isEditMode && currentRoute != null) {
            // Update existing route
            currentRoute.setLineId(selectedLine.getId());
            currentRoute.setStopId(selectedStop.getId());
            currentRoute.setStopSequence(sequence);
            currentRoute.setDistanceFromStart(distance);
            success = routeDAO.updateRoute(currentRoute);
        } else {
            // Create new route
            Route newRoute = new Route(selectedLine.getId(), selectedStop.getId(), sequence, distance);
            success = routeDAO.insertRoute(newRoute) > 0;
        }

        routeDAO.close();

        if (success) {
            Toast.makeText(this,
                    isEditMode ? "Route updated successfully" : "Route added successfully",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Error saving route",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
