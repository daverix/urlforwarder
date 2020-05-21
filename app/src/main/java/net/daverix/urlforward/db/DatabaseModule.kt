/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2018 David Laurell

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
package net.daverix.urlforward.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import net.daverix.urlforward.dao.LinkFilterDao
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(context: Context): UrlForwarderDatabase {
        return Room.databaseBuilder(context, UrlForwarderDatabase::class.java, DB_NAME)
                .addMigrations(MigrationTo3(), MigrationTo4())
                .build()
    }

    @Provides
    fun provideLinkFilterDao(db: UrlForwarderDatabase): LinkFilterDao {
        return db.getLinkFilterDao()
    }
}
