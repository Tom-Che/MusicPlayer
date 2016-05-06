package com.example.che.myapplication.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.che.myapplication.R;
import com.example.che.myapplication.util.SystemStatusManager;

public abstract class BaseActivity extends Activity {
    protected BaseActivity mContext;
    private ProgressDialog progressDialog;
    private ProgressInterruptListener progressInterruptListener;

    private RelativeLayout rl_content;
    private LinearLayout ll_top;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        getApplicationEx().getActivities().add(this);

        setContentView(R.layout.activity_base);

        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        ll_top = (LinearLayout) findViewById(R.id.ll_top);

        tv_title = (TextView) findViewById(R.id.tv_title);
        init();

        initView();

        progressDialog = new ProgressDialog(this) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                if (progressInterruptListener != null) {
                    progressInterruptListener.onProgressInterruptListener(progressDialog);
                }
            }
        };
        progressDialog.setCanceledOnTouchOutside(false);
        loadData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);

            SystemStatusManager tintManager = new SystemStatusManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(0);// 状态栏无背景


            View v_state = findViewById(R.id.v_state);
            v_state.getLayoutParams().height = getStatusBarHeight();
        }

    }

    public void setContent(int r) {
        View view = LayoutInflater.from(this).inflate(r, null);
        rl_content.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void hideTitle() {
        ll_top.setVisibility(View.GONE);
    }

    public void showTitle() {
        ll_top.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void showLoading(ProgressInterruptListener progressInterruptListener) {
        this.progressInterruptListener = progressInterruptListener;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.show();
                }
            }
        });
    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
            }
        });
    }

    public ApplicationEx getApplicationEx() {
        return (ApplicationEx) getApplication();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplicationEx().getActivities().remove(this);
    }


    protected void init() {
    }

    protected void initView() {
    }

    protected void loadData() {
    }

    protected <T> T findViewFromContent(int r) {
        return (T) rl_content.findViewById(r);
    }

    private static Toast toast = null;

    protected void showTip(final String msg) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                } else {
                    toast.cancel();
                    toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int statusBarHeight = 0;

    public int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }


    public interface ProgressInterruptListener {
        public void onProgressInterruptListener(ProgressDialog progressDialog);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}