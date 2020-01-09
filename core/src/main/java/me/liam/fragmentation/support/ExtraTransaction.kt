package me.liam.fragmentation.support

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.annotation.IdRes

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import me.liam.fragmentation.anim.FragmentAnimation
import me.liam.fragmentation.R
import me.liam.fragmentation.helper.findFragment
import me.liam.fragmentation.helper.getLastFragment
import me.liam.fragmentation.helper.supportCommit
import me.liam.fragmentation.queue.Action
import me.liam.fragmentation.queue.ActionQueue

abstract class ExtraTransaction {

    abstract fun setCustomerAnimations(enterAnim: Int, exitAnim: Int, popEnterAnim: Int? = null, popExitAnim: Int? = null): ExtraTransaction

    abstract fun dontDisplaySelfPopAnim(dontDisplaySelfPopAnim: Boolean): ExtraTransaction

    abstract fun setResult(resultCode: Int, data: Bundle): ExtraTransaction

    abstract fun displayEnterAnim(displayEnterAnim: Boolean): ExtraTransaction

    abstract fun setTag(tag: String): ExtraTransaction

    abstract fun addBackStack(addBackStack: Boolean): ExtraTransaction

    abstract fun runOnExecute(run: Runnable): ExtraTransaction

    abstract fun loadRootFragment(@IdRes containerId: Int, to: SupportFragment)

    abstract fun show(vararg show: SupportFragment)

    abstract fun hide(vararg hide: SupportFragment)

    abstract fun start(to: SupportFragment)

    abstract fun startWithPop(to: SupportFragment)

    abstract fun startWithPopTo(to: SupportFragment, popToCls: Class<*>, includeTarget: Boolean = true)

    abstract fun startForResult(to: SupportFragment, requestCode: Int)

    abstract fun pop()

    abstract fun popChild()

    abstract fun popTo(popToCls: Class<*>, includeTarget: Boolean = true)

    abstract fun popChildTo(popToCls: Class<*>, includeTarget: Boolean = true)

    abstract fun remove(remove: SupportFragment, anim: Boolean = true)

    abstract fun remove(vararg remove: SupportFragment)

    internal class ExtraTransactionImpl(private val from: SupportFragment, handler: Handler) : ExtraTransaction() {

        private val record = TransactionRecord()
        private val actionQueue = ActionQueue(handler)

        override fun setCustomerAnimations(enterAnim: Int, exitAnim: Int, popEnterAnim: Int?, popExitAnim: Int?): ExtraTransaction {
            record.fragmentAnimation = FragmentAnimation(enterAnim, exitAnim, popEnterAnim
                    ?: R.anim.anim_empty, popExitAnim ?: R.anim.anim_empty)
            return this
        }

        override fun dontDisplaySelfPopAnim(dontDisplaySelfPopAnim: Boolean): ExtraTransaction {
            record.dontDisplaySelfPopAnim = dontDisplaySelfPopAnim
            return this
        }

        override fun setResult(resultCode: Int, data: Bundle): ExtraTransaction {
            record.resultCode = resultCode
            record.resultData = data
            setResult(from, resultCode, data)
            return this
        }

        override fun displayEnterAnim(displayEnterAnim: Boolean): ExtraTransaction {
            record.displayEnterAnim = displayEnterAnim
            return this
        }

        override fun setTag(tag: String): ExtraTransaction {
            record.tag = tag
            return this
        }

        override fun addBackStack(addBackStack: Boolean): ExtraTransaction {
            record.addBackStack = addBackStack
            return this
        }


        override fun runOnExecute(run: Runnable): ExtraTransaction {
            record.runOnExecute = run
            return this
        }

