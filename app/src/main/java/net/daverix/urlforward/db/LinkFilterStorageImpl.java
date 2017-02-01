/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2017 David Laurell

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
package net.daverix.urlforward.db;


import net.daverix.urlforward.LinkFilter;
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static net.daverix.urlforward.Constants.TABLE_FILTER;

public class LinkFilterStorageImpl implements LinkFilterStorage {
    private final DatabaseObservable database;
    private final Mapper<LinkFilter> mapper;
    private final Provider<Long> timestampProvider;

    @Inject
    public LinkFilterStorageImpl(DatabaseObservable database,
                                 Mapper<LinkFilter> mapper,
                                 @Named("timestamp") Provider<Long> timestampProvider) {
        this.database = database;
        this.mapper = mapper;
        this.timestampProvider = timestampProvider;
    }

    @Override
    public Completable insert(LinkFilter linkFilter) {
        linkFilter.setUpdated(timestampProvider.get());
        return Observable.just(linkFilter)
                .map(mapper::getValues)
                .flatMapCompletable(values ->
                        database.insert(TABLE_FILTER, values)
                                .doAfterSuccess(linkFilter::setId)
                                .toCompletable()
                );
    }

    @Override
    public Completable update(LinkFilter linkFilter) {
        linkFilter.setUpdated(timestampProvider.get());
        return Observable.just(linkFilter)
                .map(mapper::getValues)
                .flatMapCompletable(values -> {
                    String[] args = getIdArgs(linkFilter.getId());
                    return database.update(TABLE_FILTER, values, UrlFilters._ID + "=?", args)
                            .toCompletable();
                });
    }

    @Override
    public Completable delete(long id) {
        String[] args = getIdArgs(id);
        return Completable.defer(() -> database.delete(TABLE_FILTER, UrlFilters._ID + "=?", args)
                .toCompletable());
    }

    private String[] getIdArgs(long id) {
        return new String[]{String.valueOf(id)};
    }

    @Override
    public Single<LinkFilter> getFilter(long id) {
        String query = String.format("%s=?", UrlFilters._ID);
        String[] columns = mapper.getColumns();
        String[] args = getIdArgs(id);

        return database.query(TABLE_FILTER, columns, query, args, "1")
                .map(mapper::mapFilter)
                .singleOrError();
    }

    @Override
    public Observable<LinkFilter> queryAll() {
        return database.query(TABLE_FILTER, mapper.getColumns(), null, null, null)
                .map(mapper::mapFilter);
    }
}
