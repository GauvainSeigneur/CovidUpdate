package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.AVDUtils
import com.gauvain.seigneur.covidupdate.utils.safeClick.setOnSafeClickListener
import com.gauvain.seigneur.covidupdate.utils.startVectorAnimation
import kotlinx.android.synthetic.main.view_initial_loading.view.*
import kotlinx.android.synthetic.main.view_no_data_found.view.*

class InitialLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_initial_loading, this)
        //this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground))
    }

    fun hide() {
        showError(false)
        showLoader(false)
    }

    fun setLoading() {
        this.visibility = View.VISIBLE
        showError(false)
        showLoader(true)
    }

    fun setError(title: String?, desc: String?, buttonText: String?, retryListener: () -> Unit) {
        this.visibility = View.VISIBLE
        setErrorInfo(title, desc)
        showError(true)
        showLoader(false)
        retryButton.text = buttonText ?: context.getString(R.string.retry)
        retryButton.setOnSafeClickListener { retryListener() }
    }

    private fun showLoader(isVisible: Boolean) {
        if (isVisible) {
            bigLoader.visibility = View.VISIBLE
            AVDUtils.startLoadingAnimation(bigLoader, true)
        } else {
            AVDUtils.startLoadingAnimation(bigLoader, false)
            bigLoader.visibility = View.GONE
        }
    }

    private fun showError(isVisible: Boolean) {
        if (isVisible) {
            errorView.visibility = View.VISIBLE
            binocularAvdView.startVectorAnimation()
        } else {
            errorView.visibility = View.GONE
        }
    }

    private fun setErrorInfo(title: String?, desc: String?) {
        errorTitle.text = title
        errorDesc.text = desc
    }
}