package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvNoteList;
    FloatingActionButton btnAdd;
    private List<Note> notes;
    private NoteAdapter noteAdapter;

    private final MyDBHelper myDBHelper = new MyDBHelper(this);

    @Override
    protected void onStart() {
        super.onStart();
        myDBHelper.openDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myDBHelper.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        notes = new ArrayList<>();
        rcvNoteList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        notes = getData();

        noteAdapter = new NoteAdapter(notes, new OnClickNote() {
            @Override
            public void onClickBtnEdit(int idNote) {
                List<Note> listCurrent = getData();
                Note noteEdit = null;
                for (Note note : listCurrent) {
                    if (note.getId() == idNote) {
                        noteEdit = note;
                        break;
                    }
                }
                Intent intentEdit = new Intent(MainActivity.this, EditNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("updateNote", noteEdit);
                intentEdit.putExtras(bundle);
                startActivity(intentEdit);

            }

            @Override
            public void onClickBtnDelete(int idNote) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Are you sure you want to delete this note?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        long resultDelete = myDBHelper.deleteNote(idNote);
                        if (resultDelete == -1) {
                            Toast.makeText(MainActivity.this, "ERROR",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "DELETED ",
                                    Toast.LENGTH_SHORT).show();
                            noteAdapter.refreshData(getData());
                        }

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        rcvNoteList.setAdapter(noteAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intentAdd);
            }
        });


    }

    private List<Note> getData() {
        Cursor cursor = myDBHelper.getAllNote();
        List<Note> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_CONTENT));
            String createAt = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_CREATE_AT));
            Note note = new Note(Integer.parseInt(id), title, content, Date.valueOf(createAt));
            list.add(note);
        }
        return list;


    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.refreshData(getData());
    }

    private void initUI() {
        rcvNoteList = findViewById(R.id.rcv_note_list);
        btnAdd = findViewById(R.id.btn_add);
    }
}