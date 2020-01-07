package me.liam.support;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import me.liam.anim.FragmentAnimation;
import me.liam.fragmentation.R;
import me.liam.helper.FragmentUtils;
import me.liam.queue.Action;
import me.liam.queue.ActionQueue;

public abstract class ExtraTransaction {

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim);

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim,int popEnterAnim,int popExitAnim);

    public abstract ExtraTransaction dontDisplaySelfPopAnim(boolean dontDisplaySelfPopAnim);

    public abstract ExtraTransaction setResult(int resultCode, Bundle data);

    public abstract ExtraTransaction displayEnterAnim(boolean displayEnterAnim);

    public abstract ExtraTransaction setTag(String tag);

    public abstract ExtraTransaction addBackStack(boolean addBackStack);

    public abstract ExtraTransaction runOnExecute(Runnable run);

    public abstract void loadRootFragment(int containerId, SupportFragment to);

    public abstract void show(SupportFragment... show);

    public abstract void hide(SupportFragment... hide);

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

    public abstract void remove(SupportFragment... remove);

    static class ExtraTransactionImpl extends ExtraTransaction {

        private SupportFragment from;

        private TransactionRecord record;

        private ActionQueue actionQueue;

        public ExtraTransactionImpl(SupportFragment from, Handler handler){
            this.from = from;
            record = new TransactionRecord();
            actionQueue = new ActionQueue(handler);
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
        public ExtraTransaction dontDisplaySelfPopAnim(boolean dontDisplaySelfPopAnim) {
            record.dontDisplaySelfPopAnim = dontDisplaySelfPopAnim;
            return this;
        }

        @Override
        public ExtraTransaction setResult(int resultCode, Bundle data) {
            record.resultCode = resultCode;
            record.resultData = data;
            setResult(from,resultCode,data);
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
        public ExtraTransaction runOnExecute(Runnable run) {
            record.runOnExecute = run;
            return this;
        }

        @Override
        public void loadRootFragment(final int containerId, final SupportFragment to) {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    bindFragmentOptions(to,containerId,record);
                    to.setFragmentAnimation(record.fragmentAnimation);
                    FragmentTransaction ft = from.getChildFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.add(containerId,to,record.tag);
                    supportCommit(ft,record.runOnExecute);
                    return 0;
                }
            });
        }

        @Override
        public void show(final SupportFragment... show) {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    for (SupportFragment f : show){
                        ft.show(f);
                    }
                    supportCommit(ft,record.runOnExecute);
                    return 0;
                }
            });
        }

        @Override
        public void hide(final SupportFragment... hide) {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    for (SupportFragment f : hide){
                        ft.hide(f);
                    }
                    supportCommit(ft,record.runOnExecute);
                    return 0;
                }
            });
        }

        @Override
        public void start(final SupportFragment to) {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    bindFragmentOptions(to,from.getContainerId(),record);
                    to.setFragmentAnimation(record.fragmentAnimation);
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.add(from.getContainerId(),to,record.tag);
                    supportCommit(ft,record.runOnExecute);
                    return 0;
                }
            });
        }

        @Override
        public void startWithPop(final SupportFragment to) {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    bindFragmentOptions(to,from.getContainerId(),record);
                    to.setFragmentAnimation(record.fragmentAnimation);
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.add(from.getContainerId(),to,record.tag);
                    supportCommit(ft,record.runOnExecute);
                    to.setCallBack(new SupportFragmentCallBack(){
                        @Override
                        public void onEnterAnimEnd() {
                            FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                            ft.remove(from);
                            supportCommit(ft);
                        }
                    });
                    return 0;
                }
            });
        }

        @Override
        public void startWithPopTo(SupportFragment to, Class popToCls) {
            startWithPopTo(to,popToCls,true);
        }

        @Override
        public void startWithPopTo(final SupportFragment to, Class popToCls, boolean includeTarget) {
            record.popToCls = popToCls;
            record.includeTarget = includeTarget;
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    bindFragmentOptions(to,from.getContainerId(),record);
                    to.setFragmentAnimation(record.fragmentAnimation);
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.add(from.getContainerId(),to,record.tag);
                    supportCommit(ft,record.runOnExecute);
                    to.setCallBack(new SupportFragmentCallBack(){
                        @Override
                        public void onEnterAnimEnd() {
                            popTo(from.getFragmentManager(),record.popToCls,record.includeTarget,null);
                        }
                    });
                    return 0;
                }
            });
        }

        @Override
        public void startForResult(SupportFragment to, int requestCode) {
            record.requestCode = requestCode;
            setStartForResult(from,to,requestCode);
            start(to);
        }

        @Override
        public void pop() {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    SupportFragment remove = FragmentUtils.getLastFragment(from.getFragmentManager());
                    if (remove == null) return 0;
                    long duration = AnimationUtils.loadAnimation(remove.getContext(),remove.getFragmentAnimation().getExitAnimId()).getDuration();
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.remove(remove);
                    supportCommit(ft,record.runOnExecute);
                    return duration;
                }

                @Override
                public int actionType() {
                    return Action.TYPE_POP;
                }
            });
        }

        @Override
        public void popChild() {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    SupportFragment remove = FragmentUtils.getLastFragment(from.getChildFragmentManager());
                    if (remove == null) return 0;
                    long duration = AnimationUtils.loadAnimation(remove.getContext(),remove.getFragmentAnimation().getExitAnimId()).getDuration();
                    FragmentTransaction ft = from.getChildFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.remove(remove);
                    supportCommit(ft,record.runOnExecute);
                    return duration;
                }

                @Override
                public int actionType() {
                    return Action.TYPE_POP;
                }
            });
        }

        @Override
        public void popTo(Class popToCls) {
            record.popToCls = popToCls;
            popTo(record.popToCls,true);
        }

        @Override
        public void popTo(Class popToCls, boolean includeTarget) {
            record.popToCls = popToCls;
            record.includeTarget = includeTarget;
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    popTo(from.getFragmentManager(),record.popToCls,record.includeTarget,record.runOnExecute);
                    return 0;
                }

                @Override
                public int actionType() {
                    return Action.TYPE_POP;
                }
            });
        }

        @Override
        public void popChildTo(Class popToCls) {
            record.popToCls = popToCls;
            popChildTo(record.popToCls,true);
        }

        @Override
        public void popChildTo(Class popToCls, boolean includeTarget) {
            record.popToCls = popToCls;
            record.includeTarget = includeTarget;
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    popTo(from.getChildFragmentManager(),record.popToCls,record.includeTarget,record.runOnExecute);
                    return 0;
                }

                @Override
                public int actionType() {
                    return Action.TYPE_POP;
                }
            });
        }

        @Override
        public void remove(SupportFragment remove) {
            record.remove = remove;
            remove(remove,true);
        }

        @Override
        public void remove(final SupportFragment remove, final boolean anim) {
            record.remove = remove;
            record.removeAnim = anim;
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    if (from == null || from.getFragmentManager() == null) return 0;
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    if (anim){
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    }
                    ft.remove(remove);
                    supportCommit(ft,record.runOnExecute);
                    return 0;
                }

                @Override
                public int actionType() {
                    return Action.TYPE_POP;
                }
            });
        }

        @Override
        public void remove(final SupportFragment... remove) {
            actionQueue.enqueue(new Action() {
                @Override
                public long run() {
                    if (from == null || from.getFragmentManager() == null) return 0;
                    FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                    for (SupportFragment f : remove){
                        ft.remove(f);
                    }
                    supportCommit(ft,record.runOnExecute);
                    return 0;
                }

                @Override
                public int actionType() {
                    return Action.TYPE_POP;
                }
            });
        }

        Bundle getArguments(SupportFragment target){
            if (target.getArguments() == null){
                target.setArguments(new Bundle());
            }
            return target.getArguments();
        }

        void bindFragmentOptions(SupportFragment target, int containerId,TransactionRecord record) {
            Bundle args = getArguments(target);
            args.putInt(SupportTransaction.FRAGMENTATION_CONTAINER_ID, containerId);
            args.putString(SupportTransaction.FRAGMENTATION_TAG, record.tag);
            args.putString(SupportTransaction.FRAGMENTATION_SIMPLE_NAME, target.getClass().getSimpleName());
            args.putString(SupportTransaction.FRAGMENTATION_FULL_NAME, target.getClass().getName());
            args.putBoolean(SupportTransaction.FRAGMENTATION_PLAY_ENTER_ANIM, record.displayEnterAnim);
            args.putBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST, false);
            args.putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE, false);
            args.putBoolean(SupportTransaction.FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM,record.dontDisplaySelfPopAnim);
        }

        void supportCommit(FragmentTransaction ft) {
            supportCommit(ft,null);
        }

        void supportCommit(FragmentTransaction ft,Runnable runnable) {
            if (runnable != null){
                ft.runOnCommit(runnable);
            }
            ft.commitAllowingStateLoss();
        }

        void setStartForResult(SupportFragment from, SupportFragment to, int requestCode){
            Bundle formBundle = getArguments(from);
            formBundle.putInt(SupportTransaction.FRAGMENTATION_REQUEST_CODE,requestCode);
            Bundle toBundle = getArguments(to);
            toBundle.putInt(SupportTransaction.FRAGMENTATION_FROM_REQUEST_CODE,requestCode);
        }

        void setResult(SupportFragment target, int resultCode, Bundle data){
            Bundle bundle = getArguments(target);
            bundle.putInt(SupportTransaction.FRAGMENTATION_RESULT_CODE,resultCode);
            bundle.putBundle(SupportTransaction.FRAGMENTATION_RESULT_DATA,data);
        }

        void popTo(FragmentManager fm,Class cls,boolean includeTarget,Runnable run){
            SupportFragment remove = FragmentUtils.getLastFragment(fm);
            SupportFragment target = FragmentUtils.findFragmentByClass(fm,cls);
            if (remove == null || target == null) return;
            int targetIndex = fm.getFragments().indexOf(target);
            int removeIndex = fm.getFragments().indexOf(remove);
            FragmentTransaction ft = fm.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            List<Fragment> removeList = fm.getFragments().subList(targetIndex,removeIndex);
            if (!includeTarget){
                removeList.remove(target);
            }
            for (Fragment f : removeList){
                if (f instanceof SupportFragment){
                    ft.remove(f);
                }
            }
            ft.remove(remove);
            supportCommit(ft,run);
        }
    }
}
