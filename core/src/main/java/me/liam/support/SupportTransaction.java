package me.liam.support;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;

import java.util.List;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.liam.anim.FragmentAnimation;
import me.liam.helper.FragmentUtils;
import me.liam.queue.Action;
import me.liam.queue.ActionQueue;

public class SupportTransaction {

    final public static String FRAGMENTATION_CONTAINER_ID = "Fragmentation:ContainerId";
    final public static String FRAGMENTATION_TAG = "Fragmentation:Tag";
    final public static String FRAGMENTATION_SIMPLE_NAME = "Fragmentation:SimpleName";
    final public static String FRAGMENTATION_FULL_NAME = "Fragmentation:FullName";
    final public static String FRAGMENTATION_PLAY_ENTER_ANIM = "Fragmentation:PlayEnterAnim";
    final public static String FRAGMENTATION_INIT_LIST = "Fragmentation:InitList";
    final public static String FRAGMENTATION_BACK_STACK = "Fragmentation:AddToBackStack";

    final public static String FRAGMENTATION_ENTER_ANIM_ID = "Fragmentation:EnterAnimId";
    final public static String FRAGMENTATION_EXIT_ANIM_ID = "Fragmentation:ExitAnimId";
    final public static String FRAGMENTATION_POP_ENTER_ANIM_ID = "Fragmentation:PopEnterAnimId";
    final public static String FRAGMENTATION_POP_EXIT_ANIM_ID = "Fragmentation:PopExitAnimId";

    final public static String FRAGMENTATION_SAVED_INSTANCE = "Fragmentation:SavedInstance";

    final public static String FRAGMENTATION_REQUEST_CODE = "Fragmentation:RequestCode";
    final public static String FRAGMENTATION_FROM_REQUEST_CODE = "Fragmentation:FromRequestCode";
    final public static String FRAGMENTATION_RESULT_CODE = "Fragmentation:ResultCode";
    final public static String FRAGMENTATION_RESULT_DATA = "Fragmentation:ResultData";

    private ISupportActivity iSupportActivity;

    private FragmentActivity supportActivity;

    private ActionQueue actionQueue;

//    private Context context;

    SupportTransaction(ISupportActivity iSupportActivity) {
        this.iSupportActivity = iSupportActivity;
        this.supportActivity = (FragmentActivity) iSupportActivity;
        actionQueue = new ActionQueue(new Handler(Looper.myLooper()));

    }

    public Bundle getArguments(SupportFragment target){
        if (target.getArguments() == null){
            target.setArguments(new Bundle());
        }
        return target.getArguments();
    }

    private void bindFragmentOptions(SupportFragment target, int containerId, boolean playEnterAnim, boolean addToBackStack){
        Bundle args = getArguments(target);
        args.putInt(FRAGMENTATION_CONTAINER_ID, containerId);
        args.putString(FRAGMENTATION_TAG, UUID.randomUUID().toString());
        args.putString(FRAGMENTATION_SIMPLE_NAME, target.getClass().getSimpleName());
        args.putString(FRAGMENTATION_FULL_NAME, target.getClass().getName());
        args.putBoolean(FRAGMENTATION_PLAY_ENTER_ANIM, playEnterAnim);
        args.putBoolean(FRAGMENTATION_INIT_LIST, false);
        args.putBoolean(FRAGMENTATION_SAVED_INSTANCE,false);
        args.putBoolean(FRAGMENTATION_BACK_STACK,addToBackStack);
    }

    private void supportCommit(FragmentTransaction ft) {
        supportCommit(ft,null);
    }

    private void supportCommit(FragmentTransaction ft,Runnable runnable) {
        if (runnable != null){
            ft.runOnCommit(runnable);
        }
        ft.commitAllowingStateLoss();
    }

