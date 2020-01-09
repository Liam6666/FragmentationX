package me.liam.fragmentation.support

import android.os.Bundle

/**
 * 管理Fragment的显示和隐藏状态
 */
internal class SupportFragmentVisible {
    private var supportFragment: SupportFragment? = null

    fun onAttach(supportFragment: SupportFragment) {
        this.supportFragment = supportFragment
    }

    fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState?.getBoolean(FRAGMENTATION_IS_HIDDEN) == true) {
            supportFragment?.fragmentManager?.beginTransaction()?.apply {
                supportFragment?.let { hide(it) }
                commitAllowingStateLoss()
            }
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(FRAGMENTATION_IS_HIDDEN, supportFragment?.isHidden ?: false)
    }

    companion object {
        const val FRAGMENTATION_IS_HIDDEN = "Fragmentation:IsHidden"
    }
}
