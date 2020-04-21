package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.util.AttributeSet
import com.gauvain.seigneur.covidupdate.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RefreshFab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val STATE_EXPANDED = intArrayOf(
        -R.attr.state_shrink
    )
    private val STATE_SHRUNK = intArrayOf(
        R.attr.state_shrink
    )

    fun shrink(isShrunk: Boolean) {
        if (isShrunk) {
            this.setImageState(STATE_SHRUNK, true)
        } else {
            this.setImageState(STATE_EXPANDED, true)
        }
    }
}