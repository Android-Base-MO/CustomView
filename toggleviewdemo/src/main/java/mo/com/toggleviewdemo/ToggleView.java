package mo.com.toggleviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：MoMxMo on 2015/9/21 19:57
 * 邮箱：xxxx@qq.com
 */


public class ToggleView extends View {

    private static final int STATE_MOVE = 2;
    private static final int STATE_UP = 3;
    private static final int STATE_DOWN = 1;
    private Bitmap mBackgroupToggle;
    private Bitmap mBackgroupSilde;
    private float mCurrentX;
    private int mState;
    private boolean isOpened = false;   /*默认是关闭的*/
    private OnToggleOpenListener mListener;

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*设置背景*/
    public void setBackgroudToggle(int res) {
        mBackgroupToggle = BitmapFactory.decodeResource(getResources(), res);
    }

    /*设置滑块*/
    public void setBackgroupSilde(int res) {
        mBackgroupSilde = BitmapFactory.decodeResource(getResources(), res);
    }

    /*测量*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mBackgroupToggle != null) {
            /*设置自己的大小*/
            setMeasuredDimension(mBackgroupToggle.getWidth(), mBackgroupToggle.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /*画图*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画背景*/
        if (mBackgroupToggle != null) {
            canvas.drawBitmap(mBackgroupToggle, 0, 0, null);
        }
        /*画滑块*/
        if (mBackgroupSilde == null) {
//            canvas.drawBitmap(mBackgroupSilde, 0, 0, null);
            return;
        }

        int mToggleWidth = mBackgroupToggle.getWidth();
        int mSlideWidth = mBackgroupSilde.getWidth();

        if (mState == STATE_DOWN || mState == STATE_MOVE) {
            /*按下和移动状态的时候*/

            if (!isOpened) {
                /*当前关闭状态*/
                if (mCurrentX < mSlideWidth / 2f) {
                /*如果点击的位置小于滑块左边的一半，滑块的位置为关闭*/
                    canvas.drawBitmap(mBackgroupSilde, 0, 0, null);
                } else {
                /*如果点击的位置大于滑块一半的,改变滑块的位置*/
                    float left = mCurrentX - mSlideWidth / 2f;
                    if (left > mToggleWidth - mSlideWidth) {
                        left = mToggleWidth - mSlideWidth;
                    }
                    canvas.drawBitmap(mBackgroupSilde, left, 0, null);
                }
            } else {
                /*打开状态*/
                if (mCurrentX > mToggleWidth / 2f) {
                    /*点击了滑块的中间的右边部分*/
                    canvas.drawBitmap(mBackgroupSilde, mToggleWidth - mSlideWidth, 0, null);
                } else {
                    /*点击滑块左边部分*/
                    float left = mCurrentX - mSlideWidth / 2f;
                    if (left <= 0) {
                        left = 0;
                    }
                    canvas.drawBitmap(mBackgroupSilde, left, 0, null);
                }
            }
        } else if (mState == STATE_UP) {
            if (!isOpened) {
                /*关闭状态*/
                if (mCurrentX > mToggleWidth / 2f) {
                    //打开
                    canvas.drawBitmap(mBackgroupSilde, mToggleWidth - mSlideWidth, 0, null);
                    isOpened = true;
                    if (mListener != null) {
                        mListener.onToggleSibleOpen(true);
                    }
                } else {
                    canvas.drawBitmap(mBackgroupSilde, 0, 0, null);
                }
            } else {
                /*打开状态*/
                if (mCurrentX > mToggleWidth - mSlideWidth / 2f) {
                    //打开
                    canvas.drawBitmap(mBackgroupSilde, mToggleWidth - mSlideWidth, 0, null);
                } else {
                    canvas.drawBitmap(mBackgroupSilde, 0, 0, null);

                    isOpened = false;
                    if (mListener != null) {
                        mListener.onToggleSibleOpen(false);
                    }
                }
            }
        } else {
            /*没有状态*/
            if (isOpened) {
                /*打开状态*/
                canvas.drawBitmap(mBackgroupSilde, mToggleWidth - mSlideWidth, 0, null);
            } else {
                /*关闭状态*/
                canvas.drawBitmap(mBackgroupSilde, 0, 0, null);
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
                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;
                mCurrentX = event.getX();

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;
                mCurrentX = event.getX();

                invalidate();
                break;
        }
        return true;
    }

    //暴露接口
    public void setOnToggleOpenListener(OnToggleOpenListener listener) {
        mListener = listener;
    }

    public interface OnToggleOpenListener {
        void onToggleSibleOpen(boolean isOpened);
    }
}
