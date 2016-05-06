package com.example.che.myapplication.activity;

import com.example.che.myapplication.R;
import com.example.che.myapplication.common.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected void init() {
        hideTitle();
        setContent(R.layout.activity_main);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void loadData() {

    }
}
