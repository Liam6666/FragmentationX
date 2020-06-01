package me.liam.fragmentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.liam.anim.FragmentAnimation;
import me.liam.support.SupportFragment;

public class SupportActivity extends AppCompatActivity implements ISupportActivity{

    private SupportActivityDelegate activityDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDelegate = new SupportActivityDelegate(this);
        activityDelegate.onCreate(savedInstanceState);
    }

    @Override
    public int getDefaultBackground() {
        return 0;
    }

    @Override
    public void setDefaultAnimation(FragmentAnimation animation) {

    }

    @Override
    public FragmentAnimation getDefaultAnimation() {
        return null;
    }

    @Override
    public <T extends SupportFragment> T findFragmentByClass(Class cls) {
        return null;
    }

    @Override
    public void postDataToFragments(int code, Bundle data) {

    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack) {

    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment to) {

    }

    @Override
    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments) {

    }

    @Override
    public void showHideAllFragment(SupportFragment show) {

    }

    @Override
    public void start(SupportFragment to) {

    }

    @Override
    public void start(SupportFragment to, boolean addToBackStack) {

    }

    @Override
    public void start(SupportFragment from, SupportFragment to) {

    }

    @Override
    public void start(SupportFragment from, SupportFragment to, boolean addToBackStack) {

    }

    @Override
    public void startWithPop(SupportFragment to) {

    }

    @Override
    public void startWithPop(SupportFragment from, SupportFragment to) {

    }

    @Override
    public void startWithPopTo(SupportFragment to, Class cls) {

    }

    @Override
    public void startWithPopTo(SupportFragment to, Class cls, boolean includeTarget) {

    }

    @Override
    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls) {

    }

    @Override
    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls, boolean includeTarget) {

    }

    @Override
    public void pop() {

    }

    @Override
    public void popTo(Class cls) {

    }

    @Override
    public void popTo(Class cls, boolean includeTarget) {

    }
}
