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

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable

class LinkFilter : BaseObservable, Parcelable {
    @get:[Bindable] var id: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var filterUrl: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var replaceText: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var replaceSubject: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var created: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var updated: Long = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var encoded: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    constructor(parcel: Parcel) {
        id = parcel.readLong()
        title = parcel.readString()
        filterUrl = parcel.readString()
        replaceText = parcel.readString()
        created = parcel.readLong()
        updated = parcel.readLong()
        encoded = parcel.readByte().toInt() == 1
        replaceSubject = parcel.readString()
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
        return title
    }

    fun update(item: LinkFilter) {
        created = item.created
        updated = item.updated
        encoded = item.encoded
        filterUrl = item.filterUrl
        replaceSubject = item.replaceSubject
        replaceText = item.replaceText
        title = item.title
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LinkFilter> = object : Parcelable.Creator<LinkFilter> {
            override fun createFromParcel(source: Parcel): LinkFilter {
                return LinkFilter(source)
            }

            override fun newArray(size: Int): Array<LinkFilter> {
                return newArray(size)
            }
        }
    }
}
