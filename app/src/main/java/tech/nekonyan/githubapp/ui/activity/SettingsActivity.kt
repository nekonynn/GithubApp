package tech.nekonyan.githubapp.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.ui.fragment.SettingsFragment
import tech.nekonyan.githubapp.util.Extensions.enableBackButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsFragment = SettingsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, settingsFragment).commit()

        enableBackButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}