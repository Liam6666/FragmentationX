package me.liam.support;

import android.os.Bundle;
import android.view.animation.Animation;

import me.liam.anim.FragmentAnimation;

public interface ISupportFragment {

    public boolean dispatcherOnBackPressed();

    public boolean onBackPressed();

    public FragmentAnimation onCreateCustomerAnimation();

    public void onEnterAnimEnd();

    public void onSupportPause();

    public void onSupportResume();

    public void onResult(int requestCode, int resultCode, Bundle data);

    public void setResult(int resultCode, Bundle data);

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void start(SupportFragment to, boolean addToBackStack);

    public void startForResult(SupportFragment to, int requestCode);

    public void startWithPop(SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

    public void popChild();

    public void popChildTo(Class cls);

    public void popChildTo(Class cls, boolean includeTarget);
}
