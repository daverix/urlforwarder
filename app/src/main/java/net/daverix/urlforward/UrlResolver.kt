package net.daverix.urlforward

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.net.toUri
import javax.inject.Inject

interface UrlResolver {
    fun resolveUrl(url: String): Boolean
}

class BrowsableAppUrlResolver @Inject constructor(
    private val packageManager: PackageManager
) : UrlResolver {
    @SuppressLint("QueryPermissionsNeeded")
    override fun resolveUrl(url: String): Boolean = try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        intent.resolveActivity(packageManager) != null
        true
    } catch (ex: Exception) {
        Log.e(
            "UrlResolver",
            "Cannot find matching app for url $url",
            ex
        )
        false
    }
}
