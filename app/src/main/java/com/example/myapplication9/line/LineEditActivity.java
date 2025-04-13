package com.example.myapplication9.line;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication9.R;
import com.example.myapplication9.dao.LineDAO;
import com.example.myapplication9.model.Line;

import java.util.Objects;

public class LineEditActivity extends AppCompatActivity {

    private EditText editTextName;
    private Spinner spinnerType;
    private EditText editTextColor;
    private Button buttonSave;

    private LineDAO lineDAO;
    private Line currentLine;
    private long lineId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        editTextName = findViewById(R.id.edit_text_line_name);
        spinnerType = findViewById(R.id.spinner_line_type);
        editTextColor = findViewById(R.id.edit_text_line_color);
        buttonSave = findViewById(R.id.button_save);

        lineDAO = new LineDAO(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.transport_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        if (getIntent().hasExtra("line_id")) {
            lineId = getIntent().getLongExtra("line_id", -1);
            isEditMode = true;
            setTitle(R.string.edit_line);
            loadLineData();
        } else {
            setTitle(R.string.add_line);
            editTextColor.setText("#2196F3");
        }

        buttonSave.setOnClickListener(v -> saveLineData());
    }

    private void loadLineData() {
        lineDAO.open();
        currentLine = lineDAO.getLineById(lineId);
        lineDAO.close();

        if (currentLine != null) {
            editTextName.setText(currentLine.getName());
            editTextColor.setText(currentLine.getColor());

            String lineType = currentLine.getType();
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerType.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equalsIgnoreCase(lineType)) {
                    spinnerType.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveLineData() {
        String name = editTextName.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String color = editTextColor.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Line name is required");
            return;
        }

        if (TextUtils.isEmpty(color)) {
            editTextColor.setError("Color code is required");
            return;
        }

        if (!color.startsWith("#") || color.length() != 7) {
            editTextColor.setError("Invalid color format. Use #RRGGBB");
            return;
        }

        lineDAO.open();
        boolean success;

        if (isEditMode && currentLine != null) {
            currentLine.setName(name);
            currentLine.setType(type);
            currentLine.setColor(color);
            success = lineDAO.updateLine(currentLine);
        } else {
            Line newLine = new Line(name, type, color);
            success = lineDAO.insertLine(newLine) > 0;
        }

        lineDAO.close();

        if (success) {
            Toast.makeText(this,
                    isEditMode ? "Line updated successfully" : "Line added successfully",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Error saving line",
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
