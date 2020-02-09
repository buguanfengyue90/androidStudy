package com.example.contacttest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProviderActivity extends Activity {

    EditText mUserDescEditText;  //用户描述信息
    EditText mUserTelEditText;  //电话号码
    EditText mUserCompIdEditText;  //用户所属的公司id
    Button mUserSubmitBtn;  //提交按钮
    Button mUserQueryByTelBtn; //查询按钮
    Button mUserDelBtn; //删除按钮

    EditText mCompIdEditText;  //公司id
    EditText mCompBussinessEditText;  //公司业务
    EditText mCompAddrEditText;  //公司地址
    Button mCompSubmitBtn;  //提交按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        initView();
    }

    private void initView() {

        //用户信息相关的view
        mUserDescEditText = findViewById(R.id.et_user_desc);
        mUserTelEditText = findViewById(R.id.et_user_tel);
        mUserCompIdEditText = findViewById(R.id.et_user_comp_id);
        mUserSubmitBtn = findViewById(R.id.bt_user_sub);
        mUserQueryByTelBtn = findViewById(R.id.bt_user_query);

        mUserSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfoRecord();
                mUserSubmitBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryPostCode();
                    }
                }, 1000);
            }
        });

        mUserQueryByTelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserTelEditText != null && mUserTelEditText.getText() != null) {
                    queryUserInfoByTelRecord();
                }
            }
        });

        //公司信息相关的view
        mCompIdEditText = findViewById(R.id.et_comp_id);
        mCompBussinessEditText = findViewById(R.id.et_comp_business);
        mCompAddrEditText = findViewById(R.id.et_comp_addr);
        mCompSubmitBtn = findViewById(R.id.bt_comp_sub);

        mCompSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCompanyRecord();
            }
        });
    }

    /**
     * 存储用户信息到ContentProvider
     */
    private void saveUserInfoRecord(){
        ContentValues newRecord = new ContentValues();
        newRecord.put(UserInfoDbHelper.DESC_COLUMN, mUserCompIdEditText.getText().toString());
        newRecord.put(UserInfoDbHelper.TEL_COLUMN, mUserTelEditText.getText().toString());
        newRecord.put(UserInfoDbHelper.COMP_ID_COLUMN, mCompIdEditText.getText().toString());
        getContentResolver().insert(UserInfoProvider.POSTCODE_URI, newRecord);
    }

    /**
     * 存储公司信息到ContentProvider
     */
    private void saveCompanyRecord(){
        ContentValues newRecord = new ContentValues();
        newRecord.put(UserInfoDbHelper.ADDR_COLUMN, mCompAddrEditText.getText().toString());
        newRecord.put(UserInfoDbHelper.BUSSINESS_COLUMN, mCompBussinessEditText.getText().toString());
        newRecord.put(UserInfoDbHelper.ID_COLUMN, mCompIdEditText.getText().toString());
        getContentResolver().insert(UserInfoProvider.COMPANY_URI, newRecord);
    }

    /**
     * 通过电话号码查询相关信息
     */
    private void queryPostCode(){
        Uri queryUri = Uri.parse("content://com.example.contacttest/userinfo/123456");
        Cursor cursor = getContentResolver().query(queryUri, null, null, null, null);

        if (cursor.moveToNext()){
            Toast.makeText(this, "电话来自：" +
                    cursor.getString(2), Toast.LENGTH_SHORT ).show();
        }
    }

    /**
     * 通过电话号码查询用户
     */
    private void queryUserInfoByTelRecord(){
        Uri queryUri = Uri.parse("content://com.example.contacttest/userinfo/" + mUserTelEditText.getText().toString());

        Cursor cursor = getContentResolver().query(queryUri, null, null, null, null);

        if (cursor.moveToNext()){
            Toast.makeText(this, "电话来自：" +
                    cursor.getString(2), Toast.LENGTH_SHORT ).show();
        }
    }
}
