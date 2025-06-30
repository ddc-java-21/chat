package edu.cnm.deepdive.chat.hilt;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.service.ChatServiceLongPollingProxy;
import edu.cnm.deepdive.chat.service.ChatServiceProxy;
import java.time.Duration;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ChatServiceProxyModule {

  public static final Duration MAX_CALL_DURATION = Duration.ofSeconds(30);

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        // TODO: 6/30/2025 Register type adapters as necessary.
        .create();
  }

  @Provides
  @Singleton
  Interceptor provideInterceptor(@ApplicationContext Context context) {
    return new HttpLoggingInterceptor()
        .setLevel(Level.valueOf(
                context.getString(R.string.log_level).toUpperCase()));
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient(Interceptor interceptor) {
    return new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();
  }

  @Provides
  @Singleton
  ChatServiceProxy provideProxy(@ApplicationContext Context context, Gson gson, Interceptor interceptor) {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();
    return new Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(context.getString(R.string.base_Url))
        .build()
        .create(ChatServiceProxy.class);
  }

  @Provides
  @Singleton
  ChatServiceLongPollingProxy provideLongPollingProxy(
      @ApplicationContext Context context, Gson gson, Interceptor interceptor) {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(Duration.ZERO)
        .writeTimeout(Duration.ZERO)
        .readTimeout(Duration.ZERO)
        .callTimeout(MAX_CALL_DURATION)
        .build();
    return new Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(context.getString(R.string.base_Url))
        .build()
        .create(ChatServiceLongPollingProxy.class);
  }

}
