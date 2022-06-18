package net.daverix.urlforward

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.daverix.urlforward.db.DefaultFilterDao
import net.daverix.urlforward.db.FilterDao
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
@InstallIn(SingletonComponent::class)
abstract class UrlForwarderModule {
    @Binds @Singleton
    abstract fun bindFilterDao(dao: DefaultFilterDao): FilterDao

    @Binds
    abstract fun bindContext(application: Application): Context
}
