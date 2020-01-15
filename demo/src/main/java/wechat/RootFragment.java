package wechat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

/**
 * Created by Augustine on 2019/12/31.
 * <p>
 * email:nice_ohoh@163.com
 */
public class RootFragment extends SupportFragment {

    public static RootFragment newInstance() {

        Bundle args = new Bundle();

        RootFragment fragment = new RootFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    private BottomNavigationBar bottom_bar;

    private HomePageFragment homePageFragment;
    private ContactsFragment contactsFragment;
    private MineFragment mineFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_weichat_root,null);
        bottom_bar = rootView.findViewById(R.id.bottom_bar);
        BottomBarItem item = new BottomBarItem(R.mipmap.home, R.string.title1);
        BottomBarItem item2 = new BottomBarItem(R.mipmap.plane, R.string.title2);
        BottomBarItem item3 = new BottomBarItem(R.mipmap.user, R.string.title3);
        bottom_bar.addTab(item);
        bottom_bar.addTab(item2);
        bottom_bar.addTab(item3);
        bottom_bar.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                if (position == 0){
                    showHideAllFragment(homePageFragment);
                }
                if (position == 1){
                    showHideAllFragment(contactsFragment);
                }
                if (position == 2){
                    showHideAllFragment(mineFragment);
                }
            }
        });
        if (findChildFragmentByClass(HomePageFragment.class) == null){
            homePageFragment = HomePageFragment.newInstance();
            contactsFragment = ContactsFragment.newInstance();
            mineFragment = MineFragment.newInstance();
            loadMultipleRootFragments(R.id.container,1,homePageFragment,contactsFragment,mineFragment);
        }else {
            homePageFragment = findChildFragmentByClass(HomePageFragment.class);
            contactsFragment = findChildFragmentByClass(ContactsFragment.class);
            mineFragment = findChildFragmentByClass(MineFragment.class);
        }
        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
//        if (!homePageFragment.isVisible()){
//            showHideAllFragment(homePageFragment);
//            return true;
//        }else {
//            getActivity().finish();
//            return true;
//        }
    }
}
