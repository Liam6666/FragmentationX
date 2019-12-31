package me.liam.support;

import android.os.Bundle;
import android.view.animation.Animation;

import me.liam.anim.FragmentAnimation;

public interface ISupportFragment {

    public <T extends SupportFragment> T findFragmentByClass(Class cls);

    public <T extends SupportFragment> T findChildFragmentByClass(Class cls);

    public boolean dispatcherOnBackPressed();

    public boolean onBackPressed();

    public FragmentAnimation onCreateCustomerAnimation();

    public void onEnterAnimEnd();

    public void onSupportPause();

    public void onSupportResume();

    public void onSwipeDrag(SupportFragment beforeOne, int state, float scrollPercent);

    public void onResult(int requestCode, int resultCode, Bundle data);

    public void onPostedData(int code, Bundle data);

    public void setResult(int resultCode, Bundle data);

    public ExtraTransaction getExtraTransaction();

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void start(SupportFragment to, boolean addToBackStack);

    public void startForResult(SupportFragment to, int requestCode);

    public void startWithPop(SupportFragment to);

    public void startWithPopTo(SupportFragment to, Class cls);

    public void startWithPopTo(SupportFragment to, Class cls, boolean includeTarget);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

    public void popChild();

    public void popChildTo(Class cls);

    public void popChildTo(Class cls, boolean includeTarget);

    public void popAllChild();
}
