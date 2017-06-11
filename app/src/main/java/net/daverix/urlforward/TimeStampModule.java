package net.daverix.urlforward;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class TimeStampModule {
    @Provides @Named("timestamp")
    static Long provideTimeStamp() {
        return System.currentTimeMillis();
    }
}
