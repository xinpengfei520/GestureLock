package com.xpf.gesturelock.library.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xpf.gesturelock.library.R;
import com.xpf.gesturelock.library.common.AppUtil;
import com.xpf.gesturelock.library.entity.GesturePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x-sir on 2019/2/14 :)
 * Function:
 */
public class GestureContentView extends ViewGroup {

    private int mBaseNum = 6;
    /**
     * 每个点区域的宽度
     */
    private int mBlockWidth;
    /**
     * 声明一个集合用来封装坐标集合
     */
    private List<GesturePoint> mList = new ArrayList<>();
    private GestureDrawLine mGestureDrawLine;

    /**
     * constructor
     *
     * @param builder
     */
    private GestureContentView(Builder builder) {
        super(builder.appContext);
        int[] screenDisplay = AppUtil.getScreenDisplay(builder.appContext);
        // 一个圆点的宽度为屏幕的 1/4 宽度（默认）
        mBlockWidth = screenDisplay[0] / builder.blockWeight;
        // 添加 9 个图标
        addChild(builder.appContext);
        // 初始化一个可以画线的 view
        mGestureDrawLine = new GestureDrawLine(builder.appContext, mList, builder.verify, builder.password, builder.callback);
    }

    private void addChild(Context context) {
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(context);
            image.setBackgroundResource(R.drawable.gesture_node_normal);
            this.addView(image);
            invalidate();
            // 第几行
            int row = i / 3;
            // 第几列
            int col = i % 3;
            // 定义点的每个属性
            int leftX = col * mBlockWidth + mBlockWidth / mBaseNum;
            int topY = row * mBlockWidth + mBlockWidth / mBaseNum;
            int rightX = col * mBlockWidth + mBlockWidth - mBlockWidth / mBaseNum;
            int bottomY = row * mBlockWidth + mBlockWidth - mBlockWidth / mBaseNum;
            GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image, i + 1);
            this.mList.add(p);
        }
    }

    public void setParentView(ViewGroup parent) {
        // 宽度、高度为 3 个圆点的宽度
        int width = mBlockWidth * 3;
        LayoutParams layoutParams = new LayoutParams(width, width);
        this.setLayoutParams(layoutParams);
        mGestureDrawLine.setLayoutParams(layoutParams);
        parent.addView(mGestureDrawLine);
        parent.addView(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            // 第几行
            int row = i / 3;
            // 第几列
            int col = i % 3;
            View v = getChildAt(i);
            v.layout(col * mBlockWidth + mBlockWidth / mBaseNum, row * mBlockWidth + mBlockWidth / mBaseNum,
                    col * mBlockWidth + mBlockWidth - mBlockWidth / mBaseNum, row * mBlockWidth + mBlockWidth - mBlockWidth / mBaseNum);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 遍历设置每个子view的大小
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 保留路径delayTime时间长
     *
     * @param delayTime
     */
    public void clearDrawLineState(long delayTime) {
        mGestureDrawLine.clearDrawLineState(delayTime);
    }

    /**
     * Builder inner class.
     */
    public static final class Builder {
        private Context appContext;
        private boolean verify;
        private String password;
        private int blockWeight = 4; // default 4
        private GestureDrawLine.GestureCallBack callback;

        /**
         * Constructor
         */
        public Builder(Context context) {
            this.appContext = context.getApplicationContext();
        }

        /**
         * Set verify.
         *
         * @param verify
         * @return
         */
        public Builder isVerify(boolean verify) {
            this.verify = verify;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置一个点占屏幕的几分之一(默认为4)
         *
         * @param blockWeight
         * @return
         */
        public Builder setBlockWeight(int blockWeight) {
            this.blockWeight = blockWeight;
            return this;
        }

        public Builder setCallback(GestureDrawLine.GestureCallBack callback) {
            this.callback = callback;
            return this;
        }

        public GestureContentView build() {
            return new GestureContentView(this);
        }
    }
}
