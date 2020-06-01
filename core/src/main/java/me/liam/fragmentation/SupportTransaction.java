package me.liam.fragmentation;

import androidx.fragment.app.FragmentActivity;

class SupportTransaction {

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

    final public static String FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM = "Fragmentation:DontDisplaySelfPopAnim";

    final public static String FRAGMENTATION_SAVED_INSTANCE = "Fragmentation:SavedInstance";

    final public static String FRAGMENTATION_REQUEST_CODE = "Fragmentation:RequestCode";
    final public static String FRAGMENTATION_FROM_REQUEST_CODE = "Fragmentation:FromRequestCode";
    final public static String FRAGMENTATION_RESULT_CODE = "Fragmentation:ResultCode";
    final public static String FRAGMENTATION_RESULT_DATA = "Fragmentation:ResultData";

    private FragmentActivity fragmentActivity;

    public SupportTransaction(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }
}
