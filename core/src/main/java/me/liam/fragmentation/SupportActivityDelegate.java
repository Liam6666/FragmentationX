package me.liam.fragmentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class SupportActivityDelegate {

    private ISupportActivity support;

    private FragmentActivity activity;

    private SupportTransaction transaction;

    SupportActivityDelegate(ISupportActivity support) {
        this.support = support;
        activity = (FragmentActivity) support;
        transaction = new SupportTransaction(activity);
    }

    void onCreate(@Nullable Bundle savedInstanceState) {

    }
}
