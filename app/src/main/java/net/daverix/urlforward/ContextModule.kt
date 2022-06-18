package net.daverix.urlforward

import android.content.Context
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck

@Module
// this module should only be included in other modules where context is provided
@DisableInstallInCheck
object ContextModule {
    @Provides
    fun providePackageManager(context: Context): PackageManager = context.packageManager
}
