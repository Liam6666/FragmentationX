package me.liam.support;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import me.liam.anim.FragmentAnimation;
import me.liam.anim.NoneAnim;
import me.liam.fragmentation.R;
import me.liam.helper.FragmentUtils;
import me.liam.swipeback.SwipeBackLayout;

public class SupportFragment extends Fragment implements ISupportFragment {

    private FragmentAnimation fragmentAnimation;

    SupportFragmentVisible supportFragmentVisible = new SupportFragmentVisible();

    Handler handler;

    private SupportFragmentCallBack callBack;

    private SwipeBackLayout swipeBackLayout;

    private int defaultBackgroundColor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        supportFragmentVisible.onAttach(this);
        FragmentAnimation animation = onCreateCustomerAnimation();
        if (animation != null){
            setFragmentAnimation(animation);
        }
        defaultBackgroundColor = ((SupportActivity)getActivity()).getDefaultBackground();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (swipeBackLayout != null){
            View rootView = swipeBackLayout.getChildAt(0);
            if (rootView != null){
                rootView.setBackgroundColor(defaultBackgroundColor);
            }
        }else if (getView() != null){
            getView().setBackgroundColor(defaultBackgroundColor);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        supportFragmentVisible.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            getView().setClickable(true);
        }
        if (isSavedInstance()){
            resumeAnim();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getArguments().putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE,false);
        if (!getArguments().getBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST)
                && !isHidden()){
            onLazyInit(null);
        }
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = null;
        switch (transit){
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                if (enter){
                    ((SupportActivity)getActivity()).fragmentClickable = false;
                    if (!getArguments().getBoolean(SupportTransaction.FRAGMENTATION_PLAY_ENTER_ANIM)){
                        animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_empty);
                    }else {
                        final SupportFragment beforeOne = FragmentUtils.getBeforeOne(getFragmentManager(),this);
                        animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getEnterAnimId());
                        if (beforeOne != null){
                            if (getArguments().getBoolean(SupportTransaction.FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM)){
                                getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        beforeOne.onSupportPause();
                                    }
                                },animation.getDuration());
                            }else {
                                beforeOne.onCreatePopAnimations(false);
                            }
                        }
                    }
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((SupportActivity)getActivity()).fragmentClickable = true;
                            if (callBack != null){
                                callBack.onEnterAnimEnd();
                            }
                            onEnterAnimEnd();
                        }
                    },animation.getDuration());
                }
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                if (!enter){
                    SupportFragment show = FragmentUtils.getLastActiveFragment(getFragmentManager());
                    if (show != null){
                        if (getArguments().getBoolean(SupportTransaction.FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM)){
                            show.onSupportResume();
                        }else {
                            show.onCreatePopAnimations(true);
                        }
                    }
                    animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getExitAnimId());
                }
                break;
        }
        return animation;
    }

    public void onCreatePopAnimations(boolean popEnter){
        Animation animation;
        if (popEnter){
            animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getPopEnterAnimId());
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSupportResume();
                }
            },animation.getDuration());
        }else {
            animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getPopExitAnimId());
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSupportPause();
                }
            },animation.getDuration());
        }
        if (getView() != null){
            getView().startAnimation(animation);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        supportFragmentVisible.onSaveInstanceState(outState);
        getArguments().putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE,true);
        getArguments().putBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST,false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!getArguments().getBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST)
                && !hidden){
            onLazyInit(null);
        }
    }

    @Override
    public void onDestroyView() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .onResult(this);
        super.onDestroyView();
    }

    public boolean isSavedInstance(){
        return getArguments().getBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE,false);
    }

    private void resumeAnim(){
        fragmentAnimation = new FragmentAnimation(
                getArguments().getInt(SupportTransaction.FRAGMENTATION_ENTER_ANIM_ID,R.anim.anim_empty),
                getArguments().getInt(SupportTransaction.FRAGMENTATION_EXIT_ANIM_ID,R.anim.anim_empty),
                getArguments().getInt(SupportTransaction.FRAGMENTATION_POP_ENTER_ANIM_ID,R.anim.anim_empty),
                getArguments().getInt(SupportTransaction.FRAGMENTATION_POP_EXIT_ANIM_ID,R.anim.anim_empty));
    }

    FragmentAnimation getFragmentAnimation() {
        return fragmentAnimation;
    }

    void setFragmentAnimation(FragmentAnimation animation) {
        if (animation == null){
            this.fragmentAnimation = new NoneAnim();
        }else {
            this.fragmentAnimation = animation;
        }
        getArguments().putInt(SupportTransaction.FRAGMENTATION_ENTER_ANIM_ID,fragmentAnimation.getEnterAnimId());
        getArguments().putInt(SupportTransaction.FRAGMENTATION_EXIT_ANIM_ID,fragmentAnimation.getExitAnimId());
        getArguments().putInt(SupportTransaction.FRAGMENTATION_POP_ENTER_ANIM_ID,fragmentAnimation.getPopEnterAnimId());
        getArguments().putInt(SupportTransaction.FRAGMENTATION_POP_EXIT_ANIM_ID,fragmentAnimation.getPopExitAnimId());
    }

    int getContainerId(){
        return getArguments().getInt(SupportTransaction.FRAGMENTATION_CONTAINER_ID,0);
    }

    /**
     * 在栈内查找Fragment对象，如果一个栈内存在多个同一个class的Fragment对象，则返回结果可能不准确
     * @param cls
     * @param <T>
     * @return SupportFragment
     */
    @Override
    public <T extends SupportFragment> T findFragmentByClass(Class cls) {
        return FragmentUtils.findFragmentByClass(getFragmentManager(),cls);
    }

    /**
     * 在栈内查找Child Fragment对象，如果一个栈内存在多个同一个class的Fragment对象，则返回结果可能不准确
     * @param cls
     * @param <T>
     * @return SupportFragment
     */
    @Override
    public <T extends SupportFragment> T findChildFragmentByClass(Class cls) {
        return FragmentUtils.findFragmentByClass(getChildFragmentManager(),cls);
    }

    /**
     * Back事件分发，不建议重写
     * @return
     */
    @Override
    public boolean dispatcherOnBackPressed() {
        LinkedList<SupportFragment> backStackList = FragmentUtils.getBackStackFragments(getChildFragmentManager());
        if (backStackList.size() > 0){
            SupportFragment lastActive = backStackList.getLast();
            if (lastActive != null){
                return lastActive.dispatcherOnBackPressed();
            }
        }
        return onBackPressed();
    }

    /**
     * 重写此方法可自定义Back事件
     * @return true 消耗事件并不再向下传递
     */
    @Override
    public boolean onBackPressed() {
        if (isBackStack()){
            pop();
            return true;
        }
        return false;
    }

    /**
     * 当前Fragment是否被加入back键回退栈
     * @return
     */
    @Override
    public boolean isBackStack() {
        if (getArguments() == null) return false;
        return getArguments().getBoolean(SupportTransaction.FRAGMENTATION_BACK_STACK);
    }

    /**
     * 重写此方法可完成自定义Fragment动画
     * 重写返回null并不会将Fragment设置为空动画
     * @return FragmentAnimation
     */
    @Override
    public FragmentAnimation onCreateCustomerAnimation() {
        return null;
    }

    /**
     * 懒加载
     * 用于FragmentPager中时请使用最新的FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT方式创建适配器
     * @param savedInstanceState
     */
    @Override
    public void onLazyInit(Bundle savedInstanceState) {
        getArguments().putBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST,true);
    }

    /**
     * 入场动画结束时执行一次
     * 当Fragment从被回收的状态中恢复时并不会执行入场动画，所以也不会执行此方法
     */
    @Override
    public void onEnterAnimEnd() {

    }

    /**
     * 当Fragment变为不可见状态时（启动了一个新的Fragment被遮住时）执行一次
     * 执行hide操作时不会执行此方法
     */
    @Override
    public void onSupportPause() {

    }

    /**
     * 当Fragment从不可见状态中恢复时执行一次
     */
    @Override
    public void onSupportResume() {

    }

    /**
     * 当拖动返回时执行此方法，可重写
     * @param beforeOne
     * @param state
     * @param scrollPercent
     */
    @Override
    public void onSwipeDrag(SupportFragment beforeOne, int state, float scrollPercent) {
        if (beforeOne == null || beforeOne.getView() == null) return;
        float startX = -(beforeOne.getView().getWidth() * SwipeBackLayout.DEFAULT_SCROLL_THRESHOLD);
        beforeOne.getView().setX((1.0f - scrollPercent) * startX);
        if (state == SwipeBackLayout.STATE_IDLE && scrollPercent == 0){
            beforeOne.getView().setX(0);
            ((SupportActivity)getActivity()).fragmentSwipeDrag = false;
        }
    }

    /**
     * 接受返回数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onResult(int requestCode, int resultCode, Bundle data) {

    }

    /**
     * 接受全局消息
     * @param code
     * @param data
     */
    @Override
    public void onPostedData(int code, Bundle data) {

    }

    /**
     * 设置返回数据
     * @param resultCode
     * @param data
     */
    @Override
    public void setResult(int resultCode, Bundle data) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .setResult(this,resultCode,data);
    }

    /**
     * 自定义事件
     * @return
     */
    @Override
    public ExtraTransaction getExtraTransaction() {
        return new ExtraTransaction.ExtraTransactionImpl(this,getHandler());
    }

    /**
     * 创建一个根部Fragment，装载位置是Child Fragment Manager,相当于加载一个根部的子集Fragment
     * @param containerId 容器
     * @param to 要加载的对象
     * @param anim 绑定动画
     * @param playEnterAnim 是否展示入场动画
     * @param addToBackStack 是否加入back stack
     */
    @Override
    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .loadRootFragment(getChildFragmentManager(),containerId,to,anim,playEnterAnim,addToBackStack);
    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment to) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .loadRootFragment(getChildFragmentManager(),containerId,to,((SupportActivity)getActivity()).getDefaultAnimation(),false,true);
    }

    /**
     * 同时加载多个子Fragment
     * @param containerId 容器
     * @param showPosition 显示第几个（从1开始）
     * @param fragments 要加载的Fragment
     */
    @Override
    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .loadMultipleRootFragments(getChildFragmentManager(),containerId,showPosition,fragments);
    }

    /**
     * 显示一个Fragment，并隐藏当前Child Fragment Manager 栈内的其它Fragment
     * @param show
     */
    @Override
    public void showHideAllFragment(SupportFragment show) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .showHideAllFragment(getChildFragmentManager(),show);
    }

    /**
     * 启动一个新的Fragment
     * @param to
     */
    @Override
    public void start(SupportFragment to) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .start(this,to,true);
    }

    /**
     * 启动一个新的Fragment
     * @param to
     * @param addToBackStack 是否加入Back stack
     */
    @Override
    public void start(SupportFragment to, boolean addToBackStack) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .start(this,to,addToBackStack);
    }

    /**
     * 启动一个新的Fragment，并接受返回数据
     * @param to
     * @param requestCode
     */
    @Override
    public void startForResult(SupportFragment to, int requestCode) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .startForResult(this,to,requestCode);
    }

    /**
     * 启动一个新的Fragment并关闭当前Fragment
     * @param to
     */
    @Override
    public void startWithPop(SupportFragment to) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .startWithPop(this,to);
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     * @param to
     * @param cls
     */
    @Override
    public void startWithPopTo(SupportFragment to, Class cls) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .startWithPopTo(this,to,cls,true);
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     * @param to
     * @param cls
     * @param includeTarget 是否包含指定对象
     */
    @Override
    public void startWithPopTo(SupportFragment to, Class cls, boolean includeTarget) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .startWithPopTo(this,to,cls,includeTarget);
    }

    /**
     * 弹出栈内最后一个Fragment
     */
    @Override
    public void pop() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .pop(getFragmentManager());
    }

    /**
     * 弹出栈内的Fragment到指定位置
     * @param cls
     */
    @Override
    public void popTo(Class cls) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getFragmentManager(),cls,true);
    }

    /**
     * 弹出栈内的Fragment到指定位置
     * @param cls
     * @param includeTarget 是否包含指定对象
     */
    @Override
    public void popTo(Class cls, boolean includeTarget) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getFragmentManager(),cls,includeTarget);
    }

    /**
     * 弹出Child Fragment Manager内的最后一个Fragment
     */
    @Override
    public void popChild() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .pop(getChildFragmentManager());
    }

    /**
     * 弹出Child Fragment Manager的Fragment到指定位置
     * @param cls
     */
    @Override
    public void popChildTo(Class cls) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getChildFragmentManager(),cls,true);
    }

    /**
     * 弹出Child Fragment Manager的Fragment到指定位置
     * @param cls
     * @param includeTarget 是否包含指定对象
     */
    @Override
    public void popChildTo(Class cls, boolean includeTarget) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getChildFragmentManager(),cls,includeTarget);
    }

    /**
     * 弹出Child Fragment Manager所有的Fragment
     */
    @Override
    public void popAllChild() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popAll(getChildFragmentManager());
    }

    Handler getHandler() {
        if (handler == null){
            handler = new Handler(Looper.myLooper());
        }
        return handler;
    }

    void setCallBack(SupportFragmentCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 关联SwipeBackLayout
     * @param v
     * @return
     */
    public SwipeBackLayout attachSwipeBack(View v){
        final SupportFragment current = this;
        swipeBackLayout = new SwipeBackLayout(getContext());
        swipeBackLayout.setContentView(v);
        swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListenerEx() {

            SupportFragment before = null;

            @Override
            public void onContentViewSwipedBack() {
                if (before != null){
                    onSwipeDrag(before,SwipeBackLayout.STATE_IDLE,0);
                }
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((SupportActivity)getActivity())
                                .getSupportTransaction()
                                .remove(current,false);
                    }
                });
                ((SupportActivity)getActivity()).fragmentSwipeDrag = false;
            }

            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                if (state == SwipeBackLayout.STATE_IDLE && scrollPercent == 0){
                    //Swipe back layout is Closed
                    if (before != null){
                        before.onSupportPause();
                    }
                }
                if (before != null){
                    onSwipeDrag(before,state,scrollPercent);
                }
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                before = FragmentUtils.getBeforeOne(FragmentUtils.getActiveList(getFragmentManager()),current);
                if (before != null){
                    before.onSupportResume();
                }
                ((SupportActivity)getActivity()).fragmentSwipeDrag = true;
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
        return swipeBackLayout;
    }
}
