package me.liam.fragmentation.demo.wechat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.liam.fragmentation.demo.R;
import me.liam.fragmentation.support.SupportFragment;

/**
 * Created by Augustine on 2019/12/31.
 * <p>
 * email:nice_ohoh@163.com
 */
public class ContactsChildAFragment extends SupportFragment {

    public static ContactsChildAFragment newInstance() {

        Bundle args = new Bundle();

        ContactsChildAFragment fragment = new ContactsChildAFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), R.layout.fragment_wechat_contacts_a, null);
        return rootView;
    }


}
