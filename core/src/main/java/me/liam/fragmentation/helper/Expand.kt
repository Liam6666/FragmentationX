package me.liam.fragmentation.helper

import androidx.fragment.app.Fragment
import java.util.ArrayList
import java.util.LinkedList
import java.util.NoSuchElementException

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import me.liam.fragmentation.support.SupportFragment

inline fun <T, R> T.trycatch(block: T.() -> R?): R? {
    return try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun FragmentTransaction.supportCommit(runnable: Runnable? = null): FragmentTransaction {
    runnable?.let { this.runOnCommit(it) }
    this.commitAllowingStateLoss()
    return this
}

fun FragmentManager.getInManagerFragments(): List<SupportFragment> {
    val list = ArrayList<SupportFragment>()
    for (f in this.fragments) {
        if (f is SupportFragment
                && !f.isRemoving()
                && !f.isDetached()) {
            list.add(f)
        }
    }
    return list
}

inline fun <reified T : SupportFragment> FragmentManager.findFragment(cls: Class<*>): T? {
    for (f in this.fragments) {
        if (f is SupportFragment && f.javaClass.name == cls.name) {
            return f as? T
        }
    }
    return null
}

fun FragmentManager.getAllFragments(list: MutableList<SupportFragment>) {
    for (f in this.fragments) {
        if (f is SupportFragment && !f.isRemoving() && !f.isDetached()) {
            list.add(f)
            if (f.getChildFragmentManager().fragments.size != 0) {
                f.getChildFragmentManager().getAllFragments(list)
            }
        }
    }
}

fun FragmentManager.getBeforeOne(who: SupportFragment): SupportFragment? {
    val list = ArrayList<SupportFragment>()
    for (f in this.fragments) {
        if (f is SupportFragment && !f.isRemoving() && !f.isDetached()) {
            list.add(f)
        }
    }
    val index = list.indexOf(who)
    if (index == -1) return null
    for (i in index - 1 downTo 0) {
        return list[i]
    }
    return null
}

fun FragmentManager.getActiveList(): List<SupportFragment> {
    val list = ArrayList<SupportFragment>()
    for (f in this.fragments) {
        if (f is SupportFragment
                && f.isAdded()
                && f.isVisible()
                && !f.isRemoving()
                && !f.isDetached()
                && f.isResumed()) {
            list.add(f)
        }
    }
    return list
}

fun FragmentManager.getLastActiveFragment(): SupportFragment? {
    val linkedList = LinkedList<SupportFragment>()
    linkedList.addAll(getActiveList())
    return try {
        linkedList.last
    } catch (e: NoSuchElementException) {
        null
    }
}

fun FragmentManager.getLastFragment(): SupportFragment? {
    val linkedList = LinkedList<SupportFragment>()
    linkedList.addAll(getInManagerFragments())
    return try {
        linkedList.last
    } catch (e: NoSuchElementException) {
        null
    }
}

fun Fragment.getBeforeOne(list: List<SupportFragment>?, who: SupportFragment?): SupportFragment? {
    if (list == null || list.isEmpty() || who == null) return null
    val index = list.indexOf(who)
    if (index == -1) return null
    for (i in index - 1 downTo 0) {
        return list[i]
    }
    return null
}
