package me.liam.anim;

import me.liam.fragmentation.R;

public class ClassicVerticalAnim extends FragmentAnimation {

    public ClassicVerticalAnim() {
        super(R.anim.classic_vertical_enter,
                R.anim.classic_vertical_exit,
                R.anim.classic_vertical_popenter,
                R.anim.classic_vertical_popexit);
    }
}
