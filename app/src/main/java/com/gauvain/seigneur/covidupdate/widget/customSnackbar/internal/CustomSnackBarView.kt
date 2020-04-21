package com.gauvain.seigneur.covidupdate.widget.customSnackbar.internal

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.gauvain.seigneur.covidupdate.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.ContentViewCallback
import kotlinx.android.synthetic.main.view_content_snackbar.view.*

/**
 * SnackBarView which is an extend of MaterialCardView
 */
internal class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), ContentViewCallback {

    val view: View = View.inflate(context, R.layout.view_custom_snackbar, this)
    private val constraintLayout: ConstraintLayout = view.mContentLayout
    val actionButton: MaterialButton = view.snackBarBtn
    val messageView: TextView = view.snackBarMessage

    init {
        this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondary))
    }

    override fun animateContentIn(delay: Int, duration: Int) {
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}