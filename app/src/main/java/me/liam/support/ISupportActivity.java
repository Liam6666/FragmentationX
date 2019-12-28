package me.liam.support;

import android.os.Bundle;

import me.liam.anim.FragmentAnimation;

public interface ISupportActivity {

    public void setDefaultAnimation(FragmentAnimation animation);

    public FragmentAnimation getDefaultAnimation();

    public <T extends SupportFragment> T findFragmentByClass(Class cls);

    public void postDataToFragments(int code, Bundle data);

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void start(SupportFragment to, boolean addToBackStack);

    public void start(SupportFragment from, SupportFragment to);

    public void start(SupportFragment from, SupportFragment to,boolean addToBackStack);

    public void startWithPop(SupportFragment from, SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

}
