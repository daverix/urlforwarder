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
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.daverix.urlforward.databinding.SaveFilterActivityBinding;
import net.daverix.urlforward.db.LinkFilterStorage;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SaveFilterActivity extends AppCompatActivity {
    private SaveFilterFragment fragment;
    private CompositeDisposable compositeDisposable;

    @Inject
    LinkFilterStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        ((UrlForwarderApplication) getApplication()).getActivityComponentBuilder(SaveFilterActivity.class)
                .build()
                .injectMembers(this);

        SaveFilterActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.save_filter_activity);
        binding.toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(SaveFilterActivity.this, FiltersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        fragment = (SaveFilterFragment) getSupportFragmentManager().findFragmentById(R.id.saveFilterFragment);

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_INSERT.equals(intent.getAction())) {
            binding.toolbar.setTitle(R.string.create_filter);
            binding.toolbar.inflateMenu(R.menu.fragment_save_filter);

            if (fragment == null) {
                fragment = SaveFilterFragment.newCreateInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.saveFilterFragment, fragment).commit();
            }
        } else if (intent != null && Intent.ACTION_EDIT.equals(intent.getAction())) {
            binding.toolbar.setTitle(R.string.edit_filter);
            binding.toolbar.inflateMenu(R.menu.fragment_edit_filter);

            if (fragment == null) {
                fragment = SaveFilterFragment.newUpdateInstance(intent.getData());
                getSupportFragmentManager().beginTransaction().add(R.id.saveFilterFragment, fragment).commit();
            }
        }

        binding.toolbar.setOnMenuItemClickListener(item -> {
            Intent intent1 = getIntent();

            switch (item.getItemId()) {
                case R.id.menuSave:
                    if (intent1 != null && Intent.ACTION_INSERT.equals(intent1.getAction())) {
                        createFilter(fragment.getFilter());
                    } else if (intent1 != null && Intent.ACTION_EDIT.equals(intent1.getAction())) {
                        updateFilter(fragment.getFilter());
                    }
                    return true;
                case R.id.menuDelete:
                    deleteFilter(intent1.getData());
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    private void createFilter(LinkFilter linkFilter) {
        compositeDisposable.add(storage.insert(linkFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this::idlingResource)
                .subscribe(this::finish));
    }

    private void updateFilter(LinkFilter linkFilter) {
        compositeDisposable.add(storage.update(linkFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this::idlingResource)
                .subscribe(this::finish));
    }

    private void deleteFilter(Uri uri) {
        compositeDisposable.add(storage.delete(Long.parseLong(uri.getLastPathSegment()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this::idlingResource)
                .subscribe(this::finish));
    }

    private CompletableSource idlingResource(Completable completable) {
        ModifyFilterIdlingResource idlingResource = ((UrlForwarderApplication) getApplication())
                .getModifyFilterIdlingResource();

        if (idlingResource == null) {
            return completable;
        }

        return completable
                .doOnSubscribe(d -> idlingResource.setIdle(false))
                .doFinally(() -> idlingResource.setIdle(true));
    }
}
