package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextContent;
    private ImageButton btnSave, btnBack;
    private int idNote;
    private MyDBHelper myDBHelper = new MyDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initUI();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Note note = (Note) bundle.get("updateNote");
            if(note != null){
                idNote = note.getId();
                editTextTitle.setText(note.getTitle());
                editTextContent.setText(note.getContent());
            }
        }

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
                if (!content.equals("")) {
                    long resultInsert = myDBHelper.updateNote(idNote,title, content, daynow);
                    if (resultInsert > 0) {
                        Toast.makeText(EditNoteActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditNoteActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Enter the content", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI() {
        editTextTitle = findViewById(R.id.edit_text_title_edit);
        editTextContent = findViewById(R.id.edit_text_content_edit);
        btnSave = findViewById(R.id.btn_save_edit);
        btnBack = findViewById(R.id.btn_back_edit);

    }
}