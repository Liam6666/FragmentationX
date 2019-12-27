package me.liam.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;
import me.liam.swipeback.SwipeBackLayout;

/**
 * Created by Augustine on 2019/12/27.
 * <p>
 * email:nice_ohoh@163.com
 */
public class SwipebackFragment extends SupportFragment {

    public static SwipebackFragment newInstance() {

        Bundle args = new Bundle();

        SwipebackFragment fragment = new SwipebackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SwipeBackLayout rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new SwipeBackLayout(getContext());
        View view = View.inflate(getContext(), R.layout.f1,null);
        rootView.setContentView(view);
        return rootView;
    }
}
