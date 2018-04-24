package fi.konstal.bullet_your_life;

import android.app.Application;

import fi.konstal.bullet_your_life.dagger.component.AppComponent;
import fi.konstal.bullet_your_life.dagger.component.DaggerAppComponent;
import fi.konstal.bullet_your_life.dagger.module.AppModule;
import fi.konstal.bullet_your_life.dagger.module.RoomModule;


/**
 * The class that handles Global state
 * Is singleton by default by extending {@link Application}
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class App extends Application {

    private AppComponent appComponent;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }

    /**
     * Returns the Application class
     *
     * @return the application
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
