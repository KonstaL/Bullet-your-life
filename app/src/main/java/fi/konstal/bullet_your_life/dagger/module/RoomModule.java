package fi.konstal.bullet_your_life.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fi.konstal.bullet_your_life.data.AppDatabase;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCardDao;

/**
 * Created by e4klehti on 8.4.2018.
 */

@Module
public class RoomModule {

    private AppDatabase appDatabase;

    public RoomModule(Application mApplication) {
        appDatabase = Room.databaseBuilder(mApplication, AppDatabase.class, "DayCard-db").build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    DayCardDao providesDayCardDao(AppDatabase appDatabase) {
        return appDatabase.getDayCardDao();
    }

    @Singleton
    @Provides
    CardRepository cardRepository(DayCardDao dayCardDao, Executor executor) {
        return new CardRepository(dayCardDao, executor);
    }

    @Singleton
    @Provides
    //@Named("MultiThread")
    public Executor provideMultiThreadExecutor() {
        return Executors.newCachedThreadPool();
    }


}