package com.careyun.voiceassistant.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Huangmq on 2017/3/23.
 */

public class LocationProvider extends ContentProvider {
    public static final String AUTHORITY = "com.careyun.provider.voiceassistant";
    private static final String DATABASE_NAME = "location.db";
    private static final String CITIES_TABLE_NAME = "cities";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static HashMap<String, String> sCitiesProjectionMap = new HashMap<String, String>();
    private static final int CITIES = 1;
    private static final int CITIES_ID = 2;
    private static final int DATABASE_VERSION = 6;
    static {
        sUriMatcher.addURI(AUTHORITY, "cities", CITIES);
        sUriMatcher.addURI(AUTHORITY, "cities/#", CITIES_ID);
        sCitiesProjectionMap.put(CityColumn._ID, CityColumn._ID);
        sCitiesProjectionMap.put(CityColumn.ADRESS, CityColumn.ADRESS);
        sCitiesProjectionMap.put(CityColumn.CITY_NAME, CityColumn.CITY_NAME);
        sCitiesProjectionMap.put(CityColumn.PROVINCE_NAME, CityColumn.PROVINCE_NAME);
        sCitiesProjectionMap.put(CityColumn.DISTRICT_NAME, CityColumn.DISTRICT_NAME);
        sCitiesProjectionMap.put(CityColumn.LATITUDE, CityColumn.LATITUDE);
        sCitiesProjectionMap.put(CityColumn.LONTITUDE, CityColumn.LONTITUDE);
        sCitiesProjectionMap.put(CityColumn.PROVICE_CODE, CityColumn.PROVICE_CODE);
        sCitiesProjectionMap.put(CityColumn.CITY_CODE, CityColumn.CITY_CODE);
    }
    public static class DatabaseHelper extends SQLiteOpenHelper {
        private final Context mContext;
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + CITIES_TABLE_NAME + " ("
                    + CityColumn._ID + " INTEGER PRIMARY KEY,"
                    + CityColumn.ADRESS + " TEXT,"
                    + CityColumn.CITY_NAME + " TEXT,"
                    + CityColumn.PROVINCE_NAME + " TEXT,"
                    + CityColumn.DISTRICT_NAME + " TEXT,"
                    + CityColumn.LATITUDE + " TEXT,"
                    + CityColumn.LONTITUDE + " TEXT,"
                    + CityColumn.PROVICE_CODE + " TEXT,"
                    + CityColumn.CITY_CODE + " TEXT"+ ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS cities");
            onCreate(db);
        }

    }
    private DatabaseHelper mOpenHelper;
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            switch (sUriMatcher.match(uri)) {
                case CITIES:
                    qb.setTables(CITIES_TABLE_NAME);
                    qb.setProjectionMap(sCitiesProjectionMap);
                    break;
                case CITIES_ID:
                    qb.setTables(CITIES_TABLE_NAME);
                    qb.setProjectionMap(sCitiesProjectionMap);
                    qb.appendWhere(CityColumn._ID + " = "
                            + uri.getPathSegments().get(1));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);

            }
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                    null, null);

            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CITIES:
                return CityColumn.CONTENT_TYPE;
            case CITIES_ID:
                return CityColumn.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != CITIES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = 0l;
        if (sUriMatcher.match(uri) == CITIES) {
            rowId = db.insert(CITIES_TABLE_NAME, null, values);
        }
        if (rowId > 0) {
            Uri cityUri = ContentUris.withAppendedId(
                    CityColumn.CONTENT_URI, rowId);;
            this.getContext().getContentResolver()
                    .notifyChange(cityUri, null);
            return cityUri;
        }
        throw new SQLException("Failed to insert row into " + uri);

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case CITIES:
                count = db.delete(CITIES_TABLE_NAME, selection, selectionArgs);
                break;
            case CITIES_ID:
                String weatherId = uri.getPathSegments().get(1);
                count = db.delete(CITIES_TABLE_NAME, CityColumn._ID
                        + "="
                        + weatherId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
    @Override
    public int update(Uri uri, ContentValues initialValues, String selection,
                      String[] selectionArgs) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case CITIES:
                count = db.update(CITIES_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CITIES_ID:
                String cityId = uri.getPathSegments().get(1);
                count = db.update(
                        CITIES_TABLE_NAME,
                        values,
                        CityColumn._ID
                                + "="
                                + cityId
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
