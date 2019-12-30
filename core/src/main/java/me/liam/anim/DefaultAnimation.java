package me.liam.anim;

import me.liam.fragmentation.R;

public final class DefaultAnimation extends FragmentAnimation {

    public DefaultAnimation() {
        super(R.anim.classic_horizontal_enter,R.anim.classic_horizontal_exit,R.anim.classic_horizontal_popenter,R.anim.classic_horizontal_popexit);
    }
}
