package mo.com.picchangeview_pageviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private List<ImageView> mListData;
    private String[] strs = {"图片1", "图片2", "图片3", "图片4", "图片5"};
    private int[] img = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
    private TextView tv_title;
    private LinearLayout mPointContainer;
    private int item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         /*初始化*/
        initView();

        //加载数据
        initData();
        //初始化监事件
        initEvent();

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_pic_change);
        mPointContainer = (LinearLayout) findViewById(R.id.ll_order_circle);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void initData() {
        if (mListData == null) {
            mListData = new ArrayList<ImageView>();
        } else {
            mListData.clear();
        }
        for (int i = 0; i < img.length; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageDrawable(getResources().getDrawable(img[i]));
            mListData.add(imageView);

            //加载多个原点，并设置默认的原点
            View view = new View(this);
            //设置默认显示第一个原点
            view.setBackgroundResource(R.drawable.circle_normal);

            //设置View的属性
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                params.leftMargin = 5;
            } else {
                view.setBackgroundResource(R.drawable.circle_seletor);
                tv_title.setText(strs[i]);
            }
            mPointContainer.addView(view, params);
        }

        /*设置选中中间的页面*/
        item = Integer.MAX_VALUE / 2;
        item = item - item % mListData.size();
        mViewPager.setCurrentItem(item);


    }

    private void initEvent() {
        mViewPager.setAdapter(new ADAdapter());

        /*设置VIewPager的监听事件*/
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面活动
                // position:当前的页面
                // positionOffset:滑动的百分比
                // positionOffsetPixels:滑动的像素值

            }

            @Override
            public void onPageSelected(int position) {
                //页面被选择
                // position：选中的位置

                position = position % mListData.size();
                for (int i = 0; i < mPointContainer.getChildCount(); i++) {
                    View view = mPointContainer.getChildAt(i);
                    view.setBackgroundResource(i == position ? R.drawable.circle_seletor : R.drawable.circle_normal);
                }
                /*该变文本显示*/
                tv_title.setText(strs[position]);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态的改变
                // 当页面状态发生改变时的回调
                // * @see ViewPager#SCROLL_STATE_IDLE : 闲置
                // * @see ViewPager#SCROLL_STATE_DRAGGING : 拖拽
                // * @see ViewPager#SCROLL_STATE_SETTLING : 固定状态

            }
        });
    }
    private class ADAdapter extends PagerAdapter {
        // getCount():返回的是页面的数量
        @Override
        public int getCount() {
            if (mListData != null) {
                // return mDatas.size();
                return Integer.MAX_VALUE;// 返回最大数
            }
            return 0;
        }
        // 判断是否已经加载过了
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 展示的view
            // 标记
            return view == object;// 如果一致就是已经加载过了
        }

        // 初始化页面时候的回调
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 给viewpager加载当前页面的view和数据

            position = position % mListData.size();// 5 % 5 -->0
            ImageView view = mListData.get(position);

            // 把第position个view展示到ViewPager中
            container.addView(view);// 展示

            // 返回标记
            return view;// 标记
        }

        // 销毁页面时候的回调
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 给viewpager移除当前页面的view和数据

            // 销毁第position个
            container.removeView((View) object);
        }
    }
}
