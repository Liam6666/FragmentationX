package me.liam.fragmentation.support

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import java.util.UUID

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import me.liam.fragmentation.anim.FragmentAnimation
import me.liam.fragmentation.helper.*
import me.liam.fragmentation.queue.Action
import me.liam.fragmentation.queue.ActionQueue

class SupportTransaction internal constructor(private val iSupportActivity: ISupportActivity) {

    private val actionQueue = ActionQueue(Handler(Looper.getMainLooper()))

    private fun getArguments(target: SupportFragment): Bundle {
        return target.arguments ?: Bundle().also { target.arguments = it }
    }

    private fun bindFragmentOptions(target: SupportFragment, containerId: Int, playEnterAnim: Boolean, addToBackStack: Boolean) {
        val args = getArguments(target)
        args.putInt(FRAGMENTATION_CONTAINER_ID, containerId)
        args.putString(FRAGMENTATION_TAG, UUID.randomUUID().toString())
        args.putString(FRAGMENTATION_SIMPLE_NAME, target.javaClass.simpleName)
        args.putString(FRAGMENTATION_FULL_NAME, target.javaClass.name)
        args.putBoolean(FRAGMENTATION_PLAY_ENTER_ANIM, playEnterAnim)
        args.putBoolean(FRAGMENTATION_INIT_LIST, false)
        args.putBoolean(FRAGMENTATION_SAVED_INSTANCE, false)
        args.putBoolean(FRAGMENTATION_BACK_STACK, addToBackStack)
    }

    internal fun loadRootFragment(fm: FragmentManager, containerId: Int, to: SupportFragment, anim: FragmentAnimation, playEnterAnim: Boolean, addToBackStack: Boolean) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                bindFragmentOptions(to, containerId, playEnterAnim, addToBackStack)
                to.fragmentAnimation = anim

