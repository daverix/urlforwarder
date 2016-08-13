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

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.daverix.urlforward.db.UrlForwarderContract;

import java.util.ArrayList;
import java.util.List;

public class LinksFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LOAD_FILTERS = 0;
    private LinksFragmentListener listener;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        listener = (LinksFragmentListener) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_LOAD_FILTERS, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        LinkFilter filter = (LinkFilter) l.getItemAtPosition(position);
        if(listener != null) {
            listener.onLinkClick(filter);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_LOAD_FILTERS:
                return new CursorLoader(getActivity(), UrlForwarderContract.UrlFilters.CONTENT_URI,
                        new String[] {
                                UrlForwarderContract.UrlFilters._ID,
                                UrlForwarderContract.UrlFilters.TITLE,
                                UrlForwarderContract.UrlFilters.FILTER,
                                UrlForwarderContract.UrlFilters.REPLACE_TEXT,
                                UrlForwarderContract.UrlFilters.CREATED,
                                UrlForwarderContract.UrlFilters.UPDATED
                        }, null, null, UrlForwarderContract.UrlFilters.TITLE);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_LOAD_FILTERS:
                setListAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        mapFilters(data)));
                break;
        }
    }

    private List<LinkFilter> mapFilters(Cursor cursor) {
        List<LinkFilter> filters = new ArrayList<LinkFilter>();

        for(int i=0;cursor != null && cursor.moveToPosition(i); i++) {
            LinkFilter filter = new LinkFilter();
            filter.setId(cursor.getLong(0));
            filter.setTitle(cursor.getString(1));
            filter.setFilterUrl(cursor.getString(2));
            filter.setReplaceText(cursor.getString(3));
            filter.setCreated(cursor.getLong(4));
            filter.setUpdated(cursor.getLong(5));
            filters.add(filter);
        }

        return filters;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface LinksFragmentListener {
        void onLinkClick(LinkFilter filter);
    }
}
