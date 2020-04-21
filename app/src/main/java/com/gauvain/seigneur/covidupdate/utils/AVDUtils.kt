package com.gauvain.seigneur.covidupdate.utils

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

object AVDUtils {

    fun startLoadingAnimation(imageView: ImageView, start: Boolean) {
        val loaderAvd = imageView.drawable as AnimatedVectorDrawable
        if (start) {
            AnimatedVectorDrawableCompat.registerAnimationCallback(
                loaderAvd,
                object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        imageView.post {
                            imageView.startVectorAnimation()
                        }
                    }
                })
            imageView.startVectorAnimation()
        } else {
            imageView.stopVectorAnimation()
        }
    }
}

fun ImageView.startVectorAnimation() {
    val avd = this.drawable as? AnimatedVectorDrawable
    avd?.start()
}

fun ImageView.stopVectorAnimation() {
    val avd = this.drawable as? AnimatedVectorDrawable
    avd?.stop()
}