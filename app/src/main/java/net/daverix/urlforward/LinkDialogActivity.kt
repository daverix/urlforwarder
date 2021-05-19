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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class LinkDialogActivity : FragmentActivity() {
    private var url: String? = null
    private var subject: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (intent == null) {
            Toast.makeText(this, "Invalid intent!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "Intent empty")
            finish()
            return
        }

        url = intent.getStringExtra(Intent.EXTRA_TEXT)
        subject = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        if (url.isNullOrEmpty() && subject.isNullOrEmpty()) {
            Toast.makeText(this, "No url or subject found in shared data!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "No ${Intent.EXTRA_TEXT} or ${Intent.EXTRA_SUBJECT} in intent")
            finish()
        }
    }

    private fun onLinkClick(filter: LinkFilter) {
        //TODO: add groups to db model
        val result = tryCreateUrl(
            urlPattern = filter.urlPattern,
            urlGroups = emptyList(),
            subjectPattern = filter.subjectPattern,
            subjectGroups = emptyList(),
            inputUrl = url ?: "",
            inputSubject = subject ?: "",
            outputUrl = filter.outputUrl
        )
        if(result is CreateUrlResult.Successful) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            finish()
        } else {
            Log.e("LinkDialogActivity", "not matching url $url or subject $subject")
        }
    }
}