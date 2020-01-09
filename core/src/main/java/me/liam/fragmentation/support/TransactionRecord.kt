package me.liam.fragmentation.support

import android.os.Bundle

import java.util.UUID

import me.liam.fragmentation.anim.FragmentAnimation
import me.liam.fragmentation.R

internal class TransactionRecord {

    var fragmentAnimation = FragmentAnimation(R.anim.anim_empty, R.anim.anim_empty, R.anim.anim_empty, R.anim.anim_empty)

    var requestCode = -1

    var resultCode = -1

    var resultData: Bundle? = null

    var displayEnterAnim = true

    var tag = UUID.randomUUID().toString()

    var addBackStack = true

    var runOnExecute: Runnable? = null

    var popToCls: Class<*>? = null

    var includeTarget = true

    var remove: SupportFragment? = null

    var removeAnim = true

    var dontDisplaySelfPopAnim = false
}
