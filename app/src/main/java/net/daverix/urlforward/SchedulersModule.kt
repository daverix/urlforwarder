package net.daverix.urlforward

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
object SchedulersModule {
    @JvmStatic @Provides @Named("io")
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    @JvmStatic @Provides @Named("main")
    fun provideMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}