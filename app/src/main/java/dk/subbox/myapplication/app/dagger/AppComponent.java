package dk.subbox.myapplication.app.dagger;

import android.content.Context;

import dagger.Component;
import dk.subbox.myapplication.app.dagger.module.AppModule;
import dk.subbox.myapplication.app.dagger.module.GsonModule;
import dk.subbox.myapplication.app.dagger.module.NetworkModule;
import dk.subbox.myapplication.app.network.AuthNetwork;
import okhttp3.OkHttpClient;

@AppScope
@Component(modules = { AppModule.class, NetworkModule.class, GsonModule.class})
public interface AppComponent {

  Context context();

  AuthNetwork authNetwork();

}
