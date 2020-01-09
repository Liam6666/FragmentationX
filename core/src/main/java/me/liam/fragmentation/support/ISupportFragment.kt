package me.liam.fragmentation.support

import android.os.Bundle
import androidx.annotation.IdRes

import me.liam.fragmentation.anim.FragmentAnimation

interface ISupportFragment {

    val extraTransaction: ExtraTransaction

    fun <T : SupportFragment> findFragment(cls: Class<*>): T?

    fun <T : SupportFragment> findChildFragment(cls: Class<*>): T?

    fun dispatcherOnBackPressed(): Boolean

    fun onBackPressed(): Boolean

    fun onCreateCustomerAnimation(): FragmentAnimation?

    fun onLazyInit(savedInstanceState: Bundle?)

    fun onEnterAnimEnd()

    fun onSupportPause()

    fun onSupportResume()

    fun onSwipeDrag(beforeOne: SupportFragment, state: Int, scrollPercent: Float)

    fun onResult(requestCode: Int, resultCode: Int, data: Bundle?)

    fun onPostedData(code: Int, data: Bundle)

    fun setResult(resultCode: Int, data: Bundle)

    fun loadRootFragment(@IdRes containerId: Int, to: SupportFragment, anim: FragmentAnimation, playEnterAnim: Boolean, addToBackStack: Boolean)

    fun loadRootFragment(@IdRes containerId: Int, to: SupportFragment)

    fun loadMultipleRootFragments(@IdRes containerId: Int, showPosition: Int, vararg fragments: SupportFragment)

    fun showHideAllFragment(show: SupportFragment)

    fun start(to: SupportFragment, addToBackStack: Boolean = true)

    fun startForResult(to: SupportFragment, requestCode: Int)

    fun startWithPop(to: SupportFragment)

    fun startWithPopTo(to: SupportFragment, cls: Class<*>, includeTarget: Boolean = true)

    fun pop()

    fun popTo(cls: Class<*>, includeTarget: Boolean = true)

    fun popChild()

    fun popChildTo(cls: Class<*>, includeTarget: Boolean = true)

    fun popAllChild()
}
