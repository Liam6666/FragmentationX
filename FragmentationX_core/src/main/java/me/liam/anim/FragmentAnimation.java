package me.liam.anim;

import me.liam.fragmentation.R;

public class FragmentAnimation {

    int enterAnimId;
    int exitAnimId;
    int popEnterAnimId;
    int popExitAnimId;

    public FragmentAnimation(int enterAnimId, int exitAnimId) {
        this(enterAnimId,exitAnimId,R.anim.anim_empty,R.anim.anim_empty);
    }

    public FragmentAnimation(int enterAnimId, int exitAnimId, int popEnterAnimId, int popExitAnimId) {
        this.enterAnimId = enterAnimId;
        this.exitAnimId = exitAnimId;
        this.popEnterAnimId = popEnterAnimId;
        this.popExitAnimId = popExitAnimId;
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

}
