package net.daverix.urlforward

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class LinkDialogModule {
    @Binds @ViewModelScoped
    abstract fun bindUrlResolver(resolver: BrowsableAppUrlResolver): UrlResolver
}
