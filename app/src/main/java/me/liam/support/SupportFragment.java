package me.liam.support;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.ToastUtils;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        supportFragmentVisible.onAttach(this);
        FragmentAnimation animation = onCreateCustomerAnimation();
        if (animation != null){
            setFragmentAnimation(animation);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = null;
        switch (transit){
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                if (enter){
                    if (!getArguments().getBoolean(SupportTransaction.FRAGMENTATION_PLAY_ENTER_ANIM)){
                        animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_empty);
                    }else {
                        SupportFragment beforeOne = FragmentUtils.getBeforeOne(getFragmentManager(),this);
                        animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getEnterAnimId());
                        if (beforeOne != null){
                            beforeOne.onCreatePopAnimations(false);
                        }
                    }
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
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
                        show.onCreatePopAnimations(true);
                    }
                    animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getExitAnimId());
                }
                break;
        }
        return animation;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        supportFragmentVisible.onSaveInstanceState(outState);
        getArguments().putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE,true);
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

    @Override
    public boolean dispatcherOnBackPressed() {
        if (getChildFragmentManager().getFragments().size() > 0){
            SupportFragment lastActive = FragmentUtils.getLastAddBackStackFragment(getChildFragmentManager());
            if (lastActive != null){
                return lastActive.dispatcherOnBackPressed();
            }
        }
        return onBackPressed();
    }

    @Override
    public boolean onBackPressed() {
        pop();
        return true;
    }

    @Override
    public FragmentAnimation onCreateCustomerAnimation() {
        return null;
    }

    @Override
    public void onEnterAnimEnd() {

    }

    @Override
    public void onSupportPause() {

    }

    @Override
    public void onSupportResume() {

    }

    @Override
    public void onSwipeDrag(SupportFragment beforeOne, int state, float scrollPercent) {
        if (beforeOne == null || beforeOne.getView() == null) return;
        float startX = -(beforeOne.getView().getWidth() * SwipeBackLayout.DEFAULT_SCROLL_THRESHOLD);
        beforeOne.getView().setX((1.0f - scrollPercent) * startX);
        if (state == SwipeBackLayout.STATE_IDLE && scrollPercent == 0){
            beforeOne.getView().setX(0);
        }
    }

    @Override
    public void onResult(int requestCode, int resultCode, Bundle data) {

    }

    @Override
    public void onNotification(int code, Bundle data) {

    }

    @Override
    public void setResult(int resultCode, Bundle data) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .setResult(this,resultCode,data);
    }

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
                .loadRootFragment(getChildFragmentManager(),containerId,to,null,false,true);
    }

    @Override
    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .loadMultipleRootFragments(getChildFragmentManager(),containerId,showPosition,fragments);
    }

    @Override
    public void showHideAllFragment(SupportFragment show) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .showHideAllFragment(getChildFragmentManager(),show);
    }

    @Override
    public void start(SupportFragment to) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .start(this,to,true);
    }

    @Override
    public void start(SupportFragment to, boolean addToBackStack) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .start(this,to,addToBackStack);
    }

    @Override
    public void startForResult(SupportFragment to, int requestCode) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .startForResult(this,to,requestCode);
    }


    @Override
    public void startWithPop(SupportFragment to) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .startWithPop(this,to);
    }

    @Override
    public void pop() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .pop(getFragmentManager());
    }

    @Override
    public void popTo(Class cls) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getFragmentManager(),cls,true);
    }

    @Override
    public void popTo(Class cls, boolean includeTarget) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getFragmentManager(),cls,includeTarget);
    }

    @Override
    public void popChild() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .pop(getChildFragmentManager());
    }

    @Override
    public void popChildTo(Class cls) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getChildFragmentManager(),cls,true);
    }

    @Override
    public void popChildTo(Class cls, boolean includeTarget) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popTo(getChildFragmentManager(),cls,includeTarget);
    }

    @Override
    public void popAllChild() {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .popAll(getChildFragmentManager());
    }


    public Handler getHandler() {
        if (handler == null){
            handler = new Handler(Looper.myLooper());
        }
        return handler;
    }

    void setCallBack(SupportFragmentCallBack callBack) {
        this.callBack = callBack;
    }

    public SwipeBackLayout attachSwipeBack(View v){
        final SupportFragment current = this;
        swipeBackLayout = new SwipeBackLayout(getContext());
        swipeBackLayout.setContentView(v);
        swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListenerEx() {

            SupportFragment before = null;

            @Override
            public void onContentViewSwipedBack() {
                ((SupportActivity)getActivity())
                        .getSupportTransaction()
                        .remove(current,false);
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
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
        return swipeBackLayout;
    }
}
