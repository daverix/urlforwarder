package net.daverix.urlforward


import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.daverix.urlforward.db.DatabaseModule
import net.daverix.urlforward.filter.FilterModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseModule::class,
        FilterModule::class,
        AppModule::class,
        TimeStampModule::class,
        AndroidSupportInjectionModule::class))
interface AppComponent : AndroidInjector<UrlForwarderApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<UrlForwarderApplication>()
}
