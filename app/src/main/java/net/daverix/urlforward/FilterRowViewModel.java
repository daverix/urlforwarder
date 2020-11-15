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

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class FilterRowViewModel extends BaseObservable{
    private final FiltersFragment.FilterSelectedListener listener;
    private String title;
    private String filterUrl;
    private long id;

    public FilterRowViewModel(FiltersFragment.FilterSelectedListener listener, String title, String filterUrl, long id) {
        this.listener = listener;
        this.title = title;
        this.filterUrl = filterUrl;
        this.id = id;
    }

    public void onClick() {
        listener.onFilterSelected(id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public String getFilterUrl() {
        return filterUrl;
    }
}
