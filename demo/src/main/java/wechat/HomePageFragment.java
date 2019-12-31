package wechat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

/**
 * Created by Augustine on 2019/12/31.
 * <p>
 * email:nice_ohoh@163.com
 */
public class HomePageFragment extends SupportFragment {

    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_wechat_home,null);
        return rootView;
    }
}
