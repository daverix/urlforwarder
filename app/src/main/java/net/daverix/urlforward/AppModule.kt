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
package net.daverix.urlforward

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {
    @Binds
    internal abstract fun bindContext(app: UrlForwarderApplication): Context

    @ContributesAndroidInjector(modules = arrayOf(LinkDialogModule::class))
    internal abstract fun linkDialogActivityInjector(): LinkDialogActivity

    @ContributesAndroidInjector(modules = arrayOf(InsertFilterActivityModule::class))
    internal abstract fun insertFilterActivityInjector(): InsertFilterActivity

    @ContributesAndroidInjector(modules = arrayOf(UpdateFilterActivityModule::class))
    internal abstract fun updateFilterActivityInjector(): UpdateFilterActivity

    @ContributesAndroidInjector(modules = arrayOf(FiltersActivityModule::class))
    internal abstract fun filtersActivityInjector(): FiltersActivity
}

