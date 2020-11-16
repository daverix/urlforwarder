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
package net.daverix.urlforward

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class LinkFilter : BaseObservable, Parcelable {
    @get:Bindable
    var id: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var filterUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.filterUrl)
        }
    @get:Bindable
    var replaceText: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.replaceText)
        }

    @get:Bindable
    var replaceSubject: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.replaceSubject)
        }

    @get:Bindable
    var created: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.created)
        }

    @get:Bindable
    var updated: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.updated)
        }

    @get:Bindable
    var encoded = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.encoded)
        }

    constructor(`in`: Parcel) {
        id = `in`.readLong()
        title = `in`.readString()
        filterUrl = `in`.readString()
        replaceText = `in`.readString()
        created = `in`.readLong()
        updated = `in`.readLong()
        encoded = `in`.readByte().toInt() == 1
        replaceSubject = `in`.readString()
    }

    constructor()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(filterUrl)
        dest.writeString(replaceText)
        dest.writeLong(created)
        dest.writeLong(updated)
        dest.writeByte((if (encoded) 1 else 0).toByte())
        dest.writeString(replaceSubject)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return title ?: ""
    }

    companion object CREATOR : Parcelable.Creator<LinkFilter> {
        override fun createFromParcel(parcel: Parcel): LinkFilter {
            return LinkFilter(parcel)
        }

        override fun newArray(size: Int): Array<LinkFilter?> {
            return arrayOfNulls(size)
        }
    }
}
