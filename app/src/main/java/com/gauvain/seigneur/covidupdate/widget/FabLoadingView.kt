package com.gauvain.seigneur.covidupdate.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.AVDUtils
import kotlinx.android.synthetic.main.view_fab_loading.view.*

class FabLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val SCALE_X_PROPERTY = "scaleX"
        const val SCALE_Y_PROPERTY = "scaleY"
        const val ROTATION_PROPERTY = "rotation"
        const val SHRINK_EXPAND_ANIM_DURATION = 300L
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_fab_loading, this)
    }

    fun showLoader(isVisible: Boolean) {
        //shrinkExpandRefresh(isVisible)
        displayLoaderAnimation(isVisible)
    }

    private fun shrinkExpandRefresh(isExpand: Boolean) {
        val scaleValue: Float
        val rotationValue: List<Float>
        if (isExpand) {
            scaleValue = 1f
            rotationValue = listOf(360f, 0f)
        } else {
            scaleValue = 0f
            rotationValue = listOf(0f, 360f)
        }
        val scaleX = ObjectAnimator.ofFloat(loadingAvdView, SCALE_X_PROPERTY, scaleValue)
        val scaleY = ObjectAnimator.ofFloat(loadingAvdView, SCALE_Y_PROPERTY, scaleValue)
        val rotate = ObjectAnimator.ofFloat(
            loadingAvdView, ROTATION_PROPERTY, rotationValue[0], rotationValue[1]
        )
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, rotate)
        animatorSet.duration = SHRINK_EXPAND_ANIM_DURATION
        animatorSet.start()
    }

    private fun displayLoaderAnimation(isVisible: Boolean) {
        showLoadingAVd(isVisible)
        startLoadingAVd(isVisible)
    }

    private fun showLoadingAVd(isVisible: Boolean) {
        if (isVisible) {
            loadingAvdView.visibility = View.VISIBLE
        } else {
            loadingAvdView.visibility = View.GONE
        }
    }

    private fun startLoadingAVd(start: Boolean) {
        AVDUtils.startLoadingAnimation(loadingAvdView, start)
    }
}