    void loadRootFragment(final FragmentManager fm, final int containerId, final SupportFragment to, final FragmentAnimation anim, final boolean playEnterAnim, final boolean addToBackStack){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                bindFragmentOptions(to,containerId,playEnterAnim,addToBackStack);
                to.setFragmentAnimation(anim);
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.add(containerId,to);
                supportCommit(ft);
                return 0;
            }
        });
    }

    void loadMultipleRootFragments(final FragmentManager fm, final int containerId, final int showPosition, final SupportFragment... fragments){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                int position = 1;
                FragmentTransaction ft = fm.beginTransaction();
                for (SupportFragment to : fragments){
                    bindFragmentOptions(to,containerId,false,false);
                    to.setFragmentAnimation(null);
                    ft.add(containerId,to);
                    if (position == showPosition){
                        ft.show(to);
                    }else {
                        ft.hide(to);
                    }
                }
                supportCommit(ft);
                return 0;
            }
        });
    }

    void showHideAllFragment(final FragmentManager fm, final SupportFragment show){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                FragmentTransaction ft = fm.beginTransaction();
                for (SupportFragment f : FragmentUtils.getInManagerFragments(fm)){
                    if (f == show){
                        ft.show(f);
                    }else {
                        ft.hide(f);
                    }
                }
                supportCommit(ft);
                return 0;
            }
        });
    }

    void start(final SupportFragment from, final SupportFragment to,final boolean addToBackStack){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                bindFragmentOptions(to,from.getContainerId(),true,addToBackStack);
                to.setFragmentAnimation(iSupportActivity.getDefaultAnimation());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.add(to.getContainerId(),to);
                supportCommit(ft);
                return 0;
            }
        });
    }


    void pop(final FragmentManager fm){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                SupportFragment remove = FragmentUtils.getLastFragment(fm);
                onResult(remove);
                if (remove == null) return 0;
                long duration = AnimationUtils.loadAnimation(remove.getContext(),remove.getFragmentAnimation().getExitAnimId()).getDuration();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.remove(remove);
                supportCommit(ft);
                return duration;
            }

            @Override
            public int actionType() {
                return Action.TYPE_POP;
            }
        });
    }

    void startWithPop(final SupportFragment from, final SupportFragment to){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                bindFragmentOptions(to,from.getContainerId(),true,true);
                to.setFragmentAnimation(iSupportActivity.getDefaultAnimation());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.add(to.getContainerId(),to);
                supportCommit(ft);
                to.setCallBack(new SupportFragmentCallBack(){
                    @Override
                    public void onEnterAnimEnd() {
                        silenceRemove(from.getFragmentManager(),from);
                    }
                });
                return 0;
            }
        });
    }

    void silenceRemove(final FragmentManager fm, final SupportFragment... removes){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                FragmentTransaction ft = fm.beginTransaction();
                for (SupportFragment f : removes){
                    ft.remove(f);
                }
                supportCommit(ft);
                return 0;
            }
        });
    }

    void popTo(final FragmentManager fm, final Class cls, final boolean includeTarget){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                SupportFragment remove = FragmentUtils.getLastFragment(fm);
                SupportFragment target = FragmentUtils.findFragmentByClass(fm,cls);
                if (remove == null || target == null) return 0;
                FragmentTransaction ft = fm.beginTransaction();
                int targetIndex = fm.getFragments().indexOf(target);
                int removeIndex = fm.getFragments().indexOf(remove);
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
                supportCommit(ft);
                return 0;
            }

            @Override
            public int actionType() {
                return Action.TYPE_POP;
            }
        });
    }

    void startForResult(SupportFragment from, SupportFragment to, int requestCode){
        Bundle formBundle = getArguments(from);
        formBundle.putInt(FRAGMENTATION_REQUEST_CODE,requestCode);
        Bundle toBundle = getArguments(to);
        toBundle.putInt(FRAGMENTATION_FROM_REQUEST_CODE,requestCode);
        start(from, to,true);
    }

    void setResult(SupportFragment target, int resultCode, Bundle data){
        Bundle bundle = getArguments(target);
        bundle.putInt(FRAGMENTATION_RESULT_CODE,resultCode);
        bundle.putBundle(FRAGMENTATION_RESULT_DATA,data);
    }

    void onResult(SupportFragment target){
        if (target == null) return;
        Bundle bundle = getArguments(target);
        int fromRequestCode = bundle.getInt(FRAGMENTATION_FROM_REQUEST_CODE,-1);
        int resultCode = bundle.getInt(FRAGMENTATION_RESULT_CODE,-1);
        if (resultCode == -1 || fromRequestCode == -1) return;
        Bundle data = bundle.getBundle(FRAGMENTATION_RESULT_DATA);
        if (target.getFragmentManager() == null) return;
        List<SupportFragment> list = FragmentUtils.getActiveList(target.getFragmentManager());
        for (SupportFragment f : list){
            if (getArguments(f).getInt(FRAGMENTATION_REQUEST_CODE) == fromRequestCode){
                f.onResult(fromRequestCode,resultCode,data);
            }
        }
    }

    void popAll(final FragmentManager fm){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                for (Fragment f : fm.getFragments()){
                    if (f instanceof SupportFragment && !f.isRemoving() && !f.isDetached()){
                        ft.remove(f);
                    }
                }
                supportCommit(ft);
                return 0;
            }

            @Override
            public int actionType() {
                return Action.TYPE_POP;
            }
        });
    }

    void remove(final SupportFragment remove, final boolean anim){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                if (remove == null || remove.getFragmentManager() == null) return 0;
                FragmentTransaction ft = remove.getFragmentManager().beginTransaction();
                if (anim){
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }
                ft.remove(remove);
                supportCommit(ft);
                return 0;
            }

            @Override
            public int actionType() {
                return Action.TYPE_POP;
            }
        });
    }

    void startWithPopTo(final SupportFragment from, final SupportFragment to,final Class cls, final boolean includeTarget){
        actionQueue.enqueue(new Action() {
            @Override
            public long run() {
                FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                bindFragmentOptions(to,from.getContainerId(),true,true);
                to.setFragmentAnimation(iSupportActivity.getDefaultAnimation());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.add(to.getContainerId(),to);
                supportCommit(ft);
                to.setCallBack(new SupportFragmentCallBack(){
                    @Override
                    public void onEnterAnimEnd() {
                        popTo(from.getFragmentManager(),cls,includeTarget);
                    }
                });
                return 0;
            }

            @Override
            public int actionType() {
                return Action.TYPE_POP;
            }
        });
    }
}
