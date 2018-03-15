package dk.subbox.myapplication.app.dagger.module;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import java.io.File;

import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.app.dagger.AppScope;
import dk.subbox.myapplication.app.network.AuthNetwork;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class NetworkModule {

  @AppScope
  @Provides
  public Cache cache(Context context) {
    return new Cache(new File(context.getCacheDir(),""),
        10 * 1024 * 1024);
  }

  @AppScope
  @Provides
  public OkHttpClient okHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache) {
    return new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build();
  }

  @AppScope
  @Provides
  public HttpLoggingInterceptor httpLoggingInterceptor() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
        message -> Timber.d(message));
    return logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
  }

  @AppScope
  @Provides
  public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {
    return new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.github.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @AppScope
  @Provides
  public Picasso picasso(Context context, OkHttpClient okHttpClient) {
    return new Picasso.Builder(context)
        .downloader(new OkHttp3Downloader(okHttpClient))
        .build();
  }

  @AppScope
  @Provides
  public AuthNetwork authNetwork(Retrofit retrofit){
    return retrofit.create(AuthNetwork.class);
  }

}
