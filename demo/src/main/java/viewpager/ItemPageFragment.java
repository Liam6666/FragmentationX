package viewpager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

/**
 * Create on 2020/1/8.
 */
public class ItemPageFragment extends SupportFragment {

    public static ItemPageFragment newInstance(int count) {

        Bundle args = new Bundle();
        args.putInt("count",count);
        ItemPageFragment fragment = new ItemPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_item_page,null);
        textView = rootView.findViewById(R.id.tv);
        textView.setText(""+getArguments().getInt("count"));
        Log.e("ItemPageFragment",getArguments().getInt("count") + "onCreateView");
        return rootView;
    }

    @Override
    public void onLazyInit(Bundle savedInstanceState) {
        super.onLazyInit(savedInstanceState);
        Log.e("ItemPageFragment",getArguments().getInt("count") + "onLazyInit");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("ItemPageFragment",getArguments().getInt("count") + "setUserVisibleHint" + isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ItemPageFragment",getArguments().getInt("count") + "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ItemPageFragment",getArguments().getInt("count") + "onResume");
    }
}
