package net.daverix.urlforward;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.daverix.urlforward.db.UrlForwarderContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daverix on 12/19/13.
 */
public class LinkDialogActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LOAD_FILTERS = 0;
    private String mUrl;
    private UriFilterCombiner mUriFilterCombiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_choose_filter);

        mUriFilterCombiner = new UriFilterCombinerImpl();

        Intent intent = getIntent();
        if(intent == null) {
            Toast.makeText(this, "Invalid intent!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(mUrl == null) {
            Toast.makeText(this, "No url found in intent!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        getLoaderManager().initLoader(LOADER_LOAD_FILTERS, null, this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {
            LinkFilter filter = (LinkFilter) l.getItemAtPosition(position);
            startActivity(new Intent(Intent.ACTION_VIEW, mUriFilterCombiner.create(filter, mUrl)));
        } catch (Exception e) {
            Log.e("LinkDialogActivity", "error launching intent with url " + mUrl, e);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_LOAD_FILTERS:
                return new CursorLoader(this, UrlForwarderContract.UrlFilters.CONTENT_URI,
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
                setListAdapter(new ArrayAdapter<LinkFilter>(this,
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
}
