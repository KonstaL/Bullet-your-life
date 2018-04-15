package fi.konstal.bullet_your_life;

import android.app.Application;
import android.content.Context;


import fi.konstal.bullet_your_life.dagger.component.AppComponent;
import fi.konstal.bullet_your_life.dagger.component.DaggerAppComponent;
import fi.konstal.bullet_your_life.dagger.module.AppModule;
import fi.konstal.bullet_your_life.dagger.module.RoomModule;

/**
 * Created by e4klehti on 8.4.2018.
 */

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
