package com.gauvain.seigneur.covidupdate.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.VisibleForTesting

open class QuantityStringPresenter(
    @PluralsRes
    val key: Int,
    val quantity: Int,
    private vararg val formatArgs: Any

) {

    internal fun getFormattedString(
        context: Context
    ): String = context.resources.getQuantityString(key, quantity, *formatArgs)

    @VisibleForTesting
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuantityStringPresenter) return false

        if (key != other.key) return false
        if (quantity != other.quantity) return false
        if (!formatArgs.contentEquals(other.formatArgs)) return false

        return true
    }

    @VisibleForTesting
    override fun hashCode(): Int {
        var result = key
        result = 31 * result + quantity
        result = 31 * result + formatArgs.contentHashCode()
        return result
    }
}

