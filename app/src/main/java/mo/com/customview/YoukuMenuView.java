package mo.com.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 作者：MoMxMo on 2015/9/17 20:41
 * 邮箱：xxxx@qq.com
 */


public class YoukuMenuView extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "YoukuMenuView";
    private ImageView youku_home_level1;
    private ImageView youku_home_level2;
    private RelativeLayout youku_level1;
    private RelativeLayout youku_level2;
    private RelativeLayout youku_level3;

    private boolean isLevel1Display = true;    // 标记一级菜单是否显示
    private boolean isLevel2Display = true;   // 标记二级菜单是否显示
    private boolean isLevel3Display = true;    // 标记三级菜单是否显示

    int mAnimationCount;    //正在做动画的个数

    public YoukuMenuView(Context context) {
        super(context);
    }

    public YoukuMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.youkumeun_view, this);

        // 初始化View
        initView();

        //设置监听事件
        initEvent();


    }

    private void initEvent() {
        youku_home_level1.setOnClickListener(this);
        youku_home_level2.setOnClickListener(this);
    }

    private void initView() {
        /*图片VIew*/
        youku_home_level1 = (ImageView) findViewById(R.id.youku_home_level1);
        youku_home_level2 = (ImageView) findViewById(R.id.youku_home_level2);

        /*布局容器*/
        youku_level1 = (RelativeLayout) findViewById(R.id.youku_level1);
        youku_level2 = (RelativeLayout) findViewById(R.id.youku_level2);
        youku_level3 = (RelativeLayout) findViewById(R.id.youku_level3);
    }

    @Override
    public void onClick(View v) {
        if (v == youku_home_level1) {
            Log.i(TAG, "onClick  Home点击。。。。。");
            // 点击一级菜单
            clickHome();
        } else if (v == youku_home_level2) {
            //点击二级菜单
            clickMeun();

        }
    }

    //点击一级菜单
    /**
     *点击时:如果一级二级三级菜单都显示，关闭二级三级菜单
     *如果一级二级菜单都显示，关闭二级菜单
     * 如果二级三级菜单不显示，打开二级菜单
     */
    private void clickHome() {

        /*如果正在做动画，不响应点击事件*/
        if (mAnimationCount > 0) {
            return;
        }

        //如果一级二级三级菜单都显示，关闭二级三级菜单
        if (isLevel1Display && isLevel2Display && isLevel3Display) {
            //关闭二三级
            hideAnimation(youku_level2, 300);
            hideAnimation(youku_level3, 200);

            //标记
            isLevel2Display = false;
            isLevel3Display = false;

        } else if (isLevel1Display && isLevel2Display && !isLevel3Display) {
            //如果一级二级菜单都显示，关闭二级菜单
            hideAnimation(youku_level2, 200);

            /*标记显示*/
            isLevel2Display = false;
        } else if (!isLevel2Display && !isLevel3Display && isLevel1Display) {
            //显示二级
            showAnimation(youku_level2, 200);

            /*标记显示*/
            isLevel2Display = true;
        }
    }
    //点击二级菜单
    /**
     * 点击时：如果三级菜单显示，关闭三级菜单
     * 如果三级菜单关闭，显示三级菜单
     */
    private void clickMeun() {

            /*如果正在做动画，不响应点击事件*/
        if (mAnimationCount > 0) {
            return;
        }

        if (isLevel2Display && isLevel3Display) {
            //如果三级菜单显示，关闭三级菜单
            hideAnimation(youku_level3,300);

            //标记
            isLevel3Display = false;
        }else if (isLevel2Display && !isLevel3Display ) {
            //如果三级菜单关闭，显示三级菜单
            showAnimation(youku_level3,300);

            //标记
            isLevel3Display = true;
        }

    }

    /**
     * 执行隐藏动画
     * @param container      动画执行的对象
     * @param delay         延时
     */
    private void hideAnimation(RelativeLayout container, int delay) {
        Log.i(TAG, "enterAnimation 执行进入动画");

        //获取容器的宽高
        int width = container.getWidth();
        int height = container.getHeight();

        //设置旋转的圆心
        container.setPivotX(width / 2f);
        container.setPivotY(height);

        ObjectAnimator animator = ObjectAnimator.ofFloat(container, "rotation", 0, -180);

        //设置动画执行时间
        animator.setDuration(600);

        //设置时延
        animator.setStartDelay(delay);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationCount++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationCount--;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }
    /**
     * 执行显示动画
     * @param container      动画执行的对象
     * @param delay         延时
     */
    private void showAnimation(RelativeLayout container, int delay) {
        Log.i(TAG, "enterAnimation 执行进入动画");

        //获取容器的宽高
        int width = container.getWidth();
        int height = container.getHeight();

        //设置旋转的圆心
        container.setPivotX(width / 2f);
        container.setPivotY(height);
        ObjectAnimator animator = ObjectAnimator.ofFloat(container, "rotation", -180, 0);

        //设置动画执行时间
        animator.setDuration(600);

        //设置时延
        animator.setStartDelay(delay);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationCount++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationCount--;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    /**
     * 点击硬件菜单
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //点击硬件菜单
            Log.i(TAG, "onKeyUp 硬件菜单被点击");

            //执行点击业务
            clickHardWareMenu();

            //不将点击世家据需向下传递
        }
        return true;
    }


    /**
     * 如果一级二级三级菜单都显示，隐藏所有菜单
     * 如果一级二级三级菜单都隐藏，显示所有菜单
     * 如果一级二级菜单都显示，隐藏一级二级菜单
     * 如果一级菜单显示，隐藏一级菜单
     */
    private void clickHardWareMenu() {

        //如果一级二级三级菜单都显示，隐藏所有菜单
        if (isLevel1Display && isLevel2Display && isLevel3Display) {
            //隐藏所有菜单
            hideAnimation(youku_level1,0);
            hideAnimation(youku_level2,200);
            hideAnimation(youku_level3,300);

            //标记
            isLevel1Display = false;
            isLevel2Display = false;
            isLevel3Display = false;
        }else if (!isLevel1Display && !isLevel2Display && !isLevel3Display) {
            //如果一级二级三级菜单都隐藏，显示所有菜单

            //显示所有菜单
            showAnimation(youku_level1,300);
            showAnimation(youku_level2,200);
            showAnimation(youku_level3,0);

            //标记
            isLevel1Display = true;
            isLevel2Display = true;
            isLevel3Display = true;
        }else if (isLevel1Display && isLevel2Display && !isLevel3Display) {
            //如果一级二级菜单都显示，隐藏一级二级菜单

            //隐藏一级二级菜单
            hideAnimation(youku_level1,300);
            hideAnimation(youku_level2,200);

            //标记
            isLevel1Display = false;
            isLevel2Display = false;
        }else if (isLevel1Display && !isLevel2Display && !isLevel3Display) {
            //如果一级菜单显示，隐藏一级菜单
            hideAnimation(youku_level1,0);

            //标记
            isLevel1Display = false;

        }


    }
}
