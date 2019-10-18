package com.shubhamkhuva.weatherapp.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    static final String TABLE_NAME_WEATHER = "weather_data";

    // Table columns
    static final String _ID = "id";
    static final String CITY = "city";
    static final String TEMP = "temp";
    static final String TEMP_MIN = "tempMin";
    static final String TEMP_MAX = "tempMax";
    static final String TEMP_TEXT = "tempText";
    static final String TEMP_ICON = "tempIcon";
    static final String DAY1_DAY = "day1Day";
    static final String DAY1_VALUE = "day1Value";
    static final String DAY2_DAY = "day2Day";
    static final String DAY2_VALUE = "day2Value";
    static final String DAY3_DAY = "day3Day";
    static final String DAY3_VALUE = "day3Value";
    static final String DAY4_DAY = "day4Day";
    static final String DAY4_VALUE = "day4Value";
    static final String DAY5_DAY = "day5Day";
    static final String DAY5_VALUE = "day5Value";

    // Database Information
    private static final String DB_NAME = "WeatherApp";

    // database version
    private static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME_WEATHER + "("
            + _ID + " INTEGER, "
            + CITY + " TEXT, "
            + TEMP + " TEXT, "
            + TEMP_MIN + " TEXT, "
            + TEMP_MAX + " TEXT, "
            + TEMP_TEXT + " TEXT, "
            + TEMP_ICON + " BLOB, "
            + DAY1_DAY + " TEXT, "
            + DAY1_VALUE + " TEXT, "
            + DAY2_DAY + " TEXT, "
            + DAY2_VALUE + " TEXT, "
            + DAY3_DAY + " TEXT, "
            + DAY3_VALUE + " TEXT, "
            + DAY4_DAY + " TEXT, "
            + DAY4_VALUE + " TEXT, "
            + DAY5_DAY + " TEXT, "
            + DAY5_VALUE + " TEXT);";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WEATHER);
        onCreate(db);
    }
}
