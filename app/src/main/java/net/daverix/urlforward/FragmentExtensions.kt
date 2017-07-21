package net.daverix.urlforward

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.addFragmentIfNotExists(id: Int, fragmentFactory: () -> Fragment) {
    if (supportFragmentManager.findFragmentById(id) == null) {
        supportFragmentManager.beginTransaction()
                .add(id, fragmentFactory())
                .commit()
    }
}
