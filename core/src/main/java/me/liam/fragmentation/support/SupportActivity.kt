package me.liam.fragmentation.support

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent

import java.util.ArrayList
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import me.liam.fragmentation.anim.ClassicHorizontalAnim
import me.liam.fragmentation.anim.FragmentAnimation
import me.liam.fragmentation.helper.findFragment
import me.liam.fragmentation.helper.getAllFragments
import me.liam.fragmentation.helper.getInManagerFragments
import me.liam.fragmentation.helper.getLastFragment

open class SupportActivity : AppCompatActivity(), ISupportActivity {

    internal lateinit var supportTransaction: SupportTransaction
        private set

    /**
     * 设置全局默认动画
     *
     * @param animation
     */
    override var defaultAnimation: FragmentAnimation = ClassicHorizontalAnim()
        set(animation) {
            field = animation
            val fragmentList = ArrayList<SupportFragment>()
            supportFragmentManager.getAllFragments(fragmentList)
            for (f in fragmentList) {
                f.fragmentAnimation = field
            }
        }

    internal var fragmentClickable = true

    internal var fragmentSwipeDrag = false

    /**
     * 获取默认的Fragment背景颜色
     *
     * @return
     */
    override val defaultBackground: Int get() = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportTransaction = SupportTransaction(this)
    }

    override fun onBackPressed() {
        if (fragmentSwipeDrag) return
        val activeFragment = supportFragmentManager.getLastFragment()
        if (activeFragment?.dispatcherOnBackPressed() == true) {
            return
        }
        if (supportFragmentManager.getInManagerFragments().size > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return if (!fragmentClickable) true else super.dispatchTouchEvent(ev)
    }

    /**
     * 在栈内查找Fragment对象，如果一个栈内存在多个同一个class的Fragment对象，则返回结果可能不准确
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    override fun <T : SupportFragment> findFragment(cls: Class<T>): T? {
        return supportFragmentManager.findFragment<SupportFragment>(cls) as? T
    }

    /**
     * 发送一条消息到当前所有的Fragment
     *
     * @param code
     * @param data
     */
    override fun postDataToFragments(code: Int, data: Bundle) {
        val fragmentList = ArrayList<SupportFragment>()
        supportFragmentManager.getAllFragments(fragmentList)
        for (f in fragmentList) {
            f.onPostedData(code, data)
        }
    }

    /**
     * 加载第一个Fragment
     *
     * @param containerId
     * @param to
     * @param anim
     * @param playEnterAnim
     * @param addToBackStack
     */
    override fun loadRootFragment(containerId: Int, to: SupportFragment, anim: FragmentAnimation, playEnterAnim: Boolean, addToBackStack: Boolean) {
        supportTransaction?.loadRootFragment(supportFragmentManager, containerId, to, anim, playEnterAnim, addToBackStack)
    }

    /**
     * 加载第一个Fragment
     *
     * @param containerId
     * @param to
     */
    override fun loadRootFragment(containerId: Int, to: SupportFragment) {
        supportTransaction?.loadRootFragment(supportFragmentManager, containerId, to, defaultAnimation, false, true)
    }

    /**
     * 加载多个Fragment
     *
     * @param containerId
     * @param showPosition
     * @param fragments
     */
    override fun loadMultipleRootFragments(containerId: Int, showPosition: Int, vararg fragments: SupportFragment) {
        supportTransaction?.loadMultipleRootFragments(supportFragmentManager, containerId, showPosition, *fragments)
    }

    /**
     * 显示一个Fragment，并隐藏当前Child Fragment Manager 栈内的其它Fragment
     *
     * @param show
     */
    override fun showHideAllFragment(show: SupportFragment) {
        supportTransaction?.showHideAllFragment(supportFragmentManager, show)
    }

    /**
     * 启动一个新的Fragment，栈内必须有至少1个Fragment才可以使用此方法
     *
     * @param to
     * @param addToBackStack
     */
    override fun start(to: SupportFragment, addToBackStack: Boolean) {
        supportTransaction?.start(supportFragmentManager.getLastFragment(), to, addToBackStack)
    }

    /**
     * 启动一个新的Fragment，栈内必须有至少1个Fragment才可以使用此方法
     *
     * @param from
     * @param to
     * @param addToBackStack
     */
    override fun start(from: SupportFragment, to: SupportFragment, addToBackStack: Boolean) {
        supportTransaction?.start(from, to, addToBackStack)
    }

    /**
     * 启动一个新的Fragment并关闭当前Fragment
     *
     * @param to
     */
    override fun startWithPop(to: SupportFragment) {
        supportTransaction?.startWithPop(supportFragmentManager.getLastFragment(), to)
    }

    /**
     * 启动一个新的Fragment并关闭当前Fragment
     *
     * @param from
     * @param to
     */
    override fun startWithPop(from: SupportFragment, to: SupportFragment) {
        supportTransaction?.startWithPop(from, to)
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     *
     * @param to
     * @param cls
     * @param includeTarget
     */
    override fun startWithPopTo(to: SupportFragment, cls: Class<*>, includeTarget: Boolean) {
        supportTransaction?.startWithPopTo(supportFragmentManager.getLastFragment(), to, cls, includeTarget)
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     *
     * @param from
     * @param to
     * @param cls
     * @param includeTarget
     */
    override fun startWithPopTo(from: SupportFragment, to: SupportFragment, cls: Class<*>, includeTarget: Boolean) {
        supportTransaction?.startWithPopTo(from, to, cls, includeTarget)
    }

    /**
     * 弹出栈内最后一个Fragment
     */
    override fun pop() {
        supportTransaction?.pop(supportFragmentManager)
    }

    /**
     * 弹出栈内的Fragment到指定位置
     *
     * @param cls
     * @param includeTarget
     */
    override fun popTo(cls: Class<*>, includeTarget: Boolean) {
        supportTransaction?.popTo(supportFragmentManager, cls, includeTarget)
    }
}
