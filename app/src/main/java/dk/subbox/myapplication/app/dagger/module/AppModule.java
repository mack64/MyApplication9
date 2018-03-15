package dk.subbox.myapplication.app.dagger.module;

import android.app.Application;
import android.content.Context;


import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.app.dagger.AppScope;

@Module
public class AppModule {

  private final Context context;

  public AppModule(Application application) {
    this.context = application.getApplicationContext();
  }

  @AppScope
  @Provides
  public Context context() {
    return context;
  }

}
