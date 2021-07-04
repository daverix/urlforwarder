package net.daverix.urlforward

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.net.toUri

interface UrlResolver {
    fun resolveUrl(url: String): Boolean
}

class BrowsableAppUrlResolver(
    private val packageManager: PackageManager
) : UrlResolver {
    override fun resolveUrl(url: String): Boolean = try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
    } catch (ex: Exception) {
        Log.e(
            "UrlResolver",
            "Cannot find matching app for url $url",
            ex
        )
        false
    }
}
