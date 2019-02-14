package com.xpf.gesturelock.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xpf.gesturelock.library.widget.GestureLockViewGroup;

public class GesturePageActivity extends AppCompatActivity {

    private GestureLockViewGroup mGestureLockViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_page);

        mGestureLockViewGroup = findViewById(R.id.id_gestureLockViewGroup);
        // 设置正确的连接点
        mGestureLockViewGroup.setAnswer(new int[]{2, 5, 8, 9});
        // 设置当绘制图案时的监听
        mGestureLockViewGroup.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
            @Override
            public void onUnmatchedExceedBoundary() {
                Toast.makeText(GesturePageActivity.this, "错误5次...",
                        Toast.LENGTH_SHORT).show();
                mGestureLockViewGroup.setUnMatchExceedBoundary(5);
            }

            @Override
            public void onGestureEvent(boolean matched) {
                Toast.makeText(GesturePageActivity.this, matched + "",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBlockSelected(int cId) {

            }
        });
    }
}
