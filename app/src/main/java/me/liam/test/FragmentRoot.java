package me.liam.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.anim.FragmentAnimation;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

public class FragmentRoot extends SupportFragment {

    public static FragmentRoot newInstance() {

        Bundle args = new Bundle();

        FragmentRoot fragment = new FragmentRoot();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_root,null);
        TextView textView = rootView.findViewById(R.id.tv);
        textView.setBackgroundColor(ColorUtils.getRandomColor(false));
        textView.setText(getFragmentManager().getFragments().size()+"");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Random().nextBoolean()){
                    start(FragmentRoot.newInstance());
                }else {
                    start(SettingsFragment.newInstance());
                }
            }
        });
        rootView.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Random().nextBoolean()){
                    start(FragmentRoot.newInstance());
                }else {
                    start(SettingsFragment.newInstance());
                }
            }
        });
        rootView.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Random().nextBoolean()){
                    start(FragmentRoot.newInstance());
                }else {
                    start(SettingsFragment.newInstance());
                }
            }
        });
        rootView.findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Random().nextBoolean()){
                    start(FragmentRoot.newInstance());
                }else {
                    start(SettingsFragment.newInstance());
                }
            }
        });
        return rootView;
    }

//    @Override
//    public FragmentAnimation onCreateCustomerAnimation() {
//        return new FragmentAnimation(R.anim.classic_vertical_enter,R.anim.classic_vertical_exit,
//                R.anim.classic_vertical_popenter,R.anim.classic_vertical_popexit);
//    }
}
