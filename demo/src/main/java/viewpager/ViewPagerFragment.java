package viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

/**
 * Create on 2020/1/8.
 */
public class ViewPagerFragment extends SupportFragment {

    public static ViewPagerFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_vp_root,null);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("1"));
        tabLayout.addTab(tabLayout.newTab().setText("2"));
        tabLayout.addTab(tabLayout.newTab().setText("3"));
        tabLayout.addTab(tabLayout.newTab().setText("4"));
        tabLayout.addTab(tabLayout.newTab().setText("5"));
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return ItemPageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
