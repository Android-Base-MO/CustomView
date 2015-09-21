package mo.com.sweepdeletedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListViewp;
    private List<String> mDataList;
    private SweepAdapter mSweepAdapter;
    private List<SweepView> mSweepViewList = new ArrayList<SweepView>();
    private int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListViewp = (ListView) findViewById(R.id.ll_sweep);
        /*加载数据*/
        initData();
        /*设置监听事件*/
        initEvent();
    }
    private void initEvent() {
        mSweepAdapter = new SweepAdapter();
        mListViewp.setAdapter(mSweepAdapter);
        mListViewp.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    closeAll(true);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initData() {
        if (mDataList == null) {
            mDataList = new ArrayList<String>();
        } else {
            mDataList.clear();
        }
        for (int i = 0; i < 16; i++) {
            mDataList.add("内容" + i);
        }
    }
    private class SweepAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mDataList != null) {
                return mDataList.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            if (mDataList != null) {
                return mDataList.get(position);
            }
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mholder = null;
            if (convertView == null) {
                mholder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_sweep, null);
                mholder.tvContent = (TextView) convertView.findViewById(R.id.item_tv_content);
                mholder.tvDelete = (TextView) convertView.findViewById(R.id.item_tv_delete);
                mholder.sweepView = (SweepView) convertView.findViewById(R.id.item_sv);
                convertView.setTag(mholder);
            } else {
                mholder = (ViewHolder) convertView.getTag();
            }
            final String content = (String) getItem(position);
            mholder.tvContent.setText(content);
            mholder.tvDelete.setText("删除");

            /*监听条目状态*/
            mholder.sweepView.setOnSweepListener(new SweepView.OnSweepListener() {
                @Override
                public void onOpenDelete(SweepView sweepView, boolean isOpen) {

                    Toast.makeText(MainActivity.this, isOpen?"打开"+content:"关闭", Toast.LENGTH_SHORT).show();
                    if (isOpen) {
                        /*记录当前的，关闭上一个*/
                        closeAll(true);
                        mSweepViewList.add(sweepView);
                        currentPosition = position;
                    } else {
                        mSweepViewList.remove(sweepView);
                    }
                }
            });
            return convertView;
        }
    }

    /*关闭所有，并判断是否执行动画*/
    private void closeAll(boolean isAnimation) {

        Iterator<SweepView> iterator = mSweepViewList.listIterator();
        while (iterator.hasNext()) {
            SweepView next = iterator.next();
            next.close(isAnimation);
        }
    }
    private class ViewHolder {
        TextView tvContent;
        TextView tvDelete;
        SweepView sweepView;
    }
}
