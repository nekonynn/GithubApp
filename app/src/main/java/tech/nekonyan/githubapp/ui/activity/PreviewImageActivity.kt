package tech.nekonyan.githubapp.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.databinding.ActivityPreviewImageBinding
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.Extensions.enableBackButton
import tech.nekonyan.githubapp.util.Extensions.setPlaceholderAndError

class PreviewImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPreviewImageBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_preview_image)
        intent.getStringExtra(Constants.AVATAR_URL_EXTRA)?.let { imagePath ->
            Glide.with(this).load(imagePath).setPlaceholderAndError(this).into(binding.ivImage)
        }

        enableBackButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}