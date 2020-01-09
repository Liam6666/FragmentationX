package me.liam.fragmentation.demo.zhihu;

import android.os.Bundle;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;

import androidx.annotation.Nullable;
import me.liam.fragmentation.demo.R;
import me.liam.fragmentation.support.SupportActivity;

/**
 * Create on 2020/1/6.
 */
public class ZhiHuActivity extends SupportActivity {

    private RootFragment1 rootFragment1;
    private RootFragment2 rootFragment2;
    private BottomNavigationBar bottom_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_root);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        bottom_bar = findViewById(R.id.bottom_bar);
        BottomBarItem item = new BottomBarItem(R.mipmap.home, R.string.title1);
        BottomBarItem item3 = new BottomBarItem(R.mipmap.user, R.string.title3);
        bottom_bar.addTab(item);
        bottom_bar.addTab(item3);
        bottom_bar.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                if (position == 0) {
                    showHideAllFragment(rootFragment1);
                }
                if (position == 1) {
                    showHideAllFragment(rootFragment2);
                }
            }
        });
        if (findFragment(RootFragment1.class) == null) {
            rootFragment1 = RootFragment1.newInstance();
            rootFragment2 = RootFragment2.newInstance();
            loadMultipleRootFragments(R.id.container, 1, rootFragment1, rootFragment2);
        } else {
            rootFragment1 = findFragment(RootFragment1.class);
            rootFragment2 = findFragment(RootFragment2.class);
        }
    }
}
