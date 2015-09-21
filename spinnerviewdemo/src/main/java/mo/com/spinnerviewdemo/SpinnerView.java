package mo.com.spinnerviewdemo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * 作者：MoMxMo on 2015/9/21 16:25
 * 邮箱：xxxx@qq.com
 */


public class SpinnerView extends RelativeLayout {

    private static final String TAG = "SpinnerView";
    private EditText mInput;
    private ImageView mArrow;
    private Context mConent;
    private BaseAdapter mAdatper;
    private PopupWindow window;
    private ListView mContentView;
    private AdapterView.OnItemClickListener mListener;

    public SpinnerView(Context context) {
        this(context, null);
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mConent = context;
        View.inflate(context, R.layout.spinner_view, this);

        initView();
        initEvent();
    }

    private void initEvent() {
        mArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick 点击下拉");


                /*创建listView对象*/
                if (mContentView == null) {
                    mContentView = new ListView(mConent);
                    mContentView.setBackgroundResource(R.drawable.shape_input);
                }
                if (mAdatper != null) {
                    mContentView.setAdapter(mAdatper);
                    if (mListener != null) {
                        mContentView.setOnItemClickListener(mListener);
                    }
                }


//                int width = getWidth();

                int width = mInput.getWidth();
                int height = 800;
                /*设置ListView的显示位置*/
                if (window == null) {
                    window = new PopupWindow(mContentView, width, height);
                }
                /*获取焦点*/
                window.setFocusable(true);
                /*外侧可以点击*/
                window.setOutsideTouchable(true);
                /*设置背景*/
                window.setBackgroundDrawable(new ColorDrawable());
                /*设置显示的位置*/
                window.showAsDropDown(mInput,-2,0);


            }
        });
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListener =  listener;
    }

    /*关闭PupopWindow*/
    public void dismiss() {
        if (window != null) {
            window.dismiss();
        }
    }

    public void setText(String data) {
        mInput.setText(data);
    }

    private void initView() {
        mInput = (EditText) findViewById(R.id.et_input);
        mArrow = (ImageView) findViewById(R.id.iv_arrow);
    }

    public void setAdatper(BaseAdapter adapter) {
        mAdatper = adapter;
    }

    public void setTextSelected(int index) {
        mInput.setSelection(index);
    }


}
