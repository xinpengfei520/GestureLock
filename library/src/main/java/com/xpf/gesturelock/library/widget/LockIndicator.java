package com.xpf.gesturelock.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.xpf.gesturelock.library.R;

/**
 * Created by x-sir on 2019/2/14 :)
 * Function:自定义手势指示器
 */
public class LockIndicator extends View {

    /**
     * 行数
     */
    private int numRow = 3;
    /**
     * 列数
     */
    private int numColumn = 3;
    private int patternWidth = 60;
    private int patternHeight = 60;
    private int f = 5;
    private int g = 5;
    private int strokeWidth = 2;
    private Paint paint = null;
    /**
     * 圆圈正常显示的效果
     */
    private Drawable patternNormal = null;
    /**
     * 圆圈按压显示的效果
     */
    private Drawable patternPressed = null;
    /**
     * 手势密码
     */
    private String lockPassStr;

    public LockIndicator(Context paramContext) {
        this(paramContext, null);
    }

    public LockIndicator(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public LockIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        patternNormal = getResources().getDrawable(R.drawable.lock_pattern_node_normal);
        patternPressed = getResources().getDrawable(R.drawable.lock_pattern_node_pressed);
        if (patternPressed != null) {
            patternWidth = patternPressed.getIntrinsicWidth();
            patternHeight = patternPressed.getIntrinsicHeight();
            this.f = (patternWidth / 4);
            this.g = (patternHeight / 4);
            patternPressed.setBounds(0, 0, patternWidth, patternHeight);
            patternNormal.setBounds(0, 0, patternWidth, patternHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((patternPressed == null) || (patternNormal == null)) {
            return;
        }

        // 绘制 3*3 的图标
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numColumn; j++) {
                paint.setColor(-16777216);

                int i1 = j * patternHeight + j * this.g;
                int i2 = i * patternWidth + i * this.f;

                canvas.save();
                canvas.translate(i1, i2);
                // 计算当前数字的值，i 为行数，j 为列数，因为从 0 开始，所有要 +1
                String curNum = String.valueOf(numColumn * i + (j + 1));

                if (!TextUtils.isEmpty(lockPassStr)) {
                    if (!lockPassStr.contains(curNum)) {
                        // 未选中
                        patternNormal.draw(canvas);
                    } else {
                        // 被选中
                        patternPressed.draw(canvas);
                    }
                } else {
                    // 重置状态
                    patternNormal.draw(canvas);
                }

                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        if (patternPressed != null)
            setMeasuredDimension(numColumn * patternHeight + this.g
                    * (-1 + numColumn), numRow * patternWidth + this.f
                    * (-1 + numRow));
    }

    /**
     * 请求重新绘制
     *
     * @param paramString 手势密码字符序列
     */
    public void setPath(String paramString) {
        lockPassStr = paramString;
        invalidate();
    }
}
