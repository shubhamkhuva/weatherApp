package com.shubhamkhuva.weatherapp.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import java.util.HashMap;

public class DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertWeather(String city,String temp,String tempMin,String tempMax,String tempText,byte[] tempIcon) {
        byte[] image = null;
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._ID, "1");
        contentValue.put(DatabaseHelper.CITY, city);
        contentValue.put(DatabaseHelper.TEMP, temp);
        contentValue.put(DatabaseHelper.TEMP_MIN, tempMin);
        contentValue.put(DatabaseHelper.TEMP_MIN, tempMax);
        contentValue.put(DatabaseHelper.TEMP_TEXT, tempText);
        contentValue.put(DatabaseHelper.TEMP_ICON, tempIcon);
        database.insert(DatabaseHelper.TABLE_NAME_WEATHER, null, contentValue);
    }

    public HashMap<String, String> fetchWeather() {
        HashMap<String, String> map = new HashMap<>();
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.TEMP,DatabaseHelper.TEMP_MIN,DatabaseHelper.TEMP_MAX,DatabaseHelper.TEMP_TEXT,DatabaseHelper.TEMP_ICON,
        DatabaseHelper.DAY1_DAY,DatabaseHelper.DAY1_VALUE,DatabaseHelper.DAY2_DAY,DatabaseHelper.DAY2_VALUE,
                DatabaseHelper.DAY3_DAY,DatabaseHelper.DAY3_VALUE,DatabaseHelper.DAY4_DAY,DatabaseHelper.DAY4_VALUE,
                DatabaseHelper.DAY5_DAY,DatabaseHelper.DAY5_VALUE,DatabaseHelper.CITY};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_WEATHER, columns, DatabaseHelper._ID + " = '1'", null, null, null, null);
        if (cursor.moveToFirst()){
            map.put(DatabaseHelper.CITY,cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY)));
            map.put(DatabaseHelper.TEMP,cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEMP)));
            map.put(DatabaseHelper.TEMP_MIN,cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEMP_MIN)));
            map.put(DatabaseHelper.TEMP_MAX,cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEMP_MAX)));
            map.put(DatabaseHelper.TEMP_TEXT,cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEMP_TEXT)));
            map.put(DatabaseHelper.TEMP_ICON,Base64.encodeToString(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.TEMP_ICON)), Base64.DEFAULT));

            map.put(DatabaseHelper.DAY1_DAY,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY1_DAY)));
            map.put(DatabaseHelper.DAY1_VALUE,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY1_VALUE)));

            map.put(DatabaseHelper.DAY2_DAY,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY2_DAY)));
            map.put(DatabaseHelper.DAY2_VALUE,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY2_VALUE)));

            map.put(DatabaseHelper.DAY3_DAY,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY3_DAY)));
            map.put(DatabaseHelper.DAY3_VALUE,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY3_VALUE)));

            map.put(DatabaseHelper.DAY4_DAY,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY4_DAY)));
            map.put(DatabaseHelper.DAY4_VALUE,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY4_VALUE)));

            map.put(DatabaseHelper.DAY5_DAY,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY5_DAY)));
            map.put(DatabaseHelper.DAY5_VALUE,cursor.getString(cursor.getColumnIndex(DatabaseHelper.DAY5_VALUE)));
        }
        return map;
    }

    public int updateForecastDay1(String day1,String day1_value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DAY1_DAY, day1);
        contentValues.put(DatabaseHelper.DAY1_VALUE, day1_value);

        int i = database.update(DatabaseHelper.TABLE_NAME_WEATHER, contentValues, DatabaseHelper._ID + " = '1'", null);
        return i;
    }
    public int updateForecastDay2(String day2,String day2_value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.DAY2_DAY, day2);
        contentValues.put(DatabaseHelper.DAY2_VALUE, day2_value);

        int i = database.update(DatabaseHelper.TABLE_NAME_WEATHER, contentValues, DatabaseHelper._ID + " = '1'", null);
        return i;
    }
    public int updateForecastDay3(String day3,String day3_value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.DAY3_DAY, day3);
        contentValues.put(DatabaseHelper.DAY3_VALUE, day3_value);

        int i = database.update(DatabaseHelper.TABLE_NAME_WEATHER, contentValues, DatabaseHelper._ID + " = '1'", null);
        return i;
    }
    public int updateForecastDay4(String day4,String day4_value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.DAY4_DAY, day4);
        contentValues.put(DatabaseHelper.DAY4_VALUE, day4_value);

        int i = database.update(DatabaseHelper.TABLE_NAME_WEATHER, contentValues, DatabaseHelper._ID + " = '1'", null);
        return i;
    }
    public int updateForecastDay5(String day5,String day5_value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.DAY5_DAY, day5);
        contentValues.put(DatabaseHelper.DAY5_VALUE, day5_value);

        int i = database.update(DatabaseHelper.TABLE_NAME_WEATHER, contentValues, DatabaseHelper._ID + " = '1'", null);
        return i;
    }

    public int updateWeather(String city,String temp,String tempMin,String tempMax,String tempText,byte[] tempIcon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CITY, city);
        contentValues.put(DatabaseHelper.TEMP, temp);
        contentValues.put(DatabaseHelper.TEMP_MIN, tempMin);
        contentValues.put(DatabaseHelper.TEMP_MAX, tempMax);
        contentValues.put(DatabaseHelper.TEMP_TEXT, tempText);
        contentValues.put(DatabaseHelper.TEMP_ICON, tempIcon);

        int i = database.update(DatabaseHelper.TABLE_NAME_WEATHER, contentValues, DatabaseHelper._ID + " = '1'", null);
        return i;
    }

    public boolean checkIfMyTitleExists() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_WEATHER,null,DatabaseHelper._ID + "='1'", null, null, null, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;
    }
}
