package me.liam.support;

import android.os.Bundle;

public abstract class ExtraTransaction {

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim);

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim,int popEnterAnim,int popExitAnim);

    public abstract ExtraTransaction setResult(int resultCode, Bundle data);

    public abstract ExtraTransaction displayEnterAnim();

    public abstract ExtraTransaction setTag();

    public abstract ExtraTransaction addBackStack();

    public abstract ExtraTransaction show();

    public abstract ExtraTransaction hide();

    public abstract ExtraTransaction runOnExcuteEnd();

    public abstract void start();

    public abstract void startWithPop();

    public abstract void startWithPopTo();

    public abstract void startForResult();

    public abstract void pop();

    public abstract void popChild();

    public abstract void popTo();

    public abstract void popToChild();

    public abstract void loadRootFragment();

    public abstract void remove();
}
