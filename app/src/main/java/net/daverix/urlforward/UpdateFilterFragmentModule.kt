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
package net.daverix.urlforward

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal object UpdateFilterFragmentModule {
    @JvmStatic @Provides @Named("extraFilterId")
    fun getFilterId(fragment: UpdateFilterFragment): Long {
        return fragment.arguments?.getLong(UpdateFilterFragment.ARG_FILTER_ID) ?: -1
    }
}
