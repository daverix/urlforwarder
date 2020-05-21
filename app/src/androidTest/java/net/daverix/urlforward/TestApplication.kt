package net.daverix.urlforward

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import net.daverix.urlforward.dao.LinkFilterDao
import javax.inject.Inject


class TestApplication : DaggerApplication() {
    @Inject
    lateinit var linkFilterDao: LinkFilterDao

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.factory().create(this)
    }
}
