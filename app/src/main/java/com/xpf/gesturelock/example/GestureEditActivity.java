package com.xpf.gesturelock.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.gesturelock.library.widget.GestureContentView;
import com.xpf.gesturelock.library.widget.GestureDrawLine;
import com.xpf.gesturelock.library.widget.LockIndicator;

/**
 * Created by xpf on 2016/11/11 :)
 * Function:手势设置、修改页面
 */
public class GestureEditActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "GestureEditActivity";
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 首次提示绘制手势密码，可以选择跳过
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    private TextView mTextTitle;
    private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextReset;
    private String mParamSetUpcode = null;
    private String mParamPhoneNumber;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private String mConfirmPassword = null;
    private int mParamIntentCode;

    private SharedPreferences mSharedPreferences = null;

    private GestureDrawLine.GestureCallBack gestureCallBack = new GestureDrawLine.GestureCallBack() {
        @Override
        public void onGestureCodeInput(String inputCode) {
            Log.i(TAG, "onGestureCodeInput()");
            if (!isInputPassValidate(inputCode)) {
                mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
                mGestureContentView.clearDrawLineState(0L);
                return;
            }

            if (mIsFirstInput) {
                mFirstPassword = inputCode;
                updateCodeList(inputCode);
                mGestureContentView.clearDrawLineState(0L);
                mTextReset.setClickable(true);
                mTextReset.setText(getString(R.string.reset_gesture_code));
            } else {
                if (inputCode.equals(mFirstPassword)) {
                    Toast.makeText(GestureEditActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    mGestureContentView.clearDrawLineState(0L);
                    GestureEditActivity.this.finish();
                } else {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
                    // 左右移动动画
                    Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
                    mTextTip.startAnimation(shakeAnimation);
                    // 保持绘制的线，1.5秒后清除
                    mGestureContentView.clearDrawLineState(1300L);
                }
            }

            mIsFirstInput = false;
        }

        @Override
        public void checkedSuccess() {
            Log.i(TAG, "checkedSuccess()");
        }

        @Override
        public void checkedFail() {
            Log.i(TAG, "checkedFail()");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        setUpViews();
        setUpListeners();
    }

    private void setUpViews() {
        mTextTitle = findViewById(R.id.text_title);
        mTextCancel = findViewById(R.id.text_cancel);
        mTextReset = findViewById(R.id.text_reset);
        mTextReset.setClickable(false);
        mLockIndicator = findViewById(R.id.lock_indicator);
        mTextTip = findViewById(R.id.text_tip);
        mGestureContainer = findViewById(R.id.gesture_container);
        mSharedPreferences = this.getSharedPreferences("secret_protect", Context.MODE_PRIVATE);

        // 初始化一个显示各个点的 viewGroup
        mGestureContentView = new GestureContentView.Builder(this)
                .isVerify(false)
                .setPassword("")
                .setBlockWeight(3)
                .setCallback(gestureCallBack)
                .build();

        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);

        updateCodeList("");
    }

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);
        mTextReset.setOnClickListener(this);
    }

    @SuppressLint("ApplySharedPref")
    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
        mSharedPreferences.edit().putString("inputCode", inputCode).commit();
        Log.i(TAG, "inputCode = " + inputCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
                this.finish();
                break;
            case R.id.text_reset:
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                break;
            default:
                break;
        }
    }

    private boolean isInputPassValidate(String inputPassword) {
        return !TextUtils.isEmpty(inputPassword) && inputPassword.length() >= 4;
    }
}
