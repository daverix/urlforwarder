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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.daverix.urlforward.db.LinkFilterStorage;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LinksFragment extends ListFragment {
    private LinksFragmentListener listener;
    private Disposable disposable;

    @Inject LinkFilterStorage storage;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        listener = (LinksFragmentListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((UrlForwarderApplication) getActivity().getApplication())
                .getFragmentComponentBuilder(LinksFragment.class)
                .build()
                .injectMembers(this);
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
    public void onResume() {
        super.onResume();

        disposable = storage.queryAll()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> setListAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        items)));
    }

    @Override
    public void onPause() {
        super.onPause();

        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public interface LinksFragmentListener {
        void onLinkClick(LinkFilter filter);
    }
}
