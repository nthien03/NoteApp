package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NoteApp.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATE_AT = "createAt";

    private SQLiteDatabase myDB;

    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TABLE_NAME + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_CONTENT + " TEXT NOT NULL, "
                + COLUMN_CREATE_AT + " TEXT NOT NULL" + ")";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDrop = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sqlDrop);
        onCreate(db);
    }

    public void openDB() {
        myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (myDB != null && myDB.isOpen()) {
            myDB.close();
        }
    }

    public long insertNote(String title, String content, String createAt) {
        myDB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_CONTENT, content);
        contentValues.put(COLUMN_CREATE_AT, createAt);
        return myDB.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllNote() {
        myDB = getReadableDatabase();
        String sqlGetAll = "SELECT * FROM " + TABLE_NAME;
        return myDB.rawQuery(sqlGetAll, null);
    }
    public long updateNote(int id, String title, String content, String createAt){
        myDB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_CONTENT, content);
        contentValues.put(COLUMN_CREATE_AT, createAt);
        String query = COLUMN_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        return myDB.update(TABLE_NAME, contentValues, query, selectionArgs);
    }

    public long deleteNote(int id){
        myDB = getWritableDatabase();
        String query = COLUMN_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };
        return myDB.delete(TABLE_NAME,query,selectionArgs);
    }


}
