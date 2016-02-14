package net.daverix.urlforward;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

public class FiltersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LOAD_FILTERS = 1;
    private FilterSelectedListener mListener;
    private RecyclerView list;
    private TextView emptyText;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        mListener = (FilterSelectedListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setVisibility(View.GONE);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        emptyText = (TextView) view.findViewById(R.id.emptyText);
        emptyText.setVisibility(View.GONE);
        return view;
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
                if(data.getCount() > 0) {
                    items = mapListFilters(data);
                    list.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }
                else {
                    items = new ArrayList<>();
                    Log.d("FiltersFragment", "list is empty");
                    list.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }

                FilterAdapter adapter = (FilterAdapter) list.getAdapter();
                if(adapter == null) {
                    Log.d("FiltersFragment", "creating adapter");
                    adapter = new FilterAdapter(items);
                    list.setAdapter(adapter);
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
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("FiltersFragment", "loader " + loader.getId() + " reset");
    }

    public interface FilterSelectedListener {
        void onFilterSelected(long id);
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

    private class FilterAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<ListFilter> filters;

        public FilterAdapter(List<ListFilter> filters) {
            this.filters = filters;
        }

        public void setFilters(List<ListFilter> filters) {
            this.filters = filters;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_row, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            holder.title = (TextView) view.findViewById(android.R.id.text1);
            holder.description = (TextView) view.findViewById(android.R.id.text2);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ListFilter filter = filters.get(position);
            holder.title.setText(filter.getTitle());
            holder.description.setText(filter.getFilterUrl());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        mListener.onFilterSelected(filter.getId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return filters.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ListFilter {
        private String title;
        private String filterUrl;
        private long id;

        private ListFilter(String title, String filterUrl, long id) {
            this.title = title;
            this.filterUrl = filterUrl;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public String getFilterUrl() {
            return filterUrl;
        }

        public long getId() {
            return id;
        }
    }
}
