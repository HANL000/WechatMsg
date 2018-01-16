package cn.truistic.enmicromsg.main.ui;




import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;


import com.xdandroid.hellodaemon.IntentWrapper;
import net.sqlcipher.database.SQLiteDatabase;
import java.util.ArrayList;
import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.base.BaseActivity;
import cn.truistic.enmicromsg.common.util.DeviceUtil;
/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase.loadLibs(this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setSupportActionBar(toolbar);

        HomeFragment homeFragment = new HomeFragment();
        ExportFragment exportFragment = new ExportFragment();
        GroupsFragment groupsFragment = new GroupsFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(exportFragment);
        fragments.add(groupsFragment);
        String[] titles = {getString(R.string.main_title_home),getString(R.string
                .main_title_export),getString(R.string.main_title_grouds)};
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    @Override
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }

    // TraceServiceImpl.stopService(); 需要停止服务的时候调用这个
}
