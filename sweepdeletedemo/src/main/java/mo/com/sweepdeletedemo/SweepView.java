package mo.com.sweepdeletedemo;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 作者：MoMxMo on 2015/9/19 23:50
 * 邮箱：xxxx@qq.com
 * <p/>
 * 删除滑动的自定义控件
 */


public class SweepView extends ViewGroup {

    private View mContentView;
    private View mDeleteView;
    private int mDeleteWidth;
    private int mDeleteHeight;
    private float mDownY;
    private float mDownX;
    private Scroller mScroller;
    private ViewDragHelper mDragHelper;
    private OnSweepListener mSweepListener;
    private boolean isOpened = false;
    public SweepView(Context context) {
        this(context, null);
    }
    public SweepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*使用v4提供的工具类
        * 第一个参数：要分析的ViewGroup
        * 第二个参数：分析的结果回调
        * */
        mDragHelper = ViewDragHelper.create(this, new SweeViewCallBack());
        // 老的方式   mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        /*获取孩子View或者ViewGroup对象*/
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);

        /*获取删除的位置*/
        LayoutParams params = mDeleteView.getLayoutParams();
        mDeleteWidth = params.width;
        mDeleteHeight = params.height;
    }

    /*测量Child的位置*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 解析一下：widthMeasureSpec
         * widthMeasureSpec中的前2位是表示期望模式
         * widthMeasureSpec中的后面30位表示像素宽度（二进制------十进制--->200px）
         *
         * UNSPECIFEI :未指明的，
         * EXACTLY  精确的，希望你的宽或者高为精确的（1001px）
         * AT_MOST:     最大，希望你的宽度或高不要超过（200）
         */
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        /*内容*/
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

        /*删除*/
        mDeleteView.measure(MeasureSpec.makeMeasureSpec(mDeleteWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mDeleteHeight, MeasureSpec.EXACTLY));

        /*控件本身*/
        setMeasuredDimension(mWidthSize, mHeightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*设置孩子的布局*/
        /*内容*/
        int mLeft = 0;
        int mTop = 0;
        int mRight = mContentView.getMeasuredWidth();
        int mBottom = mContentView.getMeasuredHeight();
        mContentView.layout(mLeft, mTop, mRight, mBottom);
        /*删除*/
        int dLeft = mContentView.getMeasuredWidth();
        int dTop = 0;
        int dRight = mContentView.getMeasuredWidth() + mDeleteView.getMeasuredWidth();
        int dBottom = mDeleteView.getMeasuredHeight();
        mDeleteView.layout(dLeft, dTop, dRight, dBottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*分析touch事件*/
        mDragHelper.processTouchEvent(event);
        /*消费*/
        return true;
    }

    /*ViewGroup触摸事件的回调*/
    private class SweeViewCallBack extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            /**参数的分析：
             * child    获得触摸焦点的child孩子，是否需要分析
             * pointerId 触摸点的id标记
             */
            /*内容和删除部分的触摸都要分析*/
            return child == mContentView || child == mDeleteView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            /**
             *参数分析
             * child    被拖拽的VIew
             * left     被拖拽的VIew,的x方向到父容器x方向的距离
             * dx    x方向的增量
             */
            /*点击的是content部分的滑动，防止内容左边越界*/
            if (child == mContentView) {
                if (left > 0) {
                    /*防止内容左边越界*/
                    left = 0;
                } else if (left < -mDeleteWidth) {
                     /*防止删除右边的越界*/
                    left = -mDeleteWidth;
                }
            } else if (child == mDeleteView) {
                /*点击的是删除的部分*/
                if (left < mContentView.getMeasuredWidth() - mDeleteWidth) {
                    /*防止右边越界*/
                    left = mContentView.getMeasuredWidth() - mDeleteWidth;
                } else if (left > mContentView.getMeasuredWidth()) {
                    /*防止左边越界*/
                    left = mContentView.getMeasuredWidth();
                }
            }
            return left;
        }

        /*当view发生改变时的回调*/
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            /*更新UI的显示*/
            /*invalidate();*/
            ViewCompat.postInvalidateOnAnimation(SweepView.this);

            //如果当前内容移动，删除部分也要跟着移动
            if (changedView == mContentView) {
                int dLeft = mContentView.getMeasuredWidth() + left;
                int dTop = 0;
                int dRight = mContentView.getMeasuredWidth() + mDeleteWidth + left;
                int dBottom = mDeleteHeight;
                mDeleteView.layout(dLeft, dTop, dRight, dBottom);
            } else {
                /*如果移动的是删除部分，内容部分也要跟着移动*/
                int dLeft = -(mContentView.getMeasuredWidth() - left);
                int dTop = 0;
                int dRight = left;
                int dBottom = mContentView.getMeasuredHeight();
                mContentView.layout(dLeft, dTop, dRight, dBottom);
            }

            /*判断删除是否打开    这里使用标记，防止每次都去提醒，调用，避免过度啰嗦*/
            if (mContentView.getLeft() == -mDeleteWidth && !isOpened) {
                /*如果内容的左边离0左边正好等于Delete的宽度,说明目前状态是打开的*/
                if (mSweepListener!=null) {
                    mSweepListener.onOpenDelete(SweepView.this,true);
                }
                isOpened = true;
            }else if (mContentView.getLeft() == 0 && isOpened) {
                /*如果内容的左边距刚好等于零，说明目前删除状态是关闭的*/
                if (mSweepListener != null) {
                    mSweepListener.onOpenDelete(SweepView.this,false);
                }
                isOpened = false;
            }

            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /*松开View时的回调*/
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            // up时回调
            // releasedChild:松开的view
            // xvel:x方向的速率
            // yvel:y方向的速率

            /*如果删除部分显示大于一般,者显示删除*/
            if (mDeleteWidth / 2f < -mContentView.getX()) {
                showDeleteView(true, xvel, yvel);
            } else {
            /*如果删除部分显示小于一半，不显示删除部分*/
                showDeleteView(false, xvel, yvel);
            }
        }
    }

    private void showDeleteView(boolean openDelete, float xvel, float yvel) {
        /*显示删除部分,重新定义布局位置*/
        if (openDelete) {
            /*内容*/
            /*模拟数据变化*/
            mDragHelper.smoothSlideViewTo(mContentView, -mDeleteWidth, 0);
            // 删除
             /*模拟数据变化*/
            mDragHelper.smoothSlideViewTo(mDeleteView, mContentView.getMeasuredWidth() - mDeleteWidth, 0);
        } else {
         /*不显示删除部分，重新定义布局位置*/
        /*内容*/
             /*模拟数据变化*/
            mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
        /*删除*/
             /*模拟数据变化*/
            mDragHelper.smoothSlideViewTo(mDeleteView, mContentView.getMeasuredWidth(), 0);
        }

        /*更新UI*/
        // invalidate();   //computeScroll()触发这个方法
        /*国内一些机型不一样的原因，所以我们使用
        但是这这种方式消耗资源比较大
         ViewCompat.postInvalidateOnAnimation*/
        ViewCompat.postInvalidateOnAnimation(SweepView.this);

    }


    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            /*不断的更新*/
            /*invalidate();*/
            ViewCompat.postInvalidateOnAnimation(SweepView.this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY  = ev.getX();
                mDownX = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = ev.getX();
                float mMoveY = ev.getY();

                /*水平滑动大于垂直的时候*/
                if (Math.abs(mMoveX - mDownX) > Math.abs(mMoveY - mDownY)) {
                    // 水平滑动，请求父容器不要拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    // 垂直滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /*暴露删除开关的接口*/
    public void setOnSweepListener(OnSweepListener listener) {
        mSweepListener = listener;
    }

    /*删除开关监听器*/
    public interface OnSweepListener {
        //删除是否打开
         void onOpenDelete(SweepView currentSweepView,boolean isOpen);
    }

    /*关闭删除*/
    public void close(boolean startAnimation) {
        /*关闭，并执行动画*/
        if (startAnimation) {
            /*模拟数据变化*/
            mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
        /*删除*/
             /*模拟数据变化*/
            mDragHelper.smoothSlideViewTo(mDeleteView, mContentView.getMeasuredWidth(), 0);
        } else {

            /*还原到原来的布局*/
              /*内容*/
            int mLeft = 0;
            int mTop = 0;
            int mRight = mContentView.getMeasuredWidth();
            int mBottom = mContentView.getMeasuredHeight();
            mContentView.layout(mLeft, mTop, mRight, mBottom);

             /*删除*/
            int dLeft = mContentView.getMeasuredWidth();
            int dTop = 0;
            int dRight = mContentView.getMeasuredWidth() + mDeleteView.getMeasuredWidth();
            int dBottom = mDeleteView.getMeasuredHeight();
            mDeleteView.layout(dLeft, dTop, dRight, dBottom);

            isOpened = false;
            if (mSweepListener != null) {
                mSweepListener.onOpenDelete(this,false);
            }
        }

        /*刷新数据*/
        ViewCompat.postInvalidateOnAnimation(SweepView.this);
    }


    /*    *//*设置触摸事件*//*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = event.getX();
                float mMoveY = event.getY();

                *//*计算移动的X方向的偏移量*//*
                int deviation = (int) (mDownX - mMoveX + 0.5f);

                *//*计算临界点的位置*//*
                int scrollX = getScrollX();
                *//*左边方向*//*
                if (scrollX < 0) {
                    scrollTo(0, 0);
                } else if (scrollX + deviation > mDeleteWidth) {
                *//*右边方向*//*
                    scrollTo(mDeleteWidth, 0);
                } else {
                    scrollBy(deviation, 0);
                }
                mDownX = mMoveX;
                mDownY = mMoveY;
                break;
            case MotionEvent.ACTION_UP:
                *//*当用户松开手的时候，判断delete显示的部分mDeleteWidth*//*

                int currentX = getScrollX();
                if (currentX > mDeleteWidth / 2f) {
                    openDelete(true);
                } else {
                    openDelete(false);
                }
                break;
        }
        *//*消费*//*
        return true;
    }

    *//*是否打开删除*//*
    private void openDelete(boolean open) {
        if (open) {
              *//*显示菜单*//*
            *//*设置模拟滚动，为了更好的用户体验，使用Scroller滑动类*//*
            *//**
     * 参数介绍：
     * int startX  滑块开始的X位置
     * int startY   滑块开始的Y位置
     * int dx       开始和结束点的x增量
     * int dy        开始和结束点的y增量
     * int duration    模拟滑动的时间
     *//*
            int startX = getScrollX();
            int startY = getScrollY();
            int dx = mDeleteWidth - startX;
            int dy = 0;
            int duration = Math.abs(dx)*10;
            if (duration>500) {
                duration = 500;
            }
            *//*模拟器*，没有真正执行*//*
            mScroller.startScroll(startX, startY, dx, dy, duration);
            *//*更新UI*//*
            invalidate();
        } else {

            int startX = getScrollX();
            int startY = getScrollY();
            int dx = 0 - startX;
            int dy = 0;
            int duration = Math.abs(dx)*10;
            if (duration>500) {
                duration = 500;
            }
            *//*模拟器*，没有真正执行*//*
            mScroller.startScroll(startX, startY, dx, dy, duration);
            *//*更新UI*//*
            invalidate();

            *//*scrollTo(0, 0);*//*
        }
    }

    *//*计算滑动*//*
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
        *//*实时更新UI*//*
            invalidate();
        }
    }*/
}
