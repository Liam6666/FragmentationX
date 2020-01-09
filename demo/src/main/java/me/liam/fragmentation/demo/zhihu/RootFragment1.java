package me.liam.fragmentation.demo.zhihu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.demo.R;
import me.liam.fragmentation.support.SupportFragment;

/**
 * Create on 2020/1/6.
 */
public class RootFragment1 extends SupportFragment {

    public static RootFragment1 newInstance() {

        Bundle args = new Bundle();

        RootFragment1 fragment = new RootFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_zhihu_root1, null);
        if (findChildFragment(HomePageFragment.class) == null) {
            loadRootFragment(R.id.root1, HomePageFragment.newInstance());
        }
        return rootView;
    }
}
