package me.liam.support;

import android.view.animation.Animation;

public interface ISupportAnimation {

    public void onTargetFragmentCreateAnimations(int transit, boolean enter, int nextAnim);

}
