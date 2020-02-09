package com.example.contacttest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class UserInfoProvider extends ContentProvider {

    private static final String CONTENT = "content://";
    public static final String AUTHORITY = "com.example.contacttest";

    /*
    该ContentProvider所返回的数据类型定义、数据集合
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY;
    /*
    单项数据
     */
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY;
    /*
    数据集合操作时的Uri
     */
    public static final Uri POSTCODE_URI = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_USER_INFO);
    /*
    数据集合操作时的Uri
     */
    public static final Uri COMPANY_URI = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_COMPANY);

    private SQLiteDatabase mDataBase;

    static final int USER_INFOS = 1;
    static final int USER_INFO_ITEM = 2;
    static final int COMPANY = 3;
    static final int COMPANY_ITEM = 4;

    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "userinfo", USER_INFOS);
        uriMatcher.addURI(AUTHORITY, "userinfo/*", USER_INFO_ITEM);
        uriMatcher.addURI(AUTHORITY, "company", COMPANY);
        uriMatcher.addURI(AUTHORITY, "company/#", COMPANY_ITEM);
    }

    @Override
    public boolean onCreate() {
        mDataBase = new UserInfoDbHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                cursor = mDataBase.query(UserInfoDbHelper.TABLE_USER_INFO,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USER_INFO_ITEM:
                String tel = uri.getPathSegments().get(1);
                cursor = mDataBase.query(UserInfoDbHelper.TABLE_USER_INFO,
                        projection,
                        "tel_num=?",
                        new String[]{tel},
                        null,
                        null,
                        sortOrder);
                break;
            case COMPANY:
                cursor = mDataBase.query(UserInfoDbHelper.TABLE_COMPANY,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COMPANY_ITEM:
                String cid = uri.getPathSegments().get(1);
                cursor = mDataBase.query(UserInfoDbHelper.TABLE_COMPANY,
                        projection,
                        "tel_num=?",
                        new String[]{cid},
                        null,
                        null,
                        sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
            case COMPANY:
                return CONTENT_TYPE;
            case USER_INFO_ITEM:
            case COMPANY_ITEM:
                return CONTENT_TYPE_ITEM;
            default:
                throw  new RuntimeException("错误的Uri");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long newId = 0;
        Uri newUri = null;
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                newId = mDataBase.insert(UserInfoDbHelper.TABLE_USER_INFO, null, values);
                newUri = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_USER_INFO + "/" + newId);
                break;
            case COMPANY:
                newId = mDataBase.insert(UserInfoDbHelper.TABLE_COMPANY, null, values);
                newUri = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_COMPANY + "/" + newId);
                break;
        }
        if (newId > 0) {
            return newUri;
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                deleteRows = mDataBase.delete(UserInfoDbHelper.TABLE_USER_INFO,
                        selection,
                        selectionArgs);
                break;
            case USER_INFO_ITEM:
                String userId = uri.getPathSegments().get(1);
                deleteRows = mDataBase.delete(UserInfoDbHelper.TABLE_USER_INFO,
                        "id=?",
                        new String[]{userId});
                break;
            case COMPANY:
                deleteRows = mDataBase.delete(UserInfoDbHelper.TABLE_COMPANY,
                        selection,
                        selectionArgs);
                break;
            case COMPANY_ITEM:
                String companyId = uri.getPathSegments().get(1);
                deleteRows = mDataBase.delete(UserInfoDbHelper.TABLE_COMPANY,
                        "id=?",
                        new String[]{companyId});
                break;
        }
        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //更新数据
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                updateRows = mDataBase.update(UserInfoDbHelper.TABLE_USER_INFO,
                        values,
                        selection,
                        selectionArgs);
                break;
            case USER_INFO_ITEM:
                String userId = uri.getPathSegments().get(1);
                updateRows = mDataBase.update(UserInfoDbHelper.TABLE_USER_INFO,
                        values,
                        "id=?",
                        new String[]{userId});
                break;
            case COMPANY:
                updateRows = mDataBase.update(UserInfoDbHelper.TABLE_COMPANY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case COMPANY_ITEM:
                String companyId = uri.getPathSegments().get(1);
                updateRows = mDataBase.update(UserInfoDbHelper.TABLE_COMPANY,
                        values,
                        "id=?",
                        new String[]{companyId});
                break;
        }
        return updateRows;
    }
}
