package com.example.myapplication9.schedule;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.LineDAO;
import com.example.myapplication9.dao.ScheduleDAO;
import com.example.myapplication9.dao.StopDAO;
import com.example.myapplication9.model.Line;
import com.example.myapplication9.model.Schedule;
import com.example.myapplication9.model.Stop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ScheduleEditActivity extends AppCompatActivity {

    private Spinner spinnerLine;
    private Spinner spinnerStop;
    private EditText editTextTime;
    private Button buttonSetTime;
    private CheckBox checkBoxWeekday;
    private CheckBox checkBoxWeekend;
    private Button buttonSave;

    private LineDAO lineDAO;
    private StopDAO stopDAO;
    private ScheduleDAO scheduleDAO;

    private List<Line> lineList;
    private List<Stop> stopList;

    private Schedule currentSchedule;
    private long scheduleId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        spinnerLine = findViewById(R.id.spinner_line);
        spinnerStop = findViewById(R.id.spinner_stop);
        editTextTime = findViewById(R.id.edit_text_time);
        buttonSetTime = findViewById(R.id.button_set_time);
        checkBoxWeekday = findViewById(R.id.checkbox_weekday);
        checkBoxWeekend = findViewById(R.id.checkbox_weekend);
        buttonSave = findViewById(R.id.button_save);

        lineDAO = new LineDAO(this);
        stopDAO = new StopDAO(this);
        scheduleDAO = new ScheduleDAO(this);

        loadLinesAndStops();

        buttonSetTime.setOnClickListener(v -> showTimePickerDialog());

        if (getIntent().hasExtra("schedule_id")) {
            scheduleId = getIntent().getLongExtra("schedule_id", -1);
            isEditMode = true;
            setTitle(R.string.edit_schedule);
            loadScheduleData();
        } else {
            setTitle(R.string.add_schedule);
            checkBoxWeekday.setChecked(true);

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            editTextTime.setText(String.format("%02d:%02d", hour, minute));
        }

        buttonSave.setOnClickListener(v -> saveScheduleData());
    }

    private void loadLinesAndStops() {
        lineDAO.open();
        lineList = lineDAO.getAllLines();
        lineDAO.close();

        if (lineList.isEmpty()) {
            Toast.makeText(this, "Please add transport lines before creating schedules", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

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

        if (stopList.isEmpty()) {
            Toast.makeText(this, "Please add stops before creating schedules", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        List<String> stopNames = new ArrayList<>();
        for (Stop stop : stopList) {
            stopNames.add(stop.getName());
        }

        ArrayAdapter<String> stopAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, stopNames);
        stopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStop.setAdapter(stopAdapter);
    }

    private void loadScheduleData() {
        scheduleDAO.open();
        currentSchedule = scheduleDAO.getScheduleById(scheduleId);
        scheduleDAO.close();

        if (currentSchedule != null) {
            editTextTime.setText(currentSchedule.getDepartureTime());
            checkBoxWeekday.setChecked(currentSchedule.isWeekday());
            checkBoxWeekend.setChecked(currentSchedule.isWeekend());

            for (int i = 0; i < lineList.size(); i++) {
                if (lineList.get(i).getId() == currentSchedule.getLineId()) {
                    spinnerLine.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < stopList.size(); i++) {
                if (stopList.get(i).getId() == currentSchedule.getStopId()) {
                    spinnerStop.setSelection(i);
                    break;
                }
            }
        }
    }

    private void showTimePickerDialog() {
        String currentTime = editTextTime.getText().toString();
        int hour = 0;
        int minute = 0;

        if (!TextUtils.isEmpty(currentTime) && currentTime.contains(":")) {
            String[] timeParts = currentTime.split(":");
            try {
                hour = Integer.parseInt(timeParts[0]);
                minute = Integer.parseInt(timeParts[1]);
            } catch (NumberFormatException e) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, selectedMinute) -> {
                    String time = String.format("%02d:%02d", hourOfDay, selectedMinute);
                    editTextTime.setText(time);
                },
                hour, minute, true);

        timePickerDialog.show();
    }

    private void saveScheduleData() {
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

        String time = editTextTime.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            editTextTime.setError("Time is required");
            return;
        }

        if (!time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            editTextTime.setError("Invalid time format (HH:MM)");
            return;
        }

        boolean isWeekday = checkBoxWeekday.isChecked();
        boolean isWeekend = checkBoxWeekend.isChecked();

        if (!isWeekday && !isWeekend) {
            Toast.makeText(this, "Please select at least one day option", Toast.LENGTH_SHORT).show();
            return;
        }

        scheduleDAO.open();
        boolean success;

        if (isEditMode && currentSchedule != null) {
            currentSchedule.setLineId(selectedLine.getId());
            currentSchedule.setStopId(selectedStop.getId());
            currentSchedule.setDepartureTime(time);
            currentSchedule.setWeekday(isWeekday);
            currentSchedule.setWeekend(isWeekend);
            success = scheduleDAO.updateSchedule(currentSchedule);
        } else {
            Schedule newSchedule = new Schedule(selectedLine.getId(), selectedStop.getId(), time, isWeekday, isWeekend);
            success = scheduleDAO.insertSchedule(newSchedule) > 0;
        }

        scheduleDAO.close();

        if (success) {
            Toast.makeText(this,
                    isEditMode ? "Schedule updated successfully" : "Schedule added successfully",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Error saving schedule",
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
