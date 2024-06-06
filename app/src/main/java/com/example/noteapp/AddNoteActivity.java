package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextContent;
    private ImageButton btnSave, btnBack;
    private MyDBHelper myDBHelper = new MyDBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initUI();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR);
                int month = today.get(Calendar.MONTH) + 1;
                int day = today.get(Calendar.DATE);
                String daynow = year + "-" + month + "-" + day;
                if (!content.equals("")){
                    long resultInsert = myDBHelper.insertNote(title, content, daynow);
                    if (resultInsert > 0) {
                        Toast.makeText(AddNoteActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddNoteActivity.this, "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else {
                    Toast.makeText(AddNoteActivity.this, "Enter the content", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI() {
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextContent = findViewById(R.id.edit_text_content);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);
    }
}