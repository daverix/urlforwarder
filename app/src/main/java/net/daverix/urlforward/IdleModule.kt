package net.daverix.urlforward

import dagger.Binds
import dagger.Module
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class IdleModule {
    @Binds @Singleton @Named("modify")
    abstract fun provideModifyIdleCounter(counter: ProxyIdleCounter): IdleCounter

    @Binds @Singleton @Named("load")
    abstract fun provideLoadIdleCounter(counter: ProxyIdleCounter): IdleCounter
}
