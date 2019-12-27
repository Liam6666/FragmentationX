package me.liam.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.blankj.utilcode.util.ToastUtils;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.anim.FragmentAnimation;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

public class SettingsFragment extends SupportFragment {

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_settings,null);
        Toolbar toolbar = rootView.findViewById(R.id.toolBar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rootView.findViewById(R.id.image1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Random().nextBoolean()){
                    ToastUtils.showShort("start fragment");
                    start(SettingsFragment.newInstance());
                }else {
                    ToastUtils.showShort("Load child fragment");
                    loadRootFragment(R.id.container,SettingsFragment.newInstance());
                }
            }
        });
        return attachSwipeBack(rootView);
    }

//    @Override
//    public FragmentAnimation onCreateCustomerAnimation() {
//        return new FragmentAnimation(R.anim.classic_vertical_enter,R.anim.classic_vertical_exit,
//                R.anim.classic_vertical_popenter,R.anim.classic_vertical_popexit);
//    }
}
