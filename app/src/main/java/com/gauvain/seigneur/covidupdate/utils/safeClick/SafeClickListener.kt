package com.gauvain.seigneur.covidupdate.utils.safeClick

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private val interval: Int? = null,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {

    private var defaultInterval = 500
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        interval?.let {
            defaultInterval = it
        }
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}