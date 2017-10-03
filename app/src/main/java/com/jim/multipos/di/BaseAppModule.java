package com.jim.multipos.di;

import android.app.Application;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.data.db.AppDbHelper;
import com.jim.multipos.data.db.DbHelper;
import com.jim.multipos.data.db.DbOpenHelper;
import com.jim.multipos.data.prefs.AppPreferencesHelper;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.utils.AppConstants;
import com.jim.multipos.utils.RxBus;

import org.greenrobot.greendao.database.Database;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 27.07.2017.
 */
@Module
public class BaseAppModule {
    private final MultiPosApp app;

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    public BaseAppModule(MultiPosApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public DatabaseManager getDatabaseManager(Application application, PreferencesHelper preferencesHelper, DbHelper dbHelper) {
        return new DatabaseManager(application, preferencesHelper, dbHelper);
    }

    @Provides
    @Singleton
    public DbHelper getDbHelper(DbOpenHelper dbOpenHelper) {
        return new AppDbHelper(dbOpenHelper);
    }

    @Provides
    @Singleton
    public DbOpenHelper getDbOpenHelper(Application application) {
        return new DbOpenHelper(application, AppConstants.DB_NAME);
    }

    @Provides
    @Singleton
    public PreferencesHelper getSharedPreferences(Application application) {
        return new AppPreferencesHelper(application);
    }

    @Provides
    @Singleton
    public RxBus getRxBus() {
        return new RxBus();
    }
}