        override fun loadRootFragment(containerId: Int, to: SupportFragment) {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    bindFragmentOptions(to, containerId, record)
                    to.fragmentAnimation = record.fragmentAnimation

                    from.childFragmentManager.beginTransaction().apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        add(containerId, to, record.tag)
                    }.supportCommit(record.runOnExecute)
                    return 0
                }
            })
        }

        override fun show(vararg show: SupportFragment) {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    from.fragmentManager?.beginTransaction()?.apply {
                        for (f in show) show(f)
                    }?.supportCommit(record.runOnExecute)
                    return 0
                }
            })
        }

        override fun hide(vararg hide: SupportFragment) {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    from.fragmentManager?.beginTransaction()?.apply {
                        for (f in hide) hide(f)
                    }?.supportCommit(record.runOnExecute)
                    return 0
                }
            })
        }

        override fun start(to: SupportFragment) {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    bindFragmentOptions(to, from.containerId, record)
                    to.fragmentAnimation = record.fragmentAnimation

                    from.fragmentManager?.beginTransaction()?.apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        add(from.containerId, to, record.tag)
                    }?.supportCommit(record.runOnExecute)
                    return 0
                }
            })
        }

        override fun startWithPop(to: SupportFragment) {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    bindFragmentOptions(to, from.containerId, record)
                    to.fragmentAnimation = record.fragmentAnimation

                    from.fragmentManager?.beginTransaction()?.apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        add(from.containerId, to, record.tag)
                    }?.supportCommit(record.runOnExecute)

                    to.setAnimEndListener {
                        from.fragmentManager?.beginTransaction()?.apply {
                            remove(from);supportCommit()
                        }
                    }
                    return 0
                }
            })
        }

        override fun startWithPopTo(to: SupportFragment, popToCls: Class<*>, includeTarget: Boolean) {
            record.popToCls = popToCls
            record.includeTarget = includeTarget
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    bindFragmentOptions(to, from.containerId, record)
                    to.fragmentAnimation = record.fragmentAnimation

                    from.fragmentManager?.beginTransaction()?.apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        add(from.containerId, to, record.tag)
                    }?.supportCommit(record.runOnExecute)

                    to.setAnimEndListener {
                        popTo(from.fragmentManager, record.popToCls, record.includeTarget, null)
                    }
                    return 0
                }
            })
        }

        override fun startForResult(to: SupportFragment, requestCode: Int) {
            record.requestCode = requestCode
            setStartForResult(from, to, requestCode)
            start(to)
        }

        override fun pop() {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    val remove = from.fragmentManager?.getLastFragment() ?: return 0
                    val duration = remove.fragmentAnimation?.exitAnimId?.let { AnimationUtils.loadAnimation(remove.context, it).duration }

                    from.fragmentManager?.beginTransaction()?.apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        remove(remove)
                    }?.supportCommit(record.runOnExecute)
                    return duration ?: 0
                }

                override fun actionType(): Int {
                    return Action.TYPE_POP
                }
            })
        }

        override fun popChild() {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    val remove = from.childFragmentManager.getLastFragment()
                            ?: return 0
                    val duration = remove.fragmentAnimation?.exitAnimId?.let { AnimationUtils.loadAnimation(remove.context, it).duration }

                    from.childFragmentManager.beginTransaction().apply {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        remove(remove)
                    }.supportCommit(record.runOnExecute)
                    return duration ?: 0
                }

                override fun actionType(): Int {
                    return Action.TYPE_POP
                }
            })
        }

        override fun popTo(popToCls: Class<*>, includeTarget: Boolean) {
            record.popToCls = popToCls
            record.includeTarget = includeTarget

            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    popTo(from.fragmentManager, record.popToCls, record.includeTarget, record.runOnExecute)
                    return 0
                }

                override fun actionType(): Int {
                    return Action.TYPE_POP
                }
            })
        }

        override fun popChildTo(popToCls: Class<*>, includeTarget: Boolean) {
            record.popToCls = popToCls
            record.includeTarget = includeTarget

            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    popTo(from.childFragmentManager, record.popToCls, record.includeTarget, record.runOnExecute)
                    return 0
                }

                override fun actionType(): Int {
                    return Action.TYPE_POP
                }
            })
        }

        override fun remove(remove: SupportFragment, anim: Boolean) {
            record.remove = remove
            record.removeAnim = anim

            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    from.fragmentManager?.beginTransaction()?.apply {
                        if (anim) setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        remove(remove)
                    }?.supportCommit(record.runOnExecute)
                    return 0
                }

                override fun actionType(): Int {
                    return Action.TYPE_POP
                }
            })
        }

        override fun remove(vararg remove: SupportFragment) {
            actionQueue.enqueue(object : Action() {
                override fun run(): Long {
                    from.fragmentManager?.beginTransaction()?.apply {
                        for (f in remove) remove(f)
                    }?.supportCommit(record.runOnExecute)
                    return 0
                }

                override fun actionType(): Int {
                    return Action.TYPE_POP
                }
            })
        }

        private fun getArguments(target: SupportFragment?): Bundle {
            return target?.arguments ?: Bundle().also { target?.arguments = it }
        }

        private fun bindFragmentOptions(target: SupportFragment, containerId: Int, record: TransactionRecord) {
            val args = getArguments(target)
            args.putInt(SupportTransaction.FRAGMENTATION_CONTAINER_ID, containerId)
            args.putString(SupportTransaction.FRAGMENTATION_TAG, record.tag)
            args.putString(SupportTransaction.FRAGMENTATION_SIMPLE_NAME, target.javaClass.simpleName)
            args.putString(SupportTransaction.FRAGMENTATION_FULL_NAME, target.javaClass.name)
            args.putBoolean(SupportTransaction.FRAGMENTATION_PLAY_ENTER_ANIM, record.displayEnterAnim)
            args.putBoolean(SupportTransaction.FRAGMENTATION_INIT_LIST, false)
            args.putBoolean(SupportTransaction.FRAGMENTATION_SAVED_INSTANCE, false)
            args.putBoolean(SupportTransaction.FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM, record.dontDisplaySelfPopAnim)
        }

        fun setStartForResult(from: SupportFragment?, to: SupportFragment, requestCode: Int) {
            getArguments(from).apply { putInt(SupportTransaction.FRAGMENTATION_REQUEST_CODE, requestCode) }
            getArguments(to).apply { putInt(SupportTransaction.FRAGMENTATION_FROM_REQUEST_CODE, requestCode) }
        }

        fun setResult(target: SupportFragment?, resultCode: Int, data: Bundle) {
            val bundle = getArguments(target)
            bundle.putInt(SupportTransaction.FRAGMENTATION_RESULT_CODE, resultCode)
            bundle.putBundle(SupportTransaction.FRAGMENTATION_RESULT_DATA, data)
        }

        private fun popTo(fm: FragmentManager?, cls: Class<*>?, includeTarget: Boolean, run: Runnable?) {
            fm ?: return
            val remove = fm.getLastFragment() ?: return
            val target = cls?.let { fm.findFragment<SupportFragment>(cls) } ?: return

            val targetIndex = fm.fragments.indexOf(target)
            val removeIndex = fm.fragments.indexOf(remove)

            val ft = fm.beginTransaction()
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            val removeList = fm.fragments.subList(targetIndex, removeIndex)
            if (!includeTarget) {
                removeList.remove(target)
            }
            removeList.filter { it is SupportFragment }.forEach { ft.remove(it) }
            ft.remove(remove)
            ft.supportCommit(run)
        }
    }
}
