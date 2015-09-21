package mo.com.toggleviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToggleView mToggleView = (ToggleView) findViewById(R.id.toggle_view);

        /*设置背景和滑动图片*/
        mToggleView.setBackgroudToggle(R.drawable.switch_background);
        mToggleView.setBackgroupSilde(R.drawable.slide_button_background);

        /*监听Toggle开关状态*/
        mToggleView.setOnToggleOpenListener(new ToggleView.OnToggleOpenListener() {
            @Override
            public void onToggleSibleOpen(boolean isOpened) {
                Toast.makeText(MainActivity.this, isOpened?"is open":"is not open", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
