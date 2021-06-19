package net.daverix.urlforward

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri

fun Activity.startActivity(
    filter: LinkFilter,
    url: String?,
    subject: String?
) {
    val uri = try {
        createUrl(filter, url, subject).toUri()
    } catch (ex: Exception) {
        val errorMessage =
            "Error creating url from ${filter.filterUrl} with input url \"$url\" and subject \"$subject\""
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        Log.e("LinkDialogActivity", errorMessage, ex)
        return
    }

    try {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
        finish()
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(this, "No app found matching url $uri", Toast.LENGTH_SHORT).show()
        Log.e("LinkDialogActivity", "activity not found for $uri", ex)
    } catch (ex: Exception) {
        Toast.makeText(this, "Error forwarding url $uri: ${ex.message}", Toast.LENGTH_SHORT)
            .show()
        Log.e(
            "LinkDialogActivity",
            "error launching intent with url $uri",
            ex
        )
    }
}