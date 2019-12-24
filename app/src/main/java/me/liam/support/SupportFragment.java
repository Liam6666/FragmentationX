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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import me.liam.anim.FragmentAnimation;
import me.liam.anim.NoneAnim;
import me.liam.fragmentation.R;
import me.liam.helper.FragmentUtils;

public class SupportFragment extends Fragment implements ISupportFragment {

    private FragmentAnimation fragmentAnimation;

    SupportFragmentVisible supportFragmentVisible = new SupportFragmentVisible();

    Handler handler;

    private SupportFragmentCallBack callBack;

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
        }else {

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
        Animation animation = null;
        if (popEnter){
            animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getPopEnterAnimId());
        }else {
            animation = AnimationUtils.loadAnimation(getContext(),fragmentAnimation.getPopExitAnimId());
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
            SupportFragment lastActive = FragmentUtils.getLastActiveFragment(getChildFragmentManager());
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
    public boolean onBackPressedChild() {
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
    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .loadRootFragment(getChildFragmentManager(),containerId,to,anim,playEnterAnim);
    }

    @Override
    public void loadRootFragment(int containerId, SupportFragment to) {
        ((SupportActivity)getActivity())
                .getSupportTransaction()
                .loadRootFragment(getChildFragmentManager(),containerId,to,null,false);
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
                .start(this,to);
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


    public Handler getHandler() {
        if (handler == null){
            handler = new Handler(Looper.myLooper());
        }
        return handler;
    }

    void setCallBack(SupportFragmentCallBack callBack) {
        this.callBack = callBack;
    }

}
