package me.liam.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        textView.setBackgroundColor(ColorUtils.getRandomColor());
        textView.setText(getFragmentManager().getFragments().size()+"");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(FragmentRoot.newInstance());
            }
        });
        return rootView;
    }
}
