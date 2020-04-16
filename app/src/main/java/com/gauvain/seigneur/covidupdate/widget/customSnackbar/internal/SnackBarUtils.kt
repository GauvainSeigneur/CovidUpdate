package com.gauvain.seigneur.covidupdate.widget.customSnackbar.internal

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gauvain.seigneur.covidupdate.R

object SnackBarUtils {

    private const val maxEndButtonCharacters = 8

    /**
     * check if we have to show short or large button
     */
    fun manageButtonMode(
        context: Context,
        constraintLayout: ConstraintLayout,
        messageView: TextView,
        inText: CharSequence,
        maxCharacters: Int? = null
    ) {
        if (maxCharacters != null &&
            maxCharacters != -1 &&
            inText.length >= maxCharacters
        ) {
            moveActionBelowMessage(
                context,
                constraintLayout,
                messageView
            )
        } else if (inText.length >= maxEndButtonCharacters) {
            //in any case, if the text of the button contains at least 20 characters
            // show it instead if the short  one
            moveActionBelowMessage(
                context,
                constraintLayout,
                messageView
            )
        }
    }

    private fun moveActionBelowMessage(
        context: Context,
        constraintLayout: ConstraintLayout,
        messageView: TextView
    ) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.snackBarMessage,
            ConstraintSet.BOTTOM,
            R.id.snackBarBtn,
            ConstraintSet.TOP,
            context.resources.getDimensionPixelSize(R.dimen.space_s)
        )
        constraintSet.connect(
            R.id.snackBarMessage,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            R.id.snackBarBtn,
            ConstraintSet.TOP,
            R.id.snackBarMessage,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.connect(
            R.id.snackBarBtn,
            ConstraintSet.BOTTOM,
            R.id.mContentLayout,
            ConstraintSet.BOTTOM,
            context.resources.getDimensionPixelSize(R.dimen.space_s)
        )
        constraintSet.connect(
            R.id.snackBarBtn,
            ConstraintSet.START,
            R.id.snackBarMessage,
            ConstraintSet.START,
            0
        )
        constraintSet.applyTo(constraintLayout)
        adaptTextPaddingForBottomButton(
            context,
            messageView
        )
    }

    /**
     *  manage text padding according the line count.
     *  if the lineCount is > 1 the message padding top and bottom must be increased
     */
    fun adaptTextPaddingForMultilines(context: Context, messageView: TextView) {
        messageView.viewTreeObserver.addOnPreDrawListener {
            if (messageView.lineCount > 1) {
                messageView.setPadding(
                    messageView.paddingStart,
                    context.resources.getDimensionPixelSize(R.dimen.space_l),
                    messageView.paddingEnd,
                    context.resources.getDimensionPixelSize(R.dimen.space_l)
                )
            }
            true
        }
    }

    /**
     *  manage text padding according if a bottom button is visible
     */
    fun adaptTextPaddingForBottomButton(context: Context, messageView: TextView) {
        messageView.viewTreeObserver?.addOnPreDrawListener {
            messageView.setPadding(
                messageView.paddingStart,
                context.resources.getDimensionPixelSize(R.dimen.space_l),
                messageView.paddingEnd,
                0
            )
            true
        }
    }
}

/**
 * Extension function in order to be able to it
 */
fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)
    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}


