package mo.com.slidemeun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView iv_news;
    private SlideMeunView menu_smv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        iv_news = (ImageView) findViewById(R.id.iv_news);
        menu_smv = (SlideMeunView) findViewById(R.id.menu_smv);
    }

    private void initEvent() {
        iv_news.setOnClickListener(this);
        menu_smv.setOnOpenMeunListener(new SlideMeunView.OnOpenMeunListener() {
            @Override
            public void onIsOpenMeun(boolean isOpen) {
                Toast.makeText(MainActivity.this, isOpen ? "is  open" : "not is open ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void clickItem(View view) {
        TextView v = (TextView) view;
        Toast.makeText(MainActivity.this, v.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (menu_smv.mIsOpen) {
        /*如果菜单是打开的，点击之后关闭*/
            menu_smv.showMenu(false);
        } else {
        /*如果菜单是关闭的，点击之后打开*/
            menu_smv.showMenu(true);
        }
    }
}
