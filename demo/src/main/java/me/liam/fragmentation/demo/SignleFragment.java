package me.liam.fragmentation.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import me.liam.fragmentation.demo.R;
import me.liam.fragmentation.support.SupportFragment;

/**
 * Created by Augustine on 2019/12/31.
 * <p>
 * email:nice_ohoh@163.com
 */
public class SignleFragment extends SupportFragment {

    public static SignleFragment newInstance() {

        Bundle args = new Bundle();

        SignleFragment fragment = new SignleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_signle, null);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });
        rootView.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(SignleFragment.newInstance(), true);
            }
        });
        rootView.findViewById(R.id.setResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "A new title");
                setResult(555, bundle);
            }
        });
        rootView.findViewById(R.id.startForResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startForResult(SignleFragment.newInstance(), 666);
            }
        });
        rootView.findViewById(R.id.startWithPop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWithPop(SignleFragment.newInstance());
            }
        });
        return attachSwipeBack(rootView);
    }

    @Override
    public void onResult(int requestCode, int resultCode, Bundle data) {
        super.onResult(requestCode, resultCode, data);
        if (requestCode == 666 && resultCode == 555) {
            Toast.makeText(getContext(), "on result", Toast.LENGTH_SHORT).show();
            toolbar.setTitle(data.getString("title"));
        }
    }
}
