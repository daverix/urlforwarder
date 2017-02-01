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

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.daverix.urlforward.LinkFilter;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class DatabaseModule {
    @Binds
    abstract SQLiteOpenHelper bindSqliteOpenHelper(UrlForwarderDatabaseHelper helper);

    @Provides @Singleton
    static SQLiteDatabase provideDatabase(SQLiteOpenHelper helper) {
        return helper.getWritableDatabase();
    }

    @Binds
    abstract Mapper<LinkFilter> bindLinkFilterMapper(LinkFilterMapper mapper);

    @Binds @Singleton
    abstract LinkFilterStorage bindLinkFilterStorage(LinkFilterStorageImpl storage);

    @Provides @Named("timestamp")
    static Long provideTimeStamp() {
        return System.currentTimeMillis();
    }
}
