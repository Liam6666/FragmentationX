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
public class RootFragment2 extends SupportFragment {

    public static RootFragment2 newInstance() {

        Bundle args = new Bundle();

        RootFragment2 fragment = new RootFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_zhihu_root2, null);
        if (findChildFragment(MineFragment.class) == null) {
            loadRootFragment(R.id.root2, MineFragment.newInstance());
        }
        return rootView;
    }
}
