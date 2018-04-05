package dk.subbox.myapplication.app.dagger.module;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.R;
import dk.subbox.myapplication.app.dagger.AppScope;
import dk.subbox.myapplication.app.network.AuthNetwork;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
  public OkHttpClient okHttpClient(Interceptor interceptor, Cache cache, CertificatePinner certificatePinner) {
    return new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .certificatePinner(certificatePinner)
        .cache(cache)
        .build();
  }

  @AppScope
  @Provides
  public CertificatePinner certificatePinner(){
      return new CertificatePinner.Builder()
              .add("subbox.dk","sha256/d0OKwbnzi+LFI7A34cBUdAaMVCYqZod1Kdu3wWWedqc=")
              .add("*.one.com","sha256/d0OKwbnzi+LFI7A34cBUdAaMVCYqZod1Kdu3wWWedqc=")
              .add("one.com","sha256/d0OKwbnzi+LFI7A34cBUdAaMVCYqZod1Kdu3wWWedqc=")
              .build();
  }

  @AppScope
  @Provides
  public Interceptor interceptor(){
    return new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Timber.i(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        //
        Timber.i("Response Body: " + response.body().string());
        //

        long t2 = System.nanoTime();
        Timber.i(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
      }
    };
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
  public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson, Context context) {
    return new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(context.getResources().getString(R.string.base_URL))
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
