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

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import net.daverix.urlforward.databinding.SaveFilterFragmentBinding;
import net.daverix.urlforward.db.LinkFilterStorage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SaveFilterFragment extends Fragment {
    private static final String ARG_STATE = "state";
    private static final String ARG_URI = "uri";
    private static final int STATE_CREATE = 0;
    private static final int STATE_UPDATE = 1;

    private LinkFilter filter;
    private Uri uri;
    private int state;
    private Disposable loadFilterDisposable;

    @Inject LinkFilterStorage storage;
    @Inject @Named("timestamp") Provider<Long> timestampProvider;

    public static SaveFilterFragment newCreateInstance() {
        SaveFilterFragment saveFilterFragment = new SaveFilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, STATE_CREATE);
        saveFilterFragment.setArguments(args);
        return saveFilterFragment;
    }

    public static SaveFilterFragment newUpdateInstance(Uri uri) {
        SaveFilterFragment saveFilterFragment = new SaveFilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, STATE_UPDATE);
        args.putParcelable(ARG_URI, uri);
        saveFilterFragment.setArguments(args);
        return saveFilterFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ((UrlForwarderApplication) getActivity().getApplication())
                .getFragmentComponentBuilder(SaveFilterFragment.class)
                .build()
                .injectMembers(this);

        Bundle args = getArguments();
        if (args != null) {
            uri = args.getParcelable(ARG_URI);
            state = args.getInt(ARG_STATE);
        }

        if(savedInstanceState != null) {
            filter = savedInstanceState.getParcelable("filter");
        }
        else {
            filter = new LinkFilter();

            if (state == STATE_CREATE) {
                filter.setCreated(timestampProvider.get());
                filter.setFilterUrl("http://example.com/?url=@url&subject=@subject");
                filter.setReplaceText("@url");
                filter.setReplaceSubject("@subject");
                filter.setEncoded(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        if(loadFilterDisposable != null) {
            loadFilterDisposable.dispose();
        }

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("filter", filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SaveFilterFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.save_filter_fragment, container, false);
        binding.setFilter(filter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(state != STATE_CREATE) {
            long id = Long.parseLong(uri.getLastPathSegment());
            filter.setId(id);

            loadFilterDisposable = storage.getFilter(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(filter::update);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_save_filter, menu);
    }

    public LinkFilter getFilter() {
        return filter;
    }
}
