/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2016 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daverix.urlforward

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import net.daverix.urlforward.LinksFragment.LinksFragmentListener

class LinkDialogActivity : FragmentActivity(), LinksFragmentListener {
    private var url: String? = null
    private var subject: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.link_dialog_activity)

        val intent = intent
        if (intent == null) {
            Toast.makeText(this, "Invalid intent!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "Intent empty")
            finish()
            return
        }

        val url = intent.getStringExtra(Intent.EXTRA_TEXT)
        this.url = url
        subject = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "No url found in shared data!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "No StringExtra with url in intent")
            finish()
        }
    }

    override fun onLinkClick(filter: LinkFilter) {

        try {
            val uri = createUri(filter, url, subject)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            finish()
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "No app found matching ${filter.filterUrl}", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "activity not found for ${filter.filterUrl}", ex)
        } catch (ex: Exception) {
            Toast.makeText(this, "Error forwarding url", Toast.LENGTH_SHORT).show()
            Log.e(
                "LinkDialogActivity",
                "error launching intent with " +
                        "filterUrl ${filter.filterUrl}, " +
                        "replaceText ${filter.replaceText}, " +
                        "replaceSubject ${filter.replaceSubject}, " +
                        "url $url and subject $subject",
                ex
            )
        }
    }
}