package net.daverix.urlforward

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(app: UrlForwarderApplication): Context
}
