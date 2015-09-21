package mo.com.toggleviewdemo;

/**
 * 作者：MoMxMo on 2015/9/21 20:36
 * 邮箱：xxxx@qq.com
 */


import android.view.View;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ToggleView2 extends View {

    private final static int STATE_NONE = 0;
    private final static int STATE_DOWN = 1;
    private final static int STATE_MOVE = 2;
    private final static int STATE_UP = 3;

    private Bitmap mToggleBackground;
    private Bitmap mToggleSlide;

    private boolean isOpened = false;// 默认关闭状态
    private int mState = STATE_NONE;
    private float mCurrentX;
    private OnToggleListener mListener;

    public ToggleView2(Context context) {
        this(context, null);
    }

    public ToggleView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setToggleBackground(int resId) {
        mToggleBackground = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setToggleSilde(int resId) {
        mToggleSlide = BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 设置的大小和背景大小一致
        if (mToggleBackground != null) {
            // 设置自己的大小
            setMeasuredDimension(mToggleBackground.getWidth(),
                    mToggleBackground.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画背景
        if (mToggleBackground != null) {
            canvas.drawBitmap(mToggleBackground, 0, 0, null);
        }

        // 画滑块
        if (mToggleSlide == null) {
            // canvas.drawBitmap(mToggleSlide, 0, 0, null);
            return;
        }

        int slideWidth = mToggleSlide.getWidth();
        int backgroundWidth = mToggleBackground.getWidth();

        // 按下时 ，
        if (mState == STATE_DOWN || mState == STATE_MOVE) {
            if (!isOpened) {
                // 当现在是关闭状态,
                // 如果点击的是 滑块的左侧（按下的位置 小于 滑块的中间位置）,不动
                if (mCurrentX < slideWidth / 2f) {
                    canvas.drawBitmap(mToggleSlide, 0, 0, null);
                } else {
                    // 如果点击的是 滑块的右侧（按下的位置 大于 滑块的中间位置）,滑块的中间位置要和 按下的x位置一致
                    float left = mCurrentX - slideWidth / 2f;
                    if (left > backgroundWidth - slideWidth) {
                        left = backgroundWidth - slideWidth;
                    }
                    canvas.drawBitmap(mToggleSlide, left, 0, null);
                }
            } else {
                // 当前是打开的
                // 如果点击的是滑动块的右侧，不动(mCurrentX > middle)
                float middle = backgroundWidth - slideWidth / 2f;
                if (mCurrentX > middle) {
                    // 打开状态
                    float left = backgroundWidth - slideWidth;
                    canvas.drawBitmap(mToggleSlide, left, 0, null);
                } else {
                    float left = mCurrentX - slideWidth / 2f;
                    if (left <= 0) {
                        left = 0;
                    }
                    canvas.drawBitmap(mToggleSlide, left, 0, null);
                }
            }
        } else if (mState == STATE_UP) {
            // 当是关闭的状态
            if (!isOpened) {
                // 如果滑块的一半的位置大于背景一半的位置,显示为打开的状态
                if (mCurrentX > backgroundWidth / 2f) {
                    // 打开状态
                    float left = backgroundWidth - slideWidth;
                    canvas.drawBitmap(mToggleSlide, left, 0, null);

                    // 状态改变
                    isOpened = true;
                    // 触发点
                    if (mListener != null) {
                        mListener.onToggle(isOpened);
                    }

                } else {
                    // 关闭状态
                    canvas.drawBitmap(mToggleSlide, 0, 0, null);
                }
            } else {
                // 打开状态
                // 如果滑块的一半的位置小于背景一半的位置,显示为关闭的状态
                if (mCurrentX < backgroundWidth / 2f) {
                    // 关闭状态
                    canvas.drawBitmap(mToggleSlide, 0, 0, null);

                    // 状态改变
                    isOpened = false;
                    // 触发点
                    if (mListener != null) {
                        mListener.onToggle(isOpened);
                    }
                } else {
                    // 打开状态
                    float left = backgroundWidth - slideWidth;
                    canvas.drawBitmap(mToggleSlide, left, 0, null);
                }
            }
        } else {
            // 没有状态
            if (isOpened) {
                // 打开状态
                float left = backgroundWidth - slideWidth;
                canvas.drawBitmap(mToggleSlide, left, 0, null);
            } else {
                // 关闭状态
                canvas.drawBitmap(mToggleSlide, 0, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = STATE_DOWN;
                mCurrentX = event.getX();

                invalidate();
                // postInvalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;
                mCurrentX = event.getX();

                invalidate();
                // postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;
                mCurrentX = event.getX();// 滑块的中线

                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获得当前的开关状态
     *
     * @return
     */
    public boolean isOpen() {
        return isOpened;
    }

    public void setOnToggleListener(OnToggleListener listener) {
        this.mListener = listener;
    }

    public interface OnToggleListener {
        void onToggle(boolean isOpened);
    }
}

