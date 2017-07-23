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

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject
import javax.inject.Named

class UrlForwarderApplication : DaggerApplication() {
    @set:[Inject Named("modify")] lateinit var modifyIdleCounter: ProxyIdleCounter
    @set:[Inject Named("load")] lateinit var loadIdleCounter: ProxyIdleCounter

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
