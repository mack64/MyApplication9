package dk.subbox.myapplication.app.dagger.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.app.dagger.AppScope;
import dk.subbox.myapplication.ext.Login.LoginUserAdapterFactory;

@Module
public class GsonModule {

  @AppScope
  @Provides
  public Gson context() {
    return new GsonBuilder()
            .setDateFormat("dd-MM-yyyy'T'HH:mm:ss")
            .registerTypeAdapterFactory(LoginUserAdapterFactory.create())
        .create();
  }
}
