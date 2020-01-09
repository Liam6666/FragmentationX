package me.liam.fragmentation.anim

import me.liam.fragmentation.R

open class FragmentAnimation @JvmOverloads constructor(enterAnimId: Int, exitAnimId: Int, popEnterAnimId: Int = R.anim.anim_empty, popExitAnimId: Int = R.anim.anim_empty) {

    var enterAnimId: Int = 0
        internal set
    var exitAnimId: Int = 0
        internal set
    var popEnterAnimId: Int = 0
        internal set
    var popExitAnimId: Int = 0
        internal set

    init {
        this.enterAnimId = enterAnimId
        this.exitAnimId = exitAnimId
        this.popEnterAnimId = popEnterAnimId
        this.popExitAnimId = popExitAnimId
    }

}
