package me.liam.fragmentation.support

import android.os.Bundle
import androidx.annotation.IdRes

import me.liam.fragmentation.anim.FragmentAnimation

interface ISupportActivity {

    val defaultBackground: Int

    var defaultAnimation: FragmentAnimation

    fun <T : SupportFragment> findFragment(cls: Class<T>): T?

    fun postDataToFragments(code: Int, data: Bundle)

    fun loadRootFragment(@IdRes containerId: Int, to: SupportFragment, anim: FragmentAnimation, playEnterAnim: Boolean, addToBackStack: Boolean)

    fun loadRootFragment(@IdRes containerId: Int, to: SupportFragment)

    fun loadMultipleRootFragments(@IdRes containerId: Int, showPosition: Int, vararg fragments: SupportFragment)

    fun showHideAllFragment(show: SupportFragment)

    fun start(to: SupportFragment, addToBackStack: Boolean = true)

    fun start(from: SupportFragment, to: SupportFragment, addToBackStack: Boolean = true)

    fun startWithPop(to: SupportFragment)

    fun startWithPop(from: SupportFragment, to: SupportFragment)

    fun startWithPopTo(to: SupportFragment, cls: Class<*>, includeTarget: Boolean = true)

    fun startWithPopTo(from: SupportFragment, to: SupportFragment, cls: Class<*>, includeTarget: Boolean = true)

    fun pop()

    fun popTo(cls: Class<*>, includeTarget: Boolean = true)

}
