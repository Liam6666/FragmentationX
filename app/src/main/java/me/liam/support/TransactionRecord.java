package me.liam.support;

import android.os.Bundle;

import java.util.UUID;

import me.liam.anim.FragmentAnimation;
import me.liam.fragmentation.R;

class TransactionRecord {

    FragmentAnimation fragmentAnimation = new FragmentAnimation(R.anim.anim_empty,R.anim.anim_empty,R.anim.anim_empty,R.anim.anim_empty);

    int requestCode = -1;

    int resultCode = -1;

    Bundle resultData;

    boolean displayEnterAnim = true;

    String tag = UUID.randomUUID().toString();

    boolean addBackStack = true;

    Runnable runOnExecute;

    Class popToCls;

    boolean includeTarget = true;

    SupportFragment remove;

    boolean removeAnim = true;

}
