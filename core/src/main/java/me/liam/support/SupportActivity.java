package me.liam.support;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.LinkedList;
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

    boolean fragmentClickable = true;

    boolean fragmentSwipeDrag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportTransaction = new SupportTransaction(this);
    }

    @Override
    public void onBackPressed() {
        if (fragmentSwipeDrag) return;
        if (FragmentUtils.getActiveList(getSupportFragmentManager()).size() > 1){
            SupportFragment activeFragment = FragmentUtils.getLastActiveFragment(getSupportFragmentManager());
            if (activeFragment == null) {
                ActivityCompat.finishAfterTransition(this);
                return;
            }
            if (activeFragment.dispatcherOnBackPressed()){
                return;
            }
            pop();
        }else {
            ActivityCompat.finishAfterTransition(this);
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!fragmentClickable) return true;
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取默认的Fragment背景颜色
     * @return
     */
    @Override
    public int getDefaultBackground() {
        return Color.WHITE;
    }

    /**
     * 设置全局默认动画
     * @param animation
     */
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

    /**
     * 在栈内查找Fragment对象，如果一个栈内存在多个同一个class的Fragment对象，则返回结果可能不准确
     * @param cls
     * @param <T>
     * @return
     */
    @Override
    public <T extends SupportFragment> T findFragmentByClass(Class cls) {
        return FragmentUtils.findFragmentByClass(getSupportFragmentManager(),cls);
    }

    /**
     * 发送一条消息到当前所有的Fragment
     * @param code
     * @param data
     */
    @Override
    public void postDataToFragments(int code, Bundle data) {
        List<SupportFragment> fragmentList = new ArrayList<>();
        FragmentUtils.getAllFragments(fragmentList,getSupportFragmentManager());
        for (SupportFragment f : fragmentList){
            f.onPostedData(code,data);
        }
    }

    /**
     * 加载第一个Fragment
     * @param containerId
     * @param to
     * @param anim
     * @param playEnterAnim
     * @param addToBackStack
     */
    @Override
    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim,boolean addToBackStack) {
        supportTransaction.loadRootFragment(getSupportFragmentManager(),containerId,to,anim,playEnterAnim,addToBackStack);
    }

    /**
     * 加载第一个Fragment
     * @param containerId
     * @param to
     */
    @Override
    public void loadRootFragment(int containerId, SupportFragment to) {
        supportTransaction.loadRootFragment(getSupportFragmentManager(),containerId,to,getDefaultAnimation(),false,true);
    }

    /**
     * 加载多个Fragment
     * @param containerId
     * @param showPosition
     * @param fragments
     */
    @Override
    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments) {
        supportTransaction.loadMultipleRootFragments(getSupportFragmentManager(),containerId,showPosition,fragments);
    }

    /**
     * 显示一个Fragment，并隐藏当前Child Fragment Manager 栈内的其它Fragment
     * @param show
     */
    @Override
    public void showHideAllFragment(SupportFragment show) {
        supportTransaction.showHideAllFragment(getSupportFragmentManager(), show);
    }

    /**
     * 启动一个新的Fragment，栈内必须有至少1个Fragment才可以使用此方法
     * @param to
     */
    @Override
    public void start(SupportFragment to) {
        supportTransaction.start(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,true);
    }

    /**
     * 启动一个新的Fragment，栈内必须有至少1个Fragment才可以使用此方法
     * @param to
     * @param addToBackStack
     */
    @Override
    public void start(SupportFragment to, boolean addToBackStack) {
        supportTransaction.start(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,addToBackStack);
    }

    /**
     * 启动一个新的Fragment，栈内必须有至少1个Fragment才可以使用此方法
     * @param from
     * @param to
     */
    @Override
    public void start(SupportFragment from, SupportFragment to) {
        supportTransaction.start(from,to,true);
    }

    /**
     * 启动一个新的Fragment，栈内必须有至少1个Fragment才可以使用此方法
     * @param from
     * @param to
     * @param addToBackStack
     */
    @Override
    public void start(SupportFragment from, SupportFragment to, boolean addToBackStack) {
        supportTransaction.start(from,to,addToBackStack);
    }

    /**
     * 启动一个新的Fragment并关闭当前Fragment
     * @param to
     */
    @Override
    public void startWithPop(SupportFragment to) {
        supportTransaction.startWithPop(FragmentUtils.getLastFragment(getSupportFragmentManager()), to);
    }

    /**
     * 启动一个新的Fragment并关闭当前Fragment
     * @param from
     * @param to
     */
    @Override
    public void startWithPop(SupportFragment from, SupportFragment to) {
        supportTransaction.startWithPop(from, to);
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     * @param to
     * @param cls
     */
    @Override
    public void startWithPopTo(SupportFragment to, Class cls) {
        supportTransaction.startWithPopTo(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,cls,true);
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     * @param to
     * @param cls
     * @param includeTarget
     */
    @Override
    public void startWithPopTo(SupportFragment to, Class cls, boolean includeTarget) {
        supportTransaction.startWithPopTo(FragmentUtils.getLastFragment(getSupportFragmentManager()),to,cls,includeTarget);
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     * @param from
     * @param to
     * @param cls
     */
    @Override
    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls) {
        supportTransaction.startWithPopTo(from,to,cls,true);
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     * @param from
     * @param to
     * @param cls
     * @param includeTarget
     */
    @Override
    public void startWithPopTo(SupportFragment from, SupportFragment to, Class cls, boolean includeTarget) {
        supportTransaction.startWithPopTo(from,to,cls,includeTarget);
    }

    /**
     * 弹出栈内最后一个Fragment
     */
    @Override
    public void pop() {
        supportTransaction.pop(getSupportFragmentManager());
    }

    /**
     * 弹出栈内的Fragment到指定位置
     * @param cls
     */
    @Override
    public void popTo(Class cls) {
        supportTransaction.popTo(getSupportFragmentManager(),cls,true);
    }

    /**
     * 弹出栈内的Fragment到指定位置
     * @param cls
     * @param includeTarget
     */
    @Override
    public void popTo(Class cls, boolean includeTarget) {
        supportTransaction.popTo(getSupportFragmentManager(),cls,includeTarget);
    }

    SupportTransaction getSupportTransaction() {
        return supportTransaction;
    }
}
