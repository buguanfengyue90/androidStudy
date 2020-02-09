package com.example.contacttest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInfoDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "userinfo.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_USER_INFO = "userinfo";
    public static final String TABLE_COMPANY = "company";

    public static final String TEL_COLUMN = "tel_num";
    public static final String DESC_COLUMN = "desc";
    public static final String COMP_ID_COLUMN = "comp_id";
    public static final String ID_COLUMN = "id";
    public static final String BUSSINESS_COLUMN = "business";
    public static final String ADDR_COLUMN = "addr";

    private static final String POSTCODE_TABLE_SQL = "CREATE TABLE " + TABLE_USER_INFO + " ("
            + TEL_COLUMN + " TEXT ,"
            + COMP_ID_COLUMN + " TEXT ,"
            + DESC_COLUMN + " TEXT "
            + ")";

    private static final String COMPANY_TABLE_SQL = "CREATE TABLE " + TABLE_COMPANY + " ("
            + ID_COLUMN + " TEXT ,"
            + BUSSINESS_COLUMN + " TEXT ,"
            + ADDR_COLUMN + " TEXT "
            + ")";

    public UserInfoDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POSTCODE_TABLE_SQL);
        db.execSQL(COMPANY_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
