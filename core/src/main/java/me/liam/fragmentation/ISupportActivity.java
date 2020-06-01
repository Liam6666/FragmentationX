package me.liam.fragmentation;

import android.os.Bundle;

import me.liam.anim.FragmentAnimation;
import me.liam.support.SupportFragment;

public interface ISupportActivity {

    public int getDefaultBackground();

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

    public void start(SupportFragment from, SupportFragment to, boolean addToBackStack);

    public void startWithPop(SupportFragment to);

    public void startWithPop(SupportFragment from, SupportFragment to);

    public void startWithPopTo(SupportFragment to, Class cls);

    public void startWithPopTo(SupportFragment to, Class cls, boolean includeTarget);

    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls);

    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls, boolean includeTarget);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

}
