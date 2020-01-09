package me.liam.fragmentation.queue

import android.os.Handler
import android.os.Looper

import java.util.LinkedList

class ActionQueue(private val handler: Handler) {

    private val actionQueue = LinkedList<Action>()

    fun enqueue(action: Action) {
        if (action.actionType() == Action.TYPE_LOAD) {
            if (Thread.currentThread() === Looper.getMainLooper().thread) {
                action.run()
            } else {
                handler.post { action.run() }
            }
            return
        }
        if (!actionQueue.isEmpty()) return
        actionQueue.add(action)
        handlerAction()
    }


    private fun handlerAction() {
        if (actionQueue.size == 1) {
            handler.post {
                val a = actionQueue.peek()
                if (a != null) {
                    val duration = a.run()
                    handler.postDelayed({ actionQueue.clear() }, duration)
                } else {
                    actionQueue.clear()
                }
            }
        }
    }
}
