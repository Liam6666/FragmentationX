package me.liam.support;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.liam.anim.ClassicHorizontalAnim;
import me.liam.anim.FragmentAnimation;
import me.liam.helper.FragmentUtils;

public class SupportActivity extends AppCompatActivity implements ISupportActivity {

    private SupportTransaction supportTransaction;

    private FragmentAnimation defaultAnimation = new ClassicHorizontalAnim();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportTransaction = new SupportTransaction(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().size() > 1){
            SupportFragment activeFragment = FragmentUtils.getLastAddBackStackFragment(getSupportFragmentManager());
            if (activeFragment != null){
                if (!activeFragment.dispatcherOnBackPressed()){
                    pop();
                }
            }
        }else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public void setDefaultAnimation(FragmentAnimation animation) {
        defaultAnimation = animation;
        List<SupportFragment> fragmentList = new ArrayList<>();
        FragmentUtils.getAllFragments(fragmentList,getSupportFragmentManager());
        for (SupportFragment f : fragmentList){
            f.setFragmentAnimation(defaultAnimation);
        }
    }

    @Override
    public FragmentAnimation getDefaultAnimation() {
        return defaultAnimation;
    }

    @Override
    public <T extends SupportFragment> T findFragmentByClass(Class cls) {
        return FragmentUtils.findFragmentByClass(getSupportFragmentManager(),cls);
    }

    @Override
    public void postDataToFragments(int code, Bundle data) {
        List<SupportFragment> fragmentList = new ArrayList<>();
        FragmentUtils.getAllFragments(fragmentList,getSupportFragmentManager());
        for (SupportFragment f : fragmentList){
            f.onPostedData(code,data);
        }
    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim,boolean addToBackStack) {
        supportTransaction.loadRootFragment(getSupportFragmentManager(),containerId,to,anim,playEnterAnim,addToBackStack);
    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment to) {
        supportTransaction.loadRootFragment(getSupportFragmentManager(),containerId,to,getDefaultAnimation(),false,true);
    }

    @Override
    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments) {
        supportTransaction.loadMultipleRootFragments(getSupportFragmentManager(),containerId,showPosition,fragments);
    }

    @Override
    public void showHideAllFragment(SupportFragment show) {
        supportTransaction.showHideAllFragment(getSupportFragmentManager(), show);
    }

    @Override
    public void start(SupportFragment to) {
        supportTransaction.start(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,true);
    }

    @Override
    public void start(SupportFragment to, boolean addToBackStack) {
        supportTransaction.start(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,addToBackStack);
    }

    @Override
    public void start(SupportFragment from, SupportFragment to) {
        supportTransaction.start(from,to,true);
    }

    @Override
    public void start(SupportFragment from, SupportFragment to, boolean addToBackStack) {
        supportTransaction.start(from,to,addToBackStack);
    }

    @Override
    public void startWithPop(SupportFragment to) {
        supportTransaction.startWithPop(FragmentUtils.getLastFragment(getSupportFragmentManager()), to);
    }

    @Override
    public void startWithPop(SupportFragment from, SupportFragment to) {
        supportTransaction.startWithPop(from, to);
    }

    @Override
    public void startWithPopTo(SupportFragment to, Class cls) {
        supportTransaction.startWithPopTo(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,cls,true);
    }

    @Override
    public void startWithPopTo(SupportFragment to, Class cls, boolean includeTarget) {
        supportTransaction.startWithPopTo(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,cls,includeTarget);
    }

    @Override
    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls) {
        supportTransaction.startWithPopTo(from,to,cls,true);
    }

    @Override
    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls, boolean includeTarget) {
        supportTransaction.startWithPopTo(from,to,cls,includeTarget);
    }

    @Override
    public void pop() {
        supportTransaction.pop(getSupportFragmentManager());
    }

    @Override
    public void popTo(Class cls) {
        supportTransaction.popTo(getSupportFragmentManager(),cls,true);
    }

    @Override
    public void popTo(Class cls, boolean includeTarget) {
        supportTransaction.popTo(getSupportFragmentManager(),cls,includeTarget);
    }

    public SupportTransaction getSupportTransaction() {
        return supportTransaction;
    }
}
