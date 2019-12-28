package me.liam.support;

import android.os.Bundle;

import me.liam.anim.FragmentAnimation;
import me.liam.fragmentation.R;

public abstract class ExtraTransaction {

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim);

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim,int popEnterAnim,int popExitAnim);

    public abstract ExtraTransaction setResult(int resultCode, Bundle data);

    public abstract ExtraTransaction displayEnterAnim(boolean displayEnterAnim);

    public abstract ExtraTransaction setTag(String tag);

    public abstract ExtraTransaction addBackStack(boolean addBackStack);

    public abstract ExtraTransaction show(SupportFragment... show);

    public abstract ExtraTransaction hide(SupportFragment... hide);

    public abstract ExtraTransaction runOnExecute(Runnable run);

    public abstract void loadRootFragment(int containerId, SupportFragment to);

    public abstract void start(SupportFragment to);

    public abstract void startWithPop(SupportFragment to);

    public abstract void startWithPopTo(SupportFragment to, Class popToCls);

    public abstract void startWithPopTo(SupportFragment to, Class popToCls,boolean includeTarget);

    public abstract void startForResult(SupportFragment to,int requestCode);

    public abstract void pop();

    public abstract void popChild();

    public abstract void popTo(Class popToCls);

    public abstract void popTo(Class popToCls,boolean includeTarget);

    public abstract void popChildTo(Class popToCls);

    public abstract void popChildTo(Class popToCls,boolean includeTarget);

    public abstract void remove(SupportFragment remove);

    public abstract void remove(SupportFragment remove, boolean anim);

    static class ExtraTransactionImpl extends ExtraTransaction {

        private SupportTransaction supportTransaction;

        private SupportFragment from;

        private TransactionRecord record;

        public ExtraTransactionImpl(SupportTransaction supportTransaction,SupportFragment from){
            this.supportTransaction = supportTransaction;
            this.from = from;
            record = new TransactionRecord();
        }

        @Override
        public ExtraTransaction setCustomerAnimations(int enterAnim, int exitAnim) {
            record.fragmentAnimation = new FragmentAnimation(enterAnim,exitAnim, R.anim.anim_empty,R.anim.anim_empty);
            return this;
        }

        @Override
        public ExtraTransaction setCustomerAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
            record.fragmentAnimation = new FragmentAnimation(enterAnim,exitAnim, popEnterAnim, popExitAnim);
            return this;
        }

        @Override
        public ExtraTransaction setResult(int resultCode, Bundle data) {
            record.resultCode = resultCode;
            record.resultData = data;
            return this;
        }

        @Override
        public ExtraTransaction displayEnterAnim(boolean displayEnterAnim) {
            record.displayEnterAnim = displayEnterAnim;
            return this;
        }

        @Override
        public ExtraTransaction setTag(String tag) {
            record.tag = tag;
            return this;
        }

        @Override
        public ExtraTransaction addBackStack(boolean addBackStack) {
            record.addBackStack = addBackStack;
            return this;
        }

        @Override
        public ExtraTransaction show(SupportFragment... show) {
            record.show = show;
            return this;
        }

        @Override
        public ExtraTransaction hide(SupportFragment... hide) {
            record.hide = hide;
            return this;
        }

        @Override
        public ExtraTransaction runOnExecute(Runnable run) {
            record.runOnExecute = run;
            return this;
        }

        @Override
        public void loadRootFragment(int containerId, SupportFragment to) {
            supportTransaction.doExtraTransaction(from,to,record,SupportTransaction.EXTRA_FT_ACTION_LOAD_ROOT);
        }

        @Override
        public void start(SupportFragment to) {
            supportTransaction.doExtraTransaction(from,to,record,SupportTransaction.EXTRA_FT_ACTION_START);
        }

        @Override
        public void startWithPop(SupportFragment to) {
            supportTransaction.doExtraTransaction(from,to,record,SupportTransaction.EXTRA_FT_ACTION_START_WITH_POP);
        }

        @Override
        public void startWithPopTo(SupportFragment to, Class popToCls) {
            startWithPopTo(to,popToCls);
        }

        @Override
        public void startWithPopTo(SupportFragment to, Class popToCls, boolean includeTarget) {
            record.popToCls = popToCls;
            record.includeTarget = includeTarget;
            supportTransaction.doExtraTransaction(from,to,record,SupportTransaction.EXTRA_FT_ACTION_START_WITH_POP_TO);
        }

        @Override
        public void startForResult(SupportFragment to, int requestCode) {
            record.requestCode = requestCode;
            supportTransaction.doExtraTransaction(from,to,record,SupportTransaction.EXTRA_FT_ACTION_START_FOR_RESULT);
        }

        @Override
        public void pop() {
            supportTransaction.doExtraTransaction(from,null,record,SupportTransaction.EXTRA_FT_ACTION_POP);
        }

        @Override
        public void popChild() {
            supportTransaction.doExtraTransaction(from,null,record,SupportTransaction.EXTRA_FT_ACTION_POP_CHILD);
        }

        @Override
        public void popTo(Class popToCls) {
            popTo(popToCls,true);
        }

        @Override
        public void popTo(Class popToCls, boolean includeTarget) {
            record.popToCls = popToCls;
            record.includeTarget = includeTarget;
            supportTransaction.doExtraTransaction(from,null,record,SupportTransaction.EXTRA_FT_ACTION_POP_TO);
        }

        @Override
        public void popChildTo(Class popToCls) {
            popChildTo(popToCls,true);
        }

        @Override
        public void popChildTo(Class popToCls, boolean includeTarget) {
            record.popToCls = popToCls;
            record.includeTarget = includeTarget;
            supportTransaction.doExtraTransaction(from,null,record,SupportTransaction.EXTRA_FT_ACTION_POP_CHILD_TO);
        }

        @Override
        public void remove(SupportFragment remove) {
            remove(remove,true);
        }

        @Override
        public void remove(SupportFragment remove, boolean anim) {
            record.remove = remove;
            record.removeAnim = anim;
            supportTransaction.doExtraTransaction(from,null,record,SupportTransaction.EXTRA_FT_ACTION_REMOVE);
        }
    }
}
