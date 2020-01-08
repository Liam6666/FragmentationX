package viewpager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import me.liam.fragmentation.R;
import me.liam.support.SupportActivity;

/**
 * Create on 2020/1/8.
 */
public class ViewPagerActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp_root);
        if (findFragmentByClass(ViewPagerFragment.class) == null){
            loadRootFragment(R.id.container,ViewPagerFragment.newInstance());
        }
    }
}
