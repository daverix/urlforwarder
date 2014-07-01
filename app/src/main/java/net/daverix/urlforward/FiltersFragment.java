package net.daverix.urlforward;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

/**
 * Created by daverix on 12/29/13.
 */
public class FiltersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LOAD_FILTERS = 1;
    private FilterSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (FilterSelectedListener) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyText(getString(R.string.empty_message));
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("FiltersFragment", "resumed");

        getLoaderManager().initLoader(LOADER_LOAD_FILTERS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("FiltersFragment", "create loader " + id);

        switch (id) {
            case LOADER_LOAD_FILTERS:
                return new CursorLoader(getActivity(), UrlFilters.CONTENT_URI,
                        new String[] {
                                UrlFilters._ID,
                                UrlFilters.TITLE,
                                UrlFilters.FILTER
                        }, null, null, UrlFilters.TITLE);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("FiltersFragment", "loader " + loader.getId() + " finished, items: " + data.getCount());

        switch (loader.getId()) {
            case LOADER_LOAD_FILTERS:
                List<ListFilter> items;
                if(data != null && data.getCount() > 0) {
                    items = mapListFilters(data);
                }
                else {
                    items = new ArrayList<ListFilter>();
                    Log.d("FiltersFragment", "list is empty");
                }

                FilterAdapter adapter = (FilterAdapter) getListAdapter();
                if(adapter == null) {
                    Log.d("FiltersFragment", "creating adapter");
                    adapter = new FilterAdapter(items);
                    setListAdapter(adapter);
                }
                else {
                    Log.d("FiltersFragment", "updating adapter");
                    adapter.setFilters(items);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.d("FiltersFragment", "click on item " + position + " with id " + id);

        if(mListener != null) {
            mListener.onFilterSelected(id);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("FiltersFragment", "loader " + loader.getId() + " reset");
    }

    public interface FilterSelectedListener {
        public void onFilterSelected(long id);
    }

    private List<ListFilter> mapListFilters(Cursor cursor) {
        if(cursor == null) throw new IllegalArgumentException("cursor is null");

        List<ListFilter> items = new ArrayList<ListFilter>(cursor.getCount());

        for(int i=0;cursor.moveToPosition(i);i++) {
            items.add(new ListFilter(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(0)));
        }

        return items;
    }

    private class FilterAdapter extends BaseAdapter {
        private List<ListFilter> mFilters;

        public FilterAdapter(List<ListFilter> filters) {
            mFilters = filters;
        }

        public void setFilters(List<ListFilter> filters) {
            mFilters = filters;
        }

        @Override
        public int getCount() {
            return mFilters.size();
        }

        @Override
        public Object getItem(int position) {
            return mFilters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mFilters.get(position).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_2, parent, false);
                holder.title = (TextView) convertView.findViewById(android.R.id.text1);
                holder.description = (TextView) convertView.findViewById(android.R.id.text2);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(mFilters.get(position).getTitle());
            holder.description.setText(mFilters.get(position).getFilterUrl());

            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView title;
        public TextView description;
    }

    private class ListFilter {
        private String mTitle;
        private String mFilterUrl;
        private long mId;

        private ListFilter(String title, String filterUrl, long id) {
            mTitle = title;
            mFilterUrl = filterUrl;
            mId = id;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getFilterUrl() {
            return mFilterUrl;
        }

        public long getId() {
            return mId;
        }
    }
}
