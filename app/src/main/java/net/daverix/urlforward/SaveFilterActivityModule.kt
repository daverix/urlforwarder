package net.daverix.urlforward

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class SaveFilterActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributesSaveFilterActivityInjector(): SaveFilterFragment
}
