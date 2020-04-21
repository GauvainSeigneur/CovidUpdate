package com.gauvain.seigneur.covidupdate.widget.customSnackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import android.text.TextUtils
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.safeClick.setOnSafeClickListener
import com.gauvain.seigneur.covidupdate.widget.customSnackbar.internal.CustomSnackbarView
import com.gauvain.seigneur.covidupdate.widget.customSnackbar.internal.SnackBarUtils
import com.gauvain.seigneur.covidupdate.widget.customSnackbar.internal.findSuitableParent
import kotlinx.android.synthetic.main.view_content_snackbar.view.*

/**
 * Custom SnackBar which have the same behavior as SnackBar
 * but background color and text style can be changed in a easier way
 */
class CustomSnackbar
private constructor(
    parent: ViewGroup,
    inContent: CustomSnackbarView,
    buttonEndMaxCharacters: Int?
) : BaseTransientBottomBar<CustomSnackbar>(parent, inContent, inContent) {

    private var endButtonMaxCharacters: Int? = buttonEndMaxCharacters
    private var customSnackbarView: CustomSnackbarView = inContent

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    @IntDef(LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG)
    @IntRange(from = 1)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Duration

    /**
     * Set the action to be displayed in this [BaseTransientBottomBar].
     */
    fun setAction(
        @StringRes
        inResId: Int, inListener: View.OnClickListener
    ): CustomSnackbar {
        return setAction(context.getText(inResId), inListener)
    }

    /**
     * Set the action to be displayed in this [BaseTransientBottomBar].
     */
    fun setAction(inText: CharSequence, inListener: View.OnClickListener?): CustomSnackbar {
        if (TextUtils.isEmpty(inText)) {
            customSnackbarView.actionButton.visibility = View.GONE
            customSnackbarView.actionButton.setOnClickListener(null)
        } else {
            SnackBarUtils.manageButtonMode(
                customSnackbarView.context,
                customSnackbarView.mContentLayout,
                customSnackbarView.messageView,
                inText,
                endButtonMaxCharacters
            )
            customSnackbarView.actionButton.visibility = View.VISIBLE
            customSnackbarView.actionButton.text = inText
            customSnackbarView.actionButton.setOnSafeClickListener { view ->
                inListener?.onClick(view)
                // Now dismiss the Snackbar
                dispatchDismiss(BaseCallback.DISMISS_EVENT_ACTION)
            }
        }
        return this
    }

    /**
     * Callback to check whether if the snackbar is shown or dismissed
     * @see .addCallback
     */
    open class Callback : BaseTransientBottomBar.BaseCallback<CustomSnackbar>() {

        override fun onShown(inCustomSnackBar: CustomSnackbar) {
            // Stub implementation to make API check happy.
        }

        override fun onDismissed(
            inTransientBottomBar: CustomSnackbar,
            @DismissEvent
            inEvent: Int
        ) {
            // Stub implementation to make API check happy.
        }
    }

    companion object {
        /**
         * Duration before the snackbar is automatically dismissed
         * @see .setDuration
         */
        //Show the Snackbar indefinitely
        const val LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE
        //Show the Snackbar for a short period of time.
        const val LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT
        // Show the Snackbar for a long period of time.
        const val LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG

        /**
         * Initialize the CustomSnackBar
         */
        fun make(
            view: View,
            message: CharSequence?,
            @Duration
            inDuration: Int,
            endButtonMaxCharacters: Int? = -1
        ):
            CustomSnackbar {
            // First we find a suitable parent for our custom view
            val vParent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )
            // We inflate our custom view
            val vCustomSnackbarView: CustomSnackbarView by lazy {
                LayoutInflater.from(view.context).inflate(
                    R.layout.snackbar_custom,
                    vParent,
                    false
                ) as CustomSnackbarView
            }
            //set snackbar message
            val messageTextView = vCustomSnackbarView.snackBarMessage
            messageTextView.text = message
            SnackBarUtils.adaptTextPaddingForMultilines(
                vCustomSnackbarView.context,
                messageTextView
            )
            // We create and return our Snackbar
            val snackbar = CustomSnackbar(
                vParent,
                vCustomSnackbarView,
                endButtonMaxCharacters
            )
            snackbar.duration = inDuration // apply duration
            return snackbar
        }
    }
}
