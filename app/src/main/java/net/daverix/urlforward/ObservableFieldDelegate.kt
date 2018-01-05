package net.daverix.urlforward

import android.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class ObservableFieldDelegate<T>(private var value: T,
                                 private var id: Int) : ReadWriteProperty<BaseObservable, T> {
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.notifyPropertyChanged(id)
    }
}