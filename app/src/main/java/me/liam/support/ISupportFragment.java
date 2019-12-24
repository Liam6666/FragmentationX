package me.liam.support;

import android.view.animation.Animation;

import me.liam.anim.FragmentAnimation;

public interface ISupportFragment {

    public boolean dispatcherOnBackPressed();

    public boolean onBackPressed();

    public boolean onBackPressedChild();

    public FragmentAnimation onCreateCustomerAnimation();

    public void onEnterAnimEnd();

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void startWithPop(SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

    public void popChild();

    public void popChildTo(Class cls);

    public void popChildTo(Class cls, boolean includeTarget);
}
