package com.love.bts.ilovebts.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.love.bts.ilovebts.R;

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    protected Intent mIntent;

    /**
     * 툴바뷰
     */
    protected Toolbar mTbBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
    }

    /**
     * 툴바 셋팅
     */
    protected void initToolbar() {
        mTbBar = findViewById(R.id.tb_bar);
        setSupportActionBar(mTbBar);
    }

}
