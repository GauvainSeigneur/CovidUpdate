package com.gauvain.seigneur.covidupdate.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting

open class StringPresenter(
    @StringRes
    val key: Int,
    private vararg val formatArgs: Any
) {

    internal fun getFormattedString(
        context: Context
    ): String =
        context.getString(key, *formatArgs)

    @VisibleForTesting
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringPresenter) return false

        if (key != other.key) return false
        if (!formatArgs.contentEquals(other.formatArgs)) return false

        return true
    }

    @VisibleForTesting
    override fun hashCode(): Int {
        var result = key
        result = 31 * result + formatArgs.contentHashCode()
        return result
    }
}

