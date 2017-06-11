package net.daverix.urlforward

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LinkDialogModule {
    @ContributesAndroidInjector
    internal abstract fun contributesLinkFragmentInjector() : LinksFragment
}


