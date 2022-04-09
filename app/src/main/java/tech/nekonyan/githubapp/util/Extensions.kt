package tech.nekonyan.githubapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestBuilder
import tech.nekonyan.githubapp.R

object Extensions {
    fun View.hideKeyboard() {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        } catch (ex: Exception) {
            Log.e("Extensions", "Error occurred.")
        }
    }

    fun AppCompatActivity.enableBackButton(@DrawableRes backButton: Int? = null) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (backButton != null) supportActionBar?.setHomeAsUpIndicator(backButton)
    }

    fun RequestBuilder<Drawable>.setPlaceholderAndError(context: Context): RequestBuilder<Drawable> {
        return this.placeholder(ContextCompat.getDrawable(context, R.drawable.bg_shimmer))
            .error(ContextCompat.getDrawable(context, R.drawable.bg_error))
    }
}