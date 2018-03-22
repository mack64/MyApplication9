package dk.subbox.myapplication.app.network;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import dk.subbox.myapplication.BuildConfig;
import dk.subbox.myapplication.app.dagger.AppComponent;
import dk.subbox.myapplication.app.dagger.DaggerAppComponent;
import dk.subbox.myapplication.app.dagger.module.AppModule;
import timber.log.Timber;

/**
 * Created by mmpa6 on 15-Mar-18.
 */

public class AuthApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static AuthApplication get(Activity activity){
        return (AuthApplication) activity.getApplication();
    }

    public static AuthApplication get(Service service){
        return (AuthApplication) service.getApplication();
    }

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree(){
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    super.log(priority, tag, message, t);
                }
            });
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent component(){return appComponent;}

}
