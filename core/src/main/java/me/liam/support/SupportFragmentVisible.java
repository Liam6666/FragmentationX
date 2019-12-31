package me.liam.support;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

class SupportFragmentVisible {

    final public static String FRAGMENTATION_IS_HIDDEN = "Fragmentation:IsHidden";

    private SupportFragment supportFragment;

    public void onAttach(SupportFragment supportFragment) {
        this.supportFragment = supportFragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean(FRAGMENTATION_IS_HIDDEN)){
                FragmentManager fm = supportFragment.getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(supportFragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FRAGMENTATION_IS_HIDDEN,supportFragment.isHidden());
    }
}
