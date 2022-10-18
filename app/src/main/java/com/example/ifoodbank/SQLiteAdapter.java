package com.example.ifoodbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteAdapter {

    public static final String MYDATABASE_NAME = "iFoodBank";
    public static final int MYDATABASE_VERSION = 1;
    public static final String MY_DATABASE_TABLE = "MY_TABLE_PROFILE_INFO";
    public static final String  KEY_CONTENT = "FULL_NAME";
    public static final String  KEY_CONTENT_2 = "PHONE_NO";
    public static final String  KEY_CONTENT_3 = "EMAIL";
    public static final String  KEY_CONTENT_4 = "ADDRESS";

    public static final String SCRIPT_CREATE_DATABASE = "create table " +
            MY_DATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CONTENT + " text not null, " +
            KEY_CONTENT_2 + " text not null, " +
            KEY_CONTENT_3 + " text not null, " +
            KEY_CONTENT_4 + " text not null);";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLiteAdapter(Context c) {
        context = c;
    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqLiteHelper.close();
    }

    // to insert data into the specified table with 4 parameters
    public long insert(String content, String content_2, String content_3, String content_4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT, content);
        contentValues.put(KEY_CONTENT_2, content_2);
        contentValues.put(KEY_CONTENT_3, content_3);
        contentValues.put(KEY_CONTENT_4, content_4);
        return sqLiteDatabase.insert(MY_DATABASE_TABLE, null,
                contentValues);
    }

    // update the entries
    public long update(String oldEmail, String content, String content_2, String content_3,
                       String content_4){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT, content);
        contentValues.put(KEY_CONTENT_2, content_2);
        contentValues.put(KEY_CONTENT_3, content_3);
        contentValues.put(KEY_CONTENT_4, content_4);

        return sqLiteDatabase.update(MY_DATABASE_TABLE, contentValues, KEY_CONTENT_3 + "=?", new String[]{oldEmail});
    }

    // delete all account
    public long deleteAll(){
        return sqLiteDatabase.delete(MY_DATABASE_TABLE, null, null);
    }

    // query all
    public String queueAll() {
        String[] columns = new String[]{"id", KEY_CONTENT, KEY_CONTENT_2, KEY_CONTENT_3,
                KEY_CONTENT_4};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE, columns,
                null, null, null, null, null);
        String result = "";
        int index_ID = cursor.getColumnIndex("id");
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_3 = cursor.getColumnIndex(KEY_CONTENT_3);
        int index_CONTENT_4 = cursor.getColumnIndex(KEY_CONTENT_4);
        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {
            result = result + cursor.getString(index_ID) + "; "
                    + cursor.getString(index_CONTENT) + "; "
                    + cursor.getString(index_CONTENT_2) + "; "
                    + cursor.getString(index_CONTENT_3) + "; "
                    + cursor.getString(index_CONTENT_4) + "\n";
        }
        return result;
    }

    // query all 2
    public ArrayList<String> queryRow(String email) {
        String[] columns = new String[]{KEY_CONTENT, KEY_CONTENT_2, KEY_CONTENT_3,
                KEY_CONTENT_4};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE, columns,
                KEY_CONTENT_3 + "=?", new String[]{email}, null, null, null);
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_3 = cursor.getColumnIndex(KEY_CONTENT_3);
        int index_CONTENT_4 = cursor.getColumnIndex(KEY_CONTENT_4);
        ArrayList<String> result = new ArrayList<String>();

        System.out.println(cursor.getCount());
        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {

            result.add(cursor.getString(index_CONTENT));
            result.add(cursor.getString(index_CONTENT_2));
            result.add(cursor.getString(index_CONTENT_3));
            result.add(cursor.getString(index_CONTENT_4));
        }

        System.out.println(result);
        return result;
    }

    // query full name
    public ArrayList<String> queueFullName() {
        String[] columns = new String[] {KEY_CONTENT};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE, columns, null, null, null, null,   "id ASC");
        ArrayList<String> result = new ArrayList<String>();
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {

            result.add(cursor.getString(index_CONTENT));
        }

        return result;
    }

    //query phone no
    public ArrayList<String> queuePhoneNo() {
        String[] columns = new String[] {KEY_CONTENT_2};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE, columns, null, null, null, null, "id ASC");
        ArrayList<String> result = new ArrayList<String>();
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_2);
        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {

            result.add(cursor.getString(index_CONTENT));
        }

        return result;
    }

    //query email
    public ArrayList<String> queueEmail() {
        String[] columns = new String[] {KEY_CONTENT_3};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE, columns, null, null, null, null, "id ASC");
        ArrayList<String> result = new ArrayList<String>();
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_3);
        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {

            result.add(cursor.getString(index_CONTENT));
        }

        return result;
    }

    //query address
    public ArrayList<String> queueAddress() {
        String[] columns = new String[] {KEY_CONTENT_4};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE, columns, null, null, null, null, "id ASC");
        ArrayList<String> result = new ArrayList<String>();
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_4);
        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {

            result.add(cursor.getString(index_CONTENT));
        }

        return result;
    }

    public class SQLiteHelper extends SQLiteOpenHelper
    {

        // 4 parameters constructor
        public SQLiteHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // create the database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

        // manage the versions
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);

        }
    }
}
