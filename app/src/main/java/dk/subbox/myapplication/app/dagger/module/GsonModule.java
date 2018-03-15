package dk.subbox.myapplication.app.dagger.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.widget.RxAdapter;

import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.app.dagger.AppScope;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class GsonModule {

  @AppScope
  @Provides
  public Gson context() {
    return new GsonBuilder()
            .setDateFormat("dd-MM-yyyy'T'HH:mm:ss")
        .create();
  }
}
