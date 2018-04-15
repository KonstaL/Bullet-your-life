package fi.konstal.bullet_your_life.dagger.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import fi.konstal.bullet_your_life.activities.BaseActivity;
import fi.konstal.bullet_your_life.activities.EditCardActivity;
import fi.konstal.bullet_your_life.activities.LogsActivity;
import fi.konstal.bullet_your_life.dagger.module.AppModule;
import fi.konstal.bullet_your_life.dagger.module.RoomModule;
import fi.konstal.bullet_your_life.data.AppDatabase;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCardDao;
import fi.konstal.bullet_your_life.fragment.FutureLog;
import fi.konstal.bullet_your_life.fragment.MonthlyLog;
import fi.konstal.bullet_your_life.fragment.WeeklyLog;

/**
 * Created by e4klehti on 8.4.2018.
 */

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    void inject(LogsActivity activity);
    void inject(EditCardActivity activity);
    void inject(BaseActivity activity);
    void inject(WeeklyLog fragment);
    void inject(FutureLog fragment);
    void inject(MonthlyLog fragment);

    DayCardDao dayCardDao();

    AppDatabase appDatabase();

    CardRepository cardRepository();

    Application application();

}