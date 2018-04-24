package fi.konstal.bullet_your_life.dagger.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Module for providing Application related Objects for Dagger
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
@Module
public class AppModule {

    Application mApplication;

    /**
     * Constuctor
     *
     * @param application the Application
     */
    public AppModule(Application application) {
        mApplication = application;
    }

    /**
     * Gives Dagger access to Application Object
     *
     * @return the Application
     */
    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

}