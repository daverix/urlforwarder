package net.daverix.urlforward

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [
    AssistedInject_AssistedModule::class
])
class AssistedModule