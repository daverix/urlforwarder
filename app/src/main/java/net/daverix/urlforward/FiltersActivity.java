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
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import net.daverix.urlforward.databinding.FiltersActivityBinding;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

public class FiltersActivity extends AppCompatActivity implements FiltersFragment.FilterSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FiltersActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.filters_activity);
        binding.btnAddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_INSERT, UrlFilters.CONTENT_URI));
            }
        });
    }

    @Override
    public void onFilterSelected(long id) {
        startActivity(new Intent(Intent.ACTION_EDIT, Uri.withAppendedPath(UrlFilters.CONTENT_URI, String.valueOf(id))));
    }
}
