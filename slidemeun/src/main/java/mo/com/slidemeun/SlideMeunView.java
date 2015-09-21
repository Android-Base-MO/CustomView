package mo.com.slidemeun;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 作者：MoMxMo on 2015/9/19 19:53
 * 邮箱：xxxx@qq.com
 * <p/>
 * 新闻滑动的菜单
 */

public class SlideMeunView extends ViewGroup {
    private static final String TAG = "SlideMeunView";
    private View mMeunView;
    private View mContentView;
    private int mMeunWidth;
    public  boolean mIsOpen = false;
    private OnOpenMeunListener mListener;
    float mDownX = 0;
    float mDownY = 0;
    private Scroller mScroller;
    public SlideMeunView(Context context) {
        this(context, null);
    }

    public SlideMeunView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    //加载子布局文件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMeunView = getChildAt(0);
        mContentView = getChildAt(1);
        /*获取菜单的宽度值*/
        LayoutParams params = mMeunView.getLayoutParams();
        mMeunWidth = params.width;
    }

    //测量子View的布局
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //widthMeasureSpec, heightMeasureSpec这两个参数是SlideMeunView的父容器提供的
        // 首先，我们要了解一下widthMeasureSpec的数据分段解析，32为的二进制
        /**
         * 解析一下：widthMeasureSpec
         * widthMeasureSpec中的前2位是表示期望模式
         * widthMeasureSpec中的后面30位表示像素宽度（二进制------十进制--->200px）
         *
         * UNSPECIFEI :未指明的，
         * EXACTLY  精确的，希望你的宽或者高为精确的（1001px）
         * AT_MOST:     最大，希望你的宽度或高不要超过（200）
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  //宽的期望模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  //宽的真实大小

        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);    //高的期望模式
        int HeightSize = MeasureSpec.getSize(heightMeasureSpec);    //高的真实大小

        //菜单部分
        mMeunView.measure(MeasureSpec.makeMeasureSpec(mMeunWidth, MeasureSpec.EXACTLY), heightMeasureSpec);

        //内容部分,第一个页面显示的刚好是整个内容的页面
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

        //设置SlidemeunView的布局（自己），真实的大小,整个布局中的大小是包含菜单和内容的，
        // 所以宽度的mMenuVIiew和mContentView 的总宽度
        //高度都是一样的
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    //设置子view或者viewgroup的摆放位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*获取菜单mMenuView测量之后的宽度*/
        int width = mMeunView.getMeasuredWidth();
        int height = mMeunView.getMeasuredHeight();
        //菜单部分
        int mLeft = -width;
        int mTop = 0;
        int mRight = 0;
        int mBottom = height;
        mMeunView.layout(mLeft, mTop, mRight, mBottom);
        //内容部分
        int cLeft = 0;
        int cTop = 0;
        int cBottom = mContentView.getMeasuredHeight();
        int cRight = mContentView.getMeasuredWidth();
        mContentView.layout(cLeft, cTop, cRight, cBottom);
    }

    //触摸事件的拦截  拦截孩子的Touch事件
    // (当Child View或者ViewGroup也有点击事件的时候，设置拦截的规则)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = ev.getX();
                float mMoveY = ev.getY();
                if (Math.abs(mMoveX - mDownX) > Math.abs(mMoveY - mDownY)) {
                    /*如果X方向的滑动大于Y方向的滑动 ， 拦截触摸事件*/
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //触摸滑动事件的监听
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getX();

                /*判断X方向的偏移*/
                int deviationX = (int) (mDownX - moveX + 0.5f);
                /*获取当前的Scroll  X方向的增量*/
                int scrollX = getScrollX();
                /*防止左边菜单越界*/
                if ((scrollX + deviationX) < -mMeunWidth) {
                    //如果当前屏幕的窗体已经超出菜单的左方向
                    scrollTo(-mMeunWidth, 0);
                } else if ((scrollX + deviationX) > 0) {
                    //如果当前屏幕的窗体已经超出菜单的左方向
                    scrollTo(0, 0);
                } else {
                    scrollBy(deviationX, 0);
                }
                mDownX = moveX;
                mDownY = moveY;
                break;
            //弹起
            case MotionEvent.ACTION_UP:
                /*判断当松开收的时候，应该显示的是菜单还是内容*/
                /*获取当前窗口X的位置*/
                int currentScrollX = getScrollX();
                if (currentScrollX < -mMeunWidth / 2f) {
                    /*如果窗口中已经显示操作菜单的一般，则直接显示菜单*/
                    showMenu(true);
                } else {
                    /*显示内容*/
                    showMenu(false);
                }
                break;
        }
        /*消费，注意，这里一定要返回true,不然是无法执行的*/
        return true;
    }

    /*是否菜单*/
    public void showMenu(boolean show) {
        if (mIsOpen != show) {
            if (mListener != null && !mIsOpen) {
                mListener.onIsOpenMeun(true);
            }
            if (mListener != null && mIsOpen) {
                mListener.onIsOpenMeun(false);
            }
        }
        mIsOpen = show;
        if (show) {
            /*显示菜单*/
            /*设置模拟滚动，为了更好的用户体验，使用Scroller滑动类*/
            /**
             * 参数介绍：
             * int startX  滑块开始的X位置
             * int startY   滑块开始的Y位置
             * int dx       开始和结束点的x增量
             * int dy        开始和结束点的y增量
             * int duration    模拟滑动的时间
             */

             /*获取当前滑块的位置*/
            int currentX = getScrollX();
            int startX = currentX;
            int startY = getScrollY();
            int dx = -mMeunWidth - currentX;
            int dy = 0;

            /*设置滑动的时间*/
            int duration = Math.abs(dx) * 10;
            if (duration > 500) {
                duration = 500;
            }
            mScroller.startScroll(startX, startY, dx, dy, duration);
            /*需要使用触发UI的更新*/
            invalidate();       //会执行下面的computeScroll()方法
        } else {
            /*显示内容*/
            int currentX = getScrollX();
            int startX = currentX;
            int startY = getScrollY();
            int dx = 0 - currentX;
            int dy = 0;
               /*设置滑动的时间*/
            int duration = Math.abs(dx) * 10;
            if (duration > 500) {
                duration = 500;
            }
            mScroller.startScroll(startX, startY, dx, dy, duration);
            invalidate();
        }
    }

    /*暴露接口*/
    public void setOnOpenMeunListener(OnOpenMeunListener listener) {
        mListener = listener;
    }

    /*设置菜单的监听类*/
    public interface OnOpenMeunListener {
        void onIsOpenMeun(boolean isOpen);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //正在模拟数据计算
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }
}
