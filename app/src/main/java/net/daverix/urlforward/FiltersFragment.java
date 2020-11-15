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
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.daverix.urlforward.databinding.FilterRowBinding;
import net.daverix.urlforward.databinding.FiltersFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

public class FiltersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LOAD_FILTERS = 1;
    private FilterSelectedListener mListener;
    private FiltersViewModel viewModel;
    private FilterAdapter adapter;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        mListener = (FilterSelectedListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new FiltersViewModel();
        adapter = new FilterAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FiltersFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.filters_fragment, container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.list.setAdapter(adapter);
        binding.setFilters(viewModel);
        return binding.getRoot();
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
                List<FilterRowViewModel> items;
                if(data.getCount() > 0) {
                    items = mapListFilters(data);
                    viewModel.filtersVisible.set(true);
                }
                else {
                    items = new ArrayList<>();
                    Log.d("FiltersFragment", "list is empty");
                    viewModel.filtersVisible.set(false);
                }

                Log.d("FiltersFragment", "updating adapter");
                adapter.setFilters(items);
                adapter.notifyDataSetChanged();
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

    private List<FilterRowViewModel> mapListFilters(Cursor cursor) {
        if(cursor == null) throw new IllegalArgumentException("cursor is null");

        List<FilterRowViewModel> items = new ArrayList<FilterRowViewModel>(cursor.getCount());

        for(int i=0;cursor.moveToPosition(i);i++) {
            items.add(new FilterRowViewModel(mListener,
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(0)));
        }

        return items;
    }

    private class FilterAdapter extends RecyclerView.Adapter<BindingHolder<FilterRowBinding>> {
        private List<FilterRowViewModel> filters;

        FilterAdapter() {
            filters = new ArrayList<>();
        }

        void setFilters(List<FilterRowViewModel> filters) {
            this.filters = filters;
        }

        @Override
        public BindingHolder<FilterRowBinding> onCreateViewHolder(ViewGroup viewGroup, int i) {
            FilterRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.filter_row, viewGroup, false);
            return new BindingHolder<>(binding);
        }

        @Override
        public void onBindViewHolder(BindingHolder<FilterRowBinding> holder, int position) {
            final FilterRowViewModel filter = filters.get(position);
            holder.binding.setFilter(filter);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return filters.size();
        }
    }
}
