package me.liam.fragmentation.demo.wechat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.demo.R;
import me.liam.fragmentation.support.SupportFragment;

/**
 * Created by Augustine on 2019/12/31.
 * <p>
 * email:nice_ohoh@163.com
 */
public class ContactsFragment extends SupportFragment {

    public static ContactsFragment newInstance() {

        Bundle args = new Bundle();

        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;
    private TabLayout tabLayout;

    private ContactsChildAFragment contactsChildAFragment;
    private ContactsChildBFragment contactsChildBFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_wechat_contacts, null);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("菜单A"));
        tabLayout.addTab(tabLayout.newTab().setText("菜单B"));
        if (findChildFragment(ContactsChildAFragment.class) == null) {
            contactsChildAFragment = ContactsChildAFragment.newInstance();
            contactsChildBFragment = ContactsChildBFragment.newInstance();
            loadMultipleRootFragments(R.id.container, 1, contactsChildAFragment, contactsChildBFragment);
        } else {
            contactsChildAFragment = findChildFragment(ContactsChildAFragment.class);
            contactsChildBFragment = findChildFragment(ContactsChildBFragment.class);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showHideAllFragment(contactsChildAFragment);
                } else {
                    showHideAllFragment(contactsChildBFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }

    @Override
    public void onLazyInit(Bundle savedInstanceState) {
        super.onLazyInit(savedInstanceState);
        Log.e("ContactsFragment", "onLazyInit");
    }
}
