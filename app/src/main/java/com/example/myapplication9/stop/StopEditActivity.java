package com.example.myapplication9.stop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.StopDAO;
import com.example.myapplication9.model.Stop;

import java.util.Objects;

public class StopEditActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private CheckBox checkBoxAccessibility;
    private Button buttonSave;

    private StopDAO stopDAO;
    private Stop currentStop;
    private long stopId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        editTextName = findViewById(R.id.edit_text_stop_name);
        editTextLatitude = findViewById(R.id.edit_text_latitude);
        editTextLongitude = findViewById(R.id.edit_text_longitude);
        checkBoxAccessibility = findViewById(R.id.checkbox_accessibility);
        buttonSave = findViewById(R.id.button_save);

        stopDAO = new StopDAO(this);

        if (getIntent().hasExtra("stop_id")) {
            stopId = getIntent().getLongExtra("stop_id", -1);
            isEditMode = true;
            setTitle(R.string.edit_stop);
            loadStopData();
        } else {
            setTitle(R.string.add_stop);
        }

        buttonSave.setOnClickListener(v -> saveStopData());
    }

    private void loadStopData() {
        stopDAO.open();
        currentStop = stopDAO.getStopById(stopId);
        stopDAO.close();

        if (currentStop != null) {
            editTextName.setText(currentStop.getName());
            editTextLatitude.setText(String.valueOf(currentStop.getLatitude()));
            editTextLongitude.setText(String.valueOf(currentStop.getLongitude()));
            checkBoxAccessibility.setChecked(currentStop.isAccessibility());
        }
    }

    private void saveStopData() {
        String name = editTextName.getText().toString().trim();
        String latitudeStr = editTextLatitude.getText().toString().trim();
        String longitudeStr = editTextLongitude.getText().toString().trim();
        boolean accessibility = checkBoxAccessibility.isChecked();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Stop name is required");
            return;
        }

        if (TextUtils.isEmpty(latitudeStr)) {
            editTextLatitude.setError("Latitude is required");
            return;
        }

        if (TextUtils.isEmpty(longitudeStr)) {
            editTextLongitude.setError("Longitude is required");
            return;
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);

            if (latitude < -90 || latitude > 90) {
                editTextLatitude.setError("Latitude must be between -90 and 90");
                return;
            }

            if (longitude < -180 || longitude > 180) {
                editTextLongitude.setError("Longitude must be between -180 and 180");
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_invalid_coordinates, Toast.LENGTH_SHORT).show();
            return;
        }

        stopDAO.open();
        boolean success;

        if (isEditMode && currentStop != null) {
            currentStop.setName(name);
            currentStop.setLatitude(latitude);
            currentStop.setLongitude(longitude);
            currentStop.setAccessibility(accessibility);
            success = stopDAO.updateStop(currentStop);
        } else {
            Stop newStop = new Stop(name, latitude, longitude, accessibility);
            success = stopDAO.insertStop(newStop) > 0;
        }

        stopDAO.close();

        if (success) {
            Toast.makeText(this,
                    isEditMode ? "Stop updated successfully" : "Stop added successfully",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Error saving stop",
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
