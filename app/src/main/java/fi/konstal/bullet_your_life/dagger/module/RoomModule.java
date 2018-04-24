package fi.konstal.bullet_your_life.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fi.konstal.bullet_your_life.data.AppDatabase;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCardDao;
import fi.konstal.bullet_your_life.data.NoteCardDao;


/**
 * Gives Dagger Room related objects
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
@Module
public class RoomModule {

    private AppDatabase appDatabase;

    /**
     * Constructor
     *
     * @param mApplication needed for initialization
     */
    public RoomModule(Application mApplication) {
        appDatabase = Room.databaseBuilder(mApplication, AppDatabase.class, "DayCard-db").build();
    }

    /**
     * Proveides AppDatabase to dagger
     *
     * @return the appDatabase
     */
    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    /**
     * Provides DayCardDao singleton to Dagger
     *
     * @param appDatabase automatically injectec by dagger
     * @return DayCard DAO
     */
    @Singleton
    @Provides
    DayCardDao providesDayCardDao(AppDatabase appDatabase) {
        return appDatabase.getDayCardDao();
    }

    /**
     * Provides NoteCardDao singleton to Dagger
     *
     * @param appDatabase automatically injected by dagger
     * @return NoteCard DAO
     */
    @Singleton
    @Provides
    NoteCardDao providesNoteCardDao(AppDatabase appDatabase) {
        return appDatabase.getNoteCardDao();
    }

    /**
     * Provides {@link CardRepository} singleton to Dagger
     *
     * @param noteCardDao automatically injected by dagger
     * @param dayCardDao  automatically injected by dagger
     * @param executor    automatically injected by dagger
     * @return The CardRepository
     */
    @Singleton
    @Provides
    CardRepository cardRepository(NoteCardDao noteCardDao, DayCardDao dayCardDao, Executor executor) {
        return new CardRepository(noteCardDao, dayCardDao, executor);
    }

    /**
     * Used in the future
     */
    /*   @Singleton
       @Provides
       @Named("MultiThread")
       public Executor provideMultiThreadExecutor() {
           return Executors.newCachedThreadPool();
       }
   */

    /**
     * Provides a singleTheaded executor for Dagger
     *
     * @return the executor
     */
    @Singleton
    @Provides
    //@Named("SingleThread")
    public Executor provideSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}