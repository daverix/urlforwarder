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

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable
import net.daverix.urlforward.dao.LinkFilter
import java.util.*

class LinkFilterViewModel : BaseObservable, Parcelable {
    @get:Bindable var id: Long = 0
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

    @get:Bindable var created: Date = Date()
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var updated: Date = Date()
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable var skipEncode: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    constructor(parcel: Parcel) {
        id = parcel.readLong()
        title = parcel.readString()
        filterUrl = parcel.readString()
        replaceText = parcel.readString()
        created = Date(parcel.readLong())
        updated = Date(parcel.readLong())
        skipEncode = parcel.readByte().toInt() == 1
        replaceSubject = parcel.readString()
    }

    constructor()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(filterUrl)
        dest.writeString(replaceText)
        dest.writeLong(created.time)
        dest.writeLong(updated.time)
        dest.writeByte((if (skipEncode) 1 else 0).toByte())
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
        skipEncode = item.skipEncode
        filterUrl = item.filterUrl
        replaceSubject = item.replaceSubject
        replaceText = item.replaceText
        title = item.title
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LinkFilterViewModel> = object : Parcelable.Creator<LinkFilterViewModel> {
            override fun createFromParcel(source: Parcel): LinkFilterViewModel {
                return LinkFilterViewModel(source)
            }

            override fun newArray(size: Int): Array<LinkFilterViewModel> {
                return newArray(size)
            }
        }
    }
}
