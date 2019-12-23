package me.liam.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.core.view.ViewCompat;
import me.liam.fragmentation.R;
import me.liam.support.SupportFragment;

public class FragmentAnimation {

    Animation enterAnim;
    Animation exitAnim;
    Animation popEnterAnim;
    Animation popExitAnim;

    int enterAnimId;
    int exitAnimId;
    int popEnterAnimId;
    int popExitAnimId;

    SupportFragment fragment;

    public FragmentAnimation(int enterAnimId, int exitAnimId) {
        this(enterAnimId,exitAnimId,R.anim.anim_empty,R.anim.anim_empty);
    }

    public FragmentAnimation(int enterAnimId, int exitAnimId, int popEnterAnimId, int popExitAnimId) {
        this.enterAnimId = enterAnimId;
        this.exitAnimId = exitAnimId;
        this.popEnterAnimId = popEnterAnimId;
        this.popExitAnimId = popExitAnimId;
    }

    public void loadAnim(SupportFragment fragment){
        this.fragment = fragment;
        this.enterAnim = AnimationUtils.loadAnimation(fragment.getContext(),enterAnimId);
        this.exitAnim = AnimationUtils.loadAnimation(fragment.getContext(),exitAnimId);
        this.popEnterAnim = AnimationUtils.loadAnimation(fragment.getContext(),popEnterAnimId);
        this.popExitAnim = AnimationUtils.loadAnimation(fragment.getContext(),popExitAnimId);
    }


    private long playAnim(Animation a){
        if (fragment == null || fragment.getView() == null) return 0;
        fragment.getView().startAnimation(a);
        return a.getDuration();
    }

    public Animation getEnterAnim() {
        return enterAnim;
    }

    public Animation getExitAnim() {
        return exitAnim;
    }

    public Animation getPopEnterAnim() {
        return popEnterAnim;
    }

    public Animation getPopExitAnim() {
        return popExitAnim;
    }

    public int getEnterAnimId() {
        return enterAnimId;
    }

    public int getExitAnimId() {
        return exitAnimId;
    }

    public int getPopEnterAnimId() {
        return popEnterAnimId;
    }

    public int getPopExitAnimId() {
        return popExitAnimId;
    }

    public long playEnterAnim(){
        return playAnim(enterAnim);
    }

    public long playExitAnim(){
        return playAnim(exitAnim);
    }

    public long playPopEnterAnim(){
        return playAnim(popEnterAnim);
    }

    public long playPopExitAnim(){
        return playAnim(popExitAnim);
    }
}
