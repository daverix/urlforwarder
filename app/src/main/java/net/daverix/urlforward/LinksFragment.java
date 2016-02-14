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
            filters.add(new LinkFilter(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getLong(4),
                    cursor.getLong(5)));
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
