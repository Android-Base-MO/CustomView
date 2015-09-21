package mo.com.spinnerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SpinnerView mSpinner;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinner = (SpinnerView) findViewById(R.id.sv_spinner);
        initData();
        initEvent();
    }

    private void initEvent() {
        mSpinner.setAdatper(new SpinnerAdapter());

        mSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSpinner.dismiss();
                String data = mData.get(position);
                mSpinner.setText(data);
                mSpinner.setTextSelected(data.length());
            }
        });
    }

    private void initData() {
        if (mData == null) {
            mData = new ArrayList<String>();
        } else {
            mData.clear();
        }
        for (int i = 0; i < 15; i++) {
            mData.add("10246301" + i);
        }
    }

    private class SpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mData != null) {
                mData.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_contact_number, null);
                mHolder.mNumber = (TextView) convertView.findViewById(R.id.tv_number);
                mHolder.mIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                mHolder.mDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            String data = mData.get(position);
            mHolder.mIcon.setImageResource(R.drawable.user);
            mHolder.mNumber.setText(data);
            mHolder.mDelete.setImageResource(R.drawable.delete);

            /*点击删除条目*/
            mHolder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    notifyDataSetChanged();
                    mSpinner.dismiss();
                }
            });

            return convertView;
        }
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mNumber;
        ImageView mDelete;
    }




}
