package net.daverix.urlforward

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
object DispatchersModule {
    @Provides @Named("io")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
