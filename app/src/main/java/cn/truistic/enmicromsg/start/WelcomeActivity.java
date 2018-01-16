package cn.truistic.enmicromsg.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.xdandroid.hellodaemon.IntentWrapper;

import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.base.BaseActivity;
import cn.truistic.enmicromsg.common.util.SharedPerfUtil;
import cn.truistic.enmicromsg.main.ui.MainActivity;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        IntentWrapper.whiteListMatters(this, "微数据服务的持续运行");
    }

    private void initView() {
        Button btn_start = (Button) findViewById(R.id.welcome_btn_start);
        btn_start.setOnClickListener(v -> {
            SharedPerfUtil.saveIsFirstStart(WelcomeActivity.this, false);
            Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }

}
