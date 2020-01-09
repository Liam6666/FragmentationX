package me.liam.fragmentation.queue

abstract class Action {

    abstract fun run(): Long

    open fun actionType(): Int {
        return TYPE_LOAD
    }

    companion object {
        const val TYPE_LOAD = 1
        const val TYPE_POP = 2
    }
}
