package me.liam.support;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;

import com.blankj.utilcode.util.ToastUtils;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.liam.anim.FragmentAnimation;
import me.liam.anim.NoneAnim;
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

    final public static String FRAGMENTATION_ENTER_ANIM_ID = "Fragmentation:EnterAnimId";
    final public static String FRAGMENTATION_EXIT_ANIM_ID = "Fragmentation:ExitAnimId";
    final public static String FRAGMENTATION_POP_ENTER_ANIM_ID = "Fragmentation:PopEnterAnimId";
    final public static String FRAGMENTATION_POP_EXIT_ANIM_ID = "Fragmentation:PopExitAnimId";

    final public static String FRAGMENTATION_SAVED_INSTANCE = "Fragmentation:SavedInstance";

    private ISupportActivity iSupportActivity;

    private FragmentActivity supportActivity;

    private ActionQueue actionQueue;

//    private Context context;

    SupportTransaction(ISupportActivity iSupportActivity) {
        this.iSupportActivity = iSupportActivity;
        this.supportActivity = (FragmentActivity) iSupportActivity;
        actionQueue = new ActionQueue(new Handler(Looper.myLooper()));

    }

//    SupportTransaction(Context context) {
//        this.context = context;
//        actionQueue = new ActionQueue(new Handler(Looper.myLooper()));
//
//    }

    public Bundle getArguments(SupportFragment target){
        if (target.getArguments() == null){
            target.setArguments(new Bundle());
        }
        return target.getArguments();
    }

    private void bindFragmentOptions(SupportFragment target, int containerId, boolean playEnterAnim){
        Bundle args = getArguments(target);
        args.putInt(FRAGMENTATION_CONTAINER_ID, containerId);
        args.putString(FRAGMENTATION_TAG, UUID.randomUUID().toString());
        args.putString(FRAGMENTATION_SIMPLE_NAME, target.getClass().getSimpleName());
        args.putString(FRAGMENTATION_FULL_NAME, target.getClass().getName());
        args.putBoolean(FRAGMENTATION_PLAY_ENTER_ANIM, playEnterAnim);
        args.putBoolean(FRAGMENTATION_INIT_LIST, false);
        args.putBoolean(FRAGMENTATION_SAVED_INSTANCE,false);
    }

    private void supportCommit(FragmentTransaction ft) {
        ft.commitAllowingStateLoss();
    }

    private void withPopAnim(final SupportFragment from, SupportFragment to){
        if (to == null || from == null) return;
        to.setiSupportAnimation(new ISupportAnimation() {
            @Override
            public void onTargetFragmentCreateAnimations(int transit, boolean enter, int nextAnim) {
                switch (transit){
                    case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                        if (enter){
                            from.getFragmentAnimation().playPopExitAnim();
                        }
                        break;
                    case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                        if (!enter){
                            from.getFragmentAnimation().playPopEnterAnim();
                        }
                        break;
                }
            }
        });
    }

    void loadRootFragment(final FragmentManager fm, final int containerId, final SupportFragment to, final FragmentAnimation anim, final boolean playEnterAnim){
        actionQueue.enqueue(new Action() {
            @Override
            public void run() {
                bindFragmentOptions(to,containerId,playEnterAnim);
                to.setFragmentAnimation(anim);
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.add(containerId,to);
                supportCommit(ft);
            }
        });
    }

    void loadMultipleRootFragments(final FragmentManager fm, final int containerId, final int showPosition, final SupportFragment... fragments){
        actionQueue.enqueue(new Action() {
            @Override
            public void run() {
                int position = 1;
                FragmentTransaction ft = fm.beginTransaction();
                for (SupportFragment to : fragments){
                    bindFragmentOptions(to,containerId,false);
                    to.setFragmentAnimation(null);
                    ft.add(containerId,to);
                    if (position == showPosition){
                        ft.show(to);
                    }else {
                        ft.hide(to);
                    }
                }
                supportCommit(ft);
            }
        });
    }

    void showHideAllFragment(final FragmentManager fm, final SupportFragment show){
        actionQueue.enqueue(new Action() {
            @Override
            public void run() {
                FragmentTransaction ft = fm.beginTransaction();
                for (SupportFragment f : FragmentUtils.getInManagerFragments(fm)){
                    if (f == show){
                        ft.show(f);
                    }else {
                        ft.hide(f);
                    }
                }
                supportCommit(ft);
            }
        });
    }

    void start(final SupportFragment from, final SupportFragment to){
        actionQueue.enqueue(new Action() {
            @Override
            public void run() {
                if (from.getFragmentManager() == null) return;
                FragmentTransaction ft = from.getFragmentManager().beginTransaction();
                bindFragmentOptions(to,from.getArguments().getInt(FRAGMENTATION_CONTAINER_ID),true);
                to.setFragmentAnimation(iSupportActivity.getDefaultAnimation());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.add(to.getArguments().getInt(FRAGMENTATION_CONTAINER_ID),to);
                supportCommit(ft);
                withPopAnim(from, to);
            }
        });
    }

    void pop(final FragmentManager fm){
        actionQueue.enqueue(new Action() {
            @Override
            public void run() {
                SupportFragment remove = FragmentUtils.getLastOne(fm);
                if (remove == null) return;
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.remove(remove);
                supportCommit(ft);
            }

            @Override
            public long getDuration() {
                return ACTION_DEFAULT_DELAY;
            }
        });
    }
}
