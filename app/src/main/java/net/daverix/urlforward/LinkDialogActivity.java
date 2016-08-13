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
package net.daverix.urlforward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class LinkDialogActivity extends FragmentActivity implements LinksFragment.LinksFragmentListener {
    private String url;
    private UriFilterCombiner mUriFilterCombiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.link_dialog_activity);

        mUriFilterCombiner = new UriFilterCombinerImpl();

        Intent intent = getIntent();
        if(intent == null) {
            Toast.makeText(this, "Invalid intent!", Toast.LENGTH_SHORT).show();
            Log.e("LinkDialogActivity", "Intent empty");
            finish();
            return;
        }

        url = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(url == null) {
            Toast.makeText(this, "No url found in intent!", Toast.LENGTH_SHORT).show();
            Log.e("LinkDialogActivity", "No StringExtra with url in intent");
            finish();
        }
    }

    @Override
    public void onLinkClick(LinkFilter filter) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, mUriFilterCombiner.create(filter, url)));
            finish();
        } catch (UriCombinerException e) {
            Log.e("LinkDialogActivity", "error launching intent with url " + url, e);
        }
    }
}
