package wechat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.anim.ClassicVerticalAnim;
import me.liam.fragmentation.R;
import me.liam.fragmentation.SignleFragment;
import me.liam.support.SupportFragment;

/**
 * Created by Augustine on 2019/12/31.
 * <p>
 * email:nice_ohoh@163.com
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
        rootView = View.inflate(getContext(), R.layout.fragment_wechat_mine,null);
        profile = rootView.findViewById(R.id.profile);
        settings = rootView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (getChildFragmentManager().getFragments().size() > 10){
//                    popAllChild();
//                }else {
//                    loadRootFragment(R.id.mine_childContainer,SettingsFragment.newInstance(),new ClassicVerticalAnim(),true,true);
//                }
                ((RootFragment)getParentFragment()).getExtraTransaction()
                        .dontDisplaySelfPopAnim(true)
                        .setCustomerAnimations(R.anim.classic_vertical_enter,R.anim.classic_vertical_exit)
                        .start(SettingsFragment.newInstance());

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SupportFragment)getParentFragment()).start(SettingsFragment.newInstance());
            }
        });
        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        if (getChildFragmentManager().getFragments().size() > 0){
            popChild();
            return true;
        }else {
            ((RootFragment)getParentFragment()).onBackPressed();
            return true;
        }
    }

    @Override
    public void onLazyInit(Bundle savedInstanceState) {
        super.onLazyInit(savedInstanceState);
        Log.e("MineFragment","onLazyInit");
    }
}
