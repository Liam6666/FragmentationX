package zhihu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.anim.ClassicVerticalAnim;
import me.liam.fragmentation.R;
import me.liam.fragmentation.SignleFragment;
import me.liam.support.SupportFragment;
import wechat.SettingsFragment;

/**
 * Create on 2020/1/6.
 */
public class MineFragment extends SupportFragment {

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;
    private LinearLayout profile;
    private LinearLayout settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_zhihu_mine,null);
        profile = rootView.findViewById(R.id.profile);
        settings = rootView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getChildFragmentManager().getFragments().size() > 10){
                    popAllChild();
                }else {
                    loadRootFragment(R.id.mine_childContainer,SettingsFragment.newInstance(),new ClassicVerticalAnim(),true,true);
                }
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(SignleFragment.newInstance());
            }
        });
       return rootView;
    }
}
