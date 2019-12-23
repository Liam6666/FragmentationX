package me.liam.support;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

    private ISupportAnimation iSupportAnimation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        supportFragmentVisible.onAttach(this);
        if (fragmentAnimation != null){
            fragmentAnimation.loadAnim(this);
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
        if (isSavedInstance()){
            resumeAnim();
        }else {

//            if (getArguments().getBoolean(SupportTransaction.FRAGMENTATION_PLAY_ENTER_ANIM)){
//                getFragmentAnimation().playEnterAnim();
//            }
//            SupportFragment beforeOne = FragmentUtils.getBeforeOne(getFragmentManager(),SupportFragment.this);
//            if (beforeOne != null){
//                beforeOne.getFragmentAnimation().playPopExitAnim();
//            }
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
//        if (isSavedInstance()){
//            return AnimationUtils.loadAnimation(getContext(),R.anim.anim_empty);
//        }
        if (iSupportAnimation != null){
            iSupportAnimation.onTargetFragmentCreateAnimations(transit, enter, nextAnim);
        }
        switch (transit){
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                if (enter){
                    animation = getFragmentAnimation().getEnterAnim();
                }
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                if (!enter){
                    animation = getFragmentAnimation().getExitAnim();
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

    public boolean isSavedInstance(){
        return getArguments().getBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE,false);
    }

    private void resumeAnim(){
        if (fragmentAnimation != null) return;
        fragmentAnimation = new FragmentAnimation(
                getArguments().getInt(SupportTransaction.FRAGMENTATION_ENTER_ANIM_ID,R.anim.anim_empty),
                getArguments().getInt(SupportTransaction.FRAGMENTATION_EXIT_ANIM_ID,R.anim.anim_empty),
                getArguments().getInt(SupportTransaction.FRAGMENTATION_POP_ENTER_ANIM_ID,R.anim.anim_empty),
                getArguments().getInt(SupportTransaction.FRAGMENTATION_POP_EXIT_ANIM_ID,R.anim.anim_empty));
        fragmentAnimation.loadAnim(this);
    }

    FragmentAnimation getFragmentAnimation() {
        return fragmentAnimation;
    }

    void setFragmentAnimation(FragmentAnimation animation) {
        this.fragmentAnimation = animation;
        if (this.fragmentAnimation == null){
            this.fragmentAnimation = new NoneAnim();
        }
        getArguments().putInt(SupportTransaction.FRAGMENTATION_ENTER_ANIM_ID,animation.getEnterAnimId());
        getArguments().putInt(SupportTransaction.FRAGMENTATION_EXIT_ANIM_ID,animation.getExitAnimId());
        getArguments().putInt(SupportTransaction.FRAGMENTATION_POP_ENTER_ANIM_ID,animation.getPopEnterAnimId());
        getArguments().putInt(SupportTransaction.FRAGMENTATION_POP_EXIT_ANIM_ID,animation.getPopExitAnimId());
    }

    @Override
    public void start(SupportFragment to) {
        ((ISupportActivity)getActivity()).start(this,to);
    }

    public ISupportAnimation getiSupportAnimation() {
        return iSupportAnimation;
    }

    public void setiSupportAnimation(ISupportAnimation iSupportAnimation) {
        this.iSupportAnimation = iSupportAnimation;
    }
}