                fm.beginTransaction().apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    add(containerId, to)
                }.supportCommit()
                return 0
            }
        })
    }

    internal fun loadMultipleRootFragments(fm: FragmentManager, containerId: Int, showPosition: Int, vararg fragments: SupportFragment) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                var position = 1
                val ft = fm.beginTransaction()
                for (to in fragments) {
                    bindFragmentOptions(to, containerId, false, false)
                    to.fragmentAnimation = null

                    ft.add(containerId, to)
                    if (position == showPosition) {
                        ft.show(to)
                    } else {
                        ft.hide(to)
                    }
                    position++
                }
                ft.supportCommit()
                return 0
            }
        })
    }

    internal fun showHideAllFragment(fm: FragmentManager, show: SupportFragment) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                val ft = fm.beginTransaction()
                for (f in fm.getInManagerFragments()) {
                    if (f === show) {
                        ft.show(f)
                    } else {
                        ft.hide(f)
                    }
                }
                ft.supportCommit()
                return 0
            }
        })
    }

    internal fun start(from: SupportFragment?, to: SupportFragment, addToBackStack: Boolean) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                from ?: return 0

                bindFragmentOptions(to, from.containerId, true, addToBackStack)
                to.fragmentAnimation = iSupportActivity.defaultAnimation

                from.fragmentManager?.beginTransaction()?.apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    add(to.containerId, to)
                }?.supportCommit()
                return 0
            }
        })
    }


    internal fun pop(fm: FragmentManager) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                val remove = fm.getLastFragment() ?: return 0

                val duration = remove.fragmentAnimation?.exitAnimId?.let { AnimationUtils.loadAnimation(remove.context, it).duration }
                fm.beginTransaction().apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    remove(remove)
                }.supportCommit()
                return duration ?: 0
            }

            override fun actionType(): Int {
                return Action.TYPE_POP
            }
        })
    }

    internal fun startWithPop(from: SupportFragment?, to: SupportFragment) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                from ?: return 0

                bindFragmentOptions(to, from.containerId, true, true)
                to.fragmentAnimation = iSupportActivity.defaultAnimation

                from.fragmentManager?.beginTransaction()?.apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    add(to.containerId, to)
                }?.supportCommit()

                to.setAnimEndListener {
                    silenceRemove(from.fragmentManager, from)
                }
                return 0
            }
        })
    }

    internal fun silenceRemove(fm: FragmentManager?, vararg removes: SupportFragment) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                fm?.beginTransaction()?.apply {
                    for (f in removes) remove(f)
                }?.supportCommit()
                return 0
            }
        })
    }

    internal fun popTo(fm: FragmentManager, cls: Class<*>, includeTarget: Boolean) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                val remove = fm.getLastFragment()
                val target: SupportFragment? = fm.findFragment(cls)
                if (remove == null || target == null) return 0

                val targetIndex = fm.fragments.indexOf(target)
                val removeIndex = fm.fragments.indexOf(remove)

                val removeList = fm.fragments.subList(targetIndex, removeIndex)
                val ft = fm.beginTransaction()
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                if (!includeTarget) {
                    removeList.remove(target)
                }
                removeList.filter { it is SupportFragment }.forEach { ft.remove(it) }
                ft.remove(remove)
                ft.supportCommit()
                return 0
            }

            override fun actionType(): Int {
                return Action.TYPE_POP
            }
        })
    }

    internal fun startForResult(from: SupportFragment, to: SupportFragment, requestCode: Int) {
        getArguments(from).apply { putInt(FRAGMENTATION_REQUEST_CODE, requestCode) }
        getArguments(to).apply { putInt(FRAGMENTATION_FROM_REQUEST_CODE, requestCode) }
        start(from, to, true)
    }

    internal fun setResult(target: SupportFragment, resultCode: Int, data: Bundle) {
        val bundle = getArguments(target)
        bundle.putInt(FRAGMENTATION_RESULT_CODE, resultCode)
        bundle.putBundle(FRAGMENTATION_RESULT_DATA, data)
    }

    internal fun onResult(target: SupportFragment?) {
        target ?: return

        val bundle = getArguments(target)
        val fromRequestCode = bundle.getInt(FRAGMENTATION_FROM_REQUEST_CODE, -1)
        val resultCode = bundle.getInt(FRAGMENTATION_RESULT_CODE, -1)
        if (resultCode == -1 || fromRequestCode == -1) return

        val data = bundle.getBundle(FRAGMENTATION_RESULT_DATA)

        val list = target.fragmentManager?.getActiveList()
        list?.filter {
            getArguments(it).getInt(FRAGMENTATION_REQUEST_CODE) == fromRequestCode
        }?.forEach { it.onResult(fromRequestCode, resultCode, data) }
    }

    internal fun popAll(fm: FragmentManager) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                fm.beginTransaction().apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    fm.fragments.filter {
                        it is SupportFragment && !it.isRemoving() && !it.isDetached()
                    }.forEach { remove(it) }
                }.supportCommit()
                return 0
            }

            override fun actionType(): Int {
                return Action.TYPE_POP
            }
        })
    }

    internal fun remove(remove: SupportFragment?, anim: Boolean) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                remove?.fragmentManager?.beginTransaction()?.apply {
                    if (anim) setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    remove(remove)
                }?.supportCommit()
                return 0
            }

            override fun actionType(): Int {
                return Action.TYPE_POP
            }
        })
    }

    internal fun startWithPopTo(from: SupportFragment?, to: SupportFragment, cls: Class<*>, includeTarget: Boolean) {
        actionQueue.enqueue(object : Action() {
            override fun run(): Long {
                from ?: return 0

                bindFragmentOptions(to, from.containerId, true, true)
                to.fragmentAnimation = iSupportActivity.defaultAnimation

                from.fragmentManager?.beginTransaction()?.apply {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    add(to.containerId, to)
                }?.supportCommit()

                to.setAnimEndListener {
                    from.fragmentManager?.let { popTo(it, cls, includeTarget) }
                }
                return 0
            }

            override fun actionType(): Int {
                return Action.TYPE_POP
            }
        })
    }

    companion object {

        const val FRAGMENTATION_CONTAINER_ID = "Fragmentation:ContainerId"
        const val FRAGMENTATION_TAG = "Fragmentation:Tag"
        const val FRAGMENTATION_SIMPLE_NAME = "Fragmentation:SimpleName"
        const val FRAGMENTATION_FULL_NAME = "Fragmentation:FullName"
        const val FRAGMENTATION_PLAY_ENTER_ANIM = "Fragmentation:PlayEnterAnim"
        const val FRAGMENTATION_INIT_LIST = "Fragmentation:InitList"
        const val FRAGMENTATION_BACK_STACK = "Fragmentation:AddToBackStack"

        const val FRAGMENTATION_ENTER_ANIM_ID = "Fragmentation:EnterAnimId"
        const val FRAGMENTATION_EXIT_ANIM_ID = "Fragmentation:ExitAnimId"
        const val FRAGMENTATION_POP_ENTER_ANIM_ID = "Fragmentation:PopEnterAnimId"
        const val FRAGMENTATION_POP_EXIT_ANIM_ID = "Fragmentation:PopExitAnimId"

        const val FRAGMENTATION_DONT_DISPLAY_SELF_POP_ANIM = "Fragmentation:DontDisplaySelfPopAnim"

        const val FRAGMENTATION_SAVED_INSTANCE = "Fragmentation:SavedInstance"

        const val FRAGMENTATION_REQUEST_CODE = "Fragmentation:RequestCode"
        const val FRAGMENTATION_FROM_REQUEST_CODE = "Fragmentation:FromRequestCode"
        const val FRAGMENTATION_RESULT_CODE = "Fragmentation:ResultCode"
        const val FRAGMENTATION_RESULT_DATA = "Fragmentation:ResultData"
    }
}
