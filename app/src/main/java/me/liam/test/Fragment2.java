package me.liam.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

public class Fragment2 extends SupportFragment {

    public static Fragment2 newInstance() {

        Bundle args = new Bundle();

        Fragment2 fragment = new Fragment2();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.f2,null);
        rootView.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("start fragment");
//                start(Fragment3.newInstance());
                getExtraTransaction()
                        .startWithPopTo(Fragment3.newInstance(),Fragment1.class,true);
            }
        });
        return attachSwipeBack(rootView);
    }
}
