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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.daverix.urlforward.databinding.FilterRowBinding;
import net.daverix.urlforward.databinding.FiltersFragmentBinding;
import net.daverix.urlforward.db.LinkFilterStorage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FiltersFragment extends Fragment {
    private static final String TAG = "FiltersFragment";
    private FilterSelectedListener mListener;
    private FilterAdapter adapter;
    private Disposable filtersDisposable;

    @Inject FiltersViewModel viewModel;
    @Inject LinkFilterStorage storage;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        mListener = (FilterSelectedListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new FilterAdapter();

        ((UrlForwarderApplication) getActivity().getApplication())
                .getFragmentComponentBuilder(FiltersFragment.class)
                .build()
                .injectMembers(this);
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

        filtersDisposable = storage.queryAll()
                .map(filter -> new FilterRowViewModel(mListener, filter.getTitle(), filter.getFilterUrl(), filter.getId()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateItems, e -> Log.e(TAG, "Could not retrieve filters", e));
    }

    @Override
    public void onPause() {
        super.onPause();

        if(filtersDisposable != null) {
            filtersDisposable.dispose();
            filtersDisposable = null;
        }
    }

    public void updateItems(List<FilterRowViewModel> items) {
        viewModel.filtersVisible.set(items.size() > 0);

        adapter.setFilters(items);
        adapter.notifyDataSetChanged();
    }

    public interface FilterSelectedListener {
        void onFilterSelected(long id);
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
