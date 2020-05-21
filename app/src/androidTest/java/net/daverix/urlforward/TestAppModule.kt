package net.daverix.urlforward

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class TestAppModule {
    @Binds
    abstract fun bindContext(app: TestApplication): Context
}
