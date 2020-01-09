package me.liam.fragmentation.support

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import me.liam.fragmentation.anim.FragmentAnimation
import me.liam.fragmentation.anim.NoneAnim
import me.liam.fragmentation.R
import me.liam.fragmentation.helper.*
import me.liam.fragmentation.swipeback.SwipeBackLayout

open class SupportFragment : Fragment(), ISupportFragment {

    var fragmentAnimation: FragmentAnimation? = null

    private var supportFragmentVisible = SupportFragmentVisible()

    private var handler = Handler(Looper.getMainLooper()) { false }

    private var animEndListener: (() -> Unit)? = null
    private var swipeBackLayout: SwipeBackLayout? = null

    private var defaultBackgroundColor: Int = Color.WHITE

    private val isSavedInstance: Boolean
        get() = arguments?.getBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE, false)
                ?: false

    val containerId: Int
        get() = arguments!!.getInt(SupportTransaction.FRAGMENTATION_CONTAINER_ID, 0)

    /**
     * 自定义事件
     *
     * @return
     */
    override val extraTransaction: ExtraTransaction
        get() = ExtraTransaction.ExtraTransactionImpl(this, handler)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        supportFragmentVisible.onAttach(this)

        val animation = onCreateCustomerAnimation()
        setFragmentAnimation(animation)

        defaultBackgroundColor = (activity as? SupportActivity)?.defaultBackground ?: Color.WHITE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeBackLayout?.getChildAt(0)?.setBackgroundColor(defaultBackgroundColor)
        getView()?.setBackgroundColor(defaultBackgroundColor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        supportFragmentVisible.onActivityCreated(savedInstanceState)
        view?.isClickable = true

        if (isSavedInstance) {
            resumeAnim()
        }
        if (arguments?.getBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST) == false && !isHidden) {
            savedInstanceState?.let { onLazyInit(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        arguments?.putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE, false)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var animation: Animation? = null
        when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> if (enter) {
                (activity as? SupportActivity)?.fragmentClickable = false
                if (arguments?.getBoolean(SupportTransaction.FRAGMENTATION_PLAY_ENTER_ANIM) == false) {
                    animation = AnimationUtils.loadAnimation(context, R.anim.anim_empty)
                } else {
                    val beforeOne = fragmentManager?.getBeforeOne(this)
                    animation = fragmentAnimation?.enterAnimId?.let { AnimationUtils.loadAnimation(context, it) }
                    beforeOne?.onCreatePopAnimations(false)
                }
                handler.postDelayed({
                    (activity as? SupportActivity)?.fragmentClickable = true
                    animEndListener?.invoke()
                    onEnterAnimEnd()
                }, animation?.duration ?: 0)
            }
            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> if (!enter) {
                val show = fragmentManager?.getLastActiveFragment()
                show?.onCreatePopAnimations(true)
                animation = fragmentAnimation?.exitAnimId?.let { AnimationUtils.loadAnimation(context, it) }
            }
        }
        return animation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentVisible.onSaveInstanceState(outState)
        arguments?.putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE, true)
        arguments?.putBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (arguments?.getBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST) == false && !hidden) {
            onLazyInit(null)
        }
    }

    override fun onDestroyView() {
        (activity as? SupportActivity)?.supportTransaction?.onResult(this)
        super.onDestroyView()
    }

    private fun onCreatePopAnimations(popEnter: Boolean) {
        if (arguments?.getBoolean(SupportTransaction.FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM) == true) {
            if (popEnter) {
                onSupportResume()
            } else {
                onSupportPause()
            }
            return
        }

        var animation: Animation? = null
        if (popEnter) {
            animation = fragmentAnimation?.popEnterAnimId?.let { AnimationUtils.loadAnimation(context, it) }
            handler.postDelayed({ onSupportResume() }, animation?.duration ?: 0)
        } else {
            animation = fragmentAnimation?.popExitAnimId?.let { AnimationUtils.loadAnimation(context, it) }
            handler.postDelayed({ onSupportPause() }, animation?.duration ?: 0)
        }

        view?.startAnimation(animation)
    }

    private fun resumeAnim() {
        val enterAnimId = arguments?.getInt(SupportTransaction.FRAGMENTATION_ENTER_ANIM_ID, R.anim.anim_empty)
                ?: R.anim.anim_empty
        val exitAnimId = arguments?.getInt(SupportTransaction.FRAGMENTATION_EXIT_ANIM_ID, R.anim.anim_empty)
                ?: R.anim.anim_empty
        val popEnterAnimId = arguments?.getInt(SupportTransaction.FRAGMENTATION_POP_ENTER_ANIM_ID, R.anim.anim_empty)
                ?: R.anim.anim_empty
        val popExitAnimId = arguments?.getInt(SupportTransaction.FRAGMENTATION_POP_EXIT_ANIM_ID, R.anim.anim_empty)
                ?: R.anim.anim_empty

        fragmentAnimation = FragmentAnimation(enterAnimId, exitAnimId, popEnterAnimId, popExitAnimId)
    }

    internal fun getFragmentAnimation(): FragmentAnimation? {
        return fragmentAnimation
    }

    internal fun setFragmentAnimation(animation: FragmentAnimation?) {
        this.fragmentAnimation = animation ?: NoneAnim()

        arguments?.apply {
            fragmentAnimation?.enterAnimId?.let { putInt(SupportTransaction.FRAGMENTATION_ENTER_ANIM_ID, it) }
            fragmentAnimation?.exitAnimId?.let { putInt(SupportTransaction.FRAGMENTATION_EXIT_ANIM_ID, it) }
            fragmentAnimation?.popEnterAnimId?.let { putInt(SupportTransaction.FRAGMENTATION_POP_ENTER_ANIM_ID, it) }
            fragmentAnimation?.popExitAnimId?.let { putInt(SupportTransaction.FRAGMENTATION_POP_EXIT_ANIM_ID, it) }
        }
    }

    /**
     * 在栈内查找Fragment对象，如果一个栈内存在多个同一个class的Fragment对象，则返回结果可能不准确
     *
     * @param cls
     * @param <T>
     * @return SupportFragment
    </T> */
    override fun <T : SupportFragment> findFragment(cls: Class<*>): T? {
        return fragmentManager?.findFragment<SupportFragment>(cls) as? T
    }

    /**
     * 在栈内查找Child Fragment对象，如果一个栈内存在多个同一个class的Fragment对象，则返回结果可能不准确
     *
     * @param cls
     * @param <T>
     * @return SupportFragment
    </T> */
    override fun <T : SupportFragment> findChildFragment(cls: Class<*>): T? {
        return childFragmentManager.findFragment<SupportFragment>(cls) as? T
    }

    /**
     * Back事件分发，不建议重写
     *
     * @return
     */
    override fun dispatcherOnBackPressed(): Boolean {
        if (childFragmentManager.fragments.size > 0) {
            val lastActive = childFragmentManager.getLastFragment()
            if (lastActive != null) {
                return lastActive.dispatcherOnBackPressed()
            }
        }
        return onBackPressed()
    }

    /**
     * 重写此方法可自定义Back事件
     *
     * @return true 消耗事件并不再向下传递
     */
    override fun onBackPressed(): Boolean {
        if (arguments?.getBoolean(SupportTransaction.FRAGMENTATION_BACK_STACK) == true) {
            pop()
            return true
        }
        return false
    }

    /**
     * 重写此方法可完成自定义Fragment动画
     * 重写返回null并不会将Fragment设置为空动画
     *
     * @return FragmentAnimation
     */
    override fun onCreateCustomerAnimation(): FragmentAnimation? {
        return null
    }

    /**
     * 懒加载
     *
     * @param savedInstanceState
     */
    override fun onLazyInit(savedInstanceState: Bundle?) {
        arguments?.putBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST, true)
    }

    /**
     * 入场动画结束时执行一次
     * 当Fragment从被回收的状态中恢复时并不会执行入场动画，所以也不会执行此方法
     */
    override fun onEnterAnimEnd() {

    }

    /**
     * 当Fragment变为不可见状态时（启动了一个新的Fragment被遮住时）执行一次
     * 执行hide操作时不会执行此方法
     */
    override fun onSupportPause() {

    }

    /**
     * 当Fragment从不可见状态中恢复时执行一次
     */
    override fun onSupportResume() {

    }

    /**
     * 当拖动返回时执行此方法，可重写
     *
     * @param beforeOne
     * @param state
     * @param scrollPercent
     */
    override fun onSwipeDrag(beforeOne: SupportFragment, state: Int, scrollPercent: Float) {
        beforeOne.view ?: return

        val startX = -(beforeOne.view!!.width * SwipeBackLayout.DEFAULT_SCROLL_THRESHOLD)
        beforeOne.view!!.x = (1.0f - scrollPercent) * startX
        if (state == SwipeBackLayout.STATE_IDLE && scrollPercent == 0f) {
            beforeOne.view!!.x = 0f
        }
    }

    /**
     * 接受返回数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onResult(requestCode: Int, resultCode: Int, data: Bundle?) {

    }

    /**
     * 接受全局消息
     *
     * @param code
     * @param data
     */
    override fun onPostedData(code: Int, data: Bundle) {

    }

    /**
     * 设置返回数据
     *
     * @param resultCode
     * @param data
     */
    override fun setResult(resultCode: Int, data: Bundle) {
        (activity as? SupportActivity)?.supportTransaction?.setResult(this, resultCode, data)
    }

    /**
     * 创建一个根部Fragment，装载位置是Child Fragment Manager,相当于加载一个根部的子集Fragment
     *
     * @param containerId    容器
     * @param to             要加载的对象
     * @param anim           绑定动画
     * @param playEnterAnim  是否展示入场动画
     * @param addToBackStack 是否加入back stack
     */
    override fun loadRootFragment(containerId: Int, to: SupportFragment, anim: FragmentAnimation, playEnterAnim: Boolean, addToBackStack: Boolean) {
        (activity as? SupportActivity)?.supportTransaction?.loadRootFragment(childFragmentManager, containerId, to, anim, playEnterAnim, addToBackStack)
    }

    override fun loadRootFragment(containerId: Int, to: SupportFragment) {
        (activity as? SupportActivity)?.supportTransaction?.loadRootFragment(childFragmentManager,
                containerId, to, (activity as? SupportActivity)?.defaultAnimation
                ?: NoneAnim(), false, true)
    }

    /**
     * 同时加载多个子Fragment
     *
     * @param containerId  容器
     * @param showPosition 显示第几个（从1开始）
     * @param fragments    要加载的Fragment
     */
    override fun loadMultipleRootFragments(containerId: Int, showPosition: Int, vararg fragments: SupportFragment) {
        (activity as? SupportActivity)?.supportTransaction?.loadMultipleRootFragments(childFragmentManager, containerId, showPosition, *fragments)
    }

    /**
     * 显示一个Fragment，并隐藏当前Child Fragment Manager 栈内的其它Fragment
     *
     * @param show
     */
    override fun showHideAllFragment(show: SupportFragment) {
        (activity as? SupportActivity)?.supportTransaction?.showHideAllFragment(childFragmentManager, show)
    }

    /**
     * 启动一个新的Fragment
     *
     * @param to
     * @param addToBackStack 是否加入Back stack
     */
    override fun start(to: SupportFragment, addToBackStack: Boolean) {
        (activity as? SupportActivity)?.supportTransaction?.start(this, to, addToBackStack)
    }

    /**
     * 启动一个新的Fragment，并接受返回数据
     *
     * @param to
     * @param requestCode
     */
    override fun startForResult(to: SupportFragment, requestCode: Int) {
        (activity as? SupportActivity)?.supportTransaction?.startForResult(this, to, requestCode)
    }

    /**
     * 启动一个新的Fragment并关闭当前Fragment
     *
     * @param to
     */
    override fun startWithPop(to: SupportFragment) {
        (activity as? SupportActivity)?.supportTransaction?.startWithPop(this, to)
    }

    /**
     * 启动一个新的Fragment，并弹出栈内的Fragment到指定位置
     *
     * @param to
     * @param cls
     * @param includeTarget 是否包含指定对象
     */
    override fun startWithPopTo(to: SupportFragment, cls: Class<*>, includeTarget: Boolean) {
        (activity as? SupportActivity)?.supportTransaction?.startWithPopTo(this, to, cls, includeTarget)
    }

    /**
     * 弹出栈内最后一个Fragment
     */
    override fun pop() {
        fragmentManager?.let { (activity as? SupportActivity)?.supportTransaction?.pop(it) }
    }

    /**
     * 弹出栈内的Fragment到指定位置
     *
     * @param cls
     * @param includeTarget 是否包含指定对象
     */
    override fun popTo(cls: Class<*>, includeTarget: Boolean) {
        fragmentManager?.let { (activity as? SupportActivity)?.supportTransaction?.popTo(it, cls, includeTarget) }
    }

    /**
     * 弹出Child Fragment Manager内的最后一个Fragment
     */
    override fun popChild() {
        (activity as? SupportActivity)?.supportTransaction?.pop(childFragmentManager)
    }

    /**
     * 弹出Child Fragment Manager的Fragment到指定位置
     *
     * @param cls
     * @param includeTarget 是否包含指定对象
     */
    override fun popChildTo(cls: Class<*>, includeTarget: Boolean) {
        (activity as? SupportActivity)?.supportTransaction?.popTo(childFragmentManager, cls, includeTarget)
    }

    /**
     * 弹出Child Fragment Manager所有的Fragment
     */
    override fun popAllChild() {
        (activity as? SupportActivity)?.supportTransaction?.popAll(childFragmentManager)
    }

    internal fun setAnimEndListener(listener: () -> Unit) {
        this.animEndListener = listener
    }

    /**
     * 关联SwipeBackLayout
     *
     * @param v
     * @return
     */
    fun attachSwipeBack(v: View): SwipeBackLayout {
        val current = this
        swipeBackLayout = SwipeBackLayout(context)
        swipeBackLayout?.setContentView(v)
        swipeBackLayout?.addSwipeListener(object : SwipeBackLayout.SwipeListenerEx {
            private var before: SupportFragment? = null

            override fun onContentViewSwipedBack() {
                before?.let { onSwipeDrag(it, SwipeBackLayout.STATE_IDLE, 0f) }

                (activity as? SupportActivity)?.apply {
                    supportTransaction?.remove(current, false)
                    fragmentSwipeDrag = false
                }
            }

            override fun onScrollStateChange(state: Int, scrollPercent: Float) {
                if (state == SwipeBackLayout.STATE_IDLE && scrollPercent == 0f) {
                    //Swipe back layout is Closed
                    before?.onSupportPause()
                }
                before?.let { onSwipeDrag(it, state, scrollPercent) }
            }

            override fun onEdgeTouch(edgeFlag: Int) {
                before = getBeforeOne(fragmentManager?.getActiveList(), current)
                before?.onSupportResume()

                (activity as? SupportActivity)?.fragmentSwipeDrag = true
            }

            override fun onScrollOverThreshold() {

            }
        })
        return swipeBackLayout!!
    }
}
