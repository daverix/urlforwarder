package net.daverix.urlforward

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.daverix.urlforward.db.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DatabaseModule::class,
    AndroidInjectorsModule::class,
    TimeStampModule::class,
    AndroidSupportInjectionModule::class,
    AssistedModule::class,
    TestAppModule::class,
    DispatchersModule::class
])
interface TestAppComponent : AndroidInjector<TestApplication> {
    @Component.Factory
    interface Factory : AndroidInjector.Factory<TestApplication>
}
