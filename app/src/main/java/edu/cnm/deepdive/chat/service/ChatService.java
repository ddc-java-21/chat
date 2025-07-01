package edu.cnm.deepdive.chat.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.model.dto.Message;
import edu.cnm.deepdive.chat.model.dto.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/** @noinspection deprecation*/
@Singleton
public class ChatService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private final GoogleSignInService signInService;
  private final ChatServiceProxy proxy;
  private final ChatServiceLongPollingProxy longPollingProxy;
  private final SharedPreferences preferences;
  private final String historyCutoffKey;
  private final int historyCutoffDefault;
  private final Scheduler scheduler;
  private final Scheduler longPollingScheduler;

  private Subject<List<Message>> poll;

  @Inject
  ChatService(@ApplicationContext Context context,
      @NonNull GoogleSignInService signInService,
      @NonNull ChatServiceProxy proxy, @NonNull ChatServiceLongPollingProxy longPollingProxy) {
    this.signInService = signInService;
    this.proxy = proxy;
    this.longPollingProxy = longPollingProxy;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    historyCutoffKey = context.getString(R.string.history_cutoff_key);
    historyCutoffDefault = context.getResources().getInteger(R.integer.history_cutoff_default);
    scheduler = Schedulers.single();
    longPollingScheduler = Schedulers.single();
  }

  public Single<User> getMyProfile() {
    return getBearerToken()
        .flatMap(proxy::getMyProfile);
  }

  public Single<List<Channel>> getChannels() {
    return getBearerToken()
        .flatMap(proxy::getChannels);
  }

  public Observable<List<Message>> getMessages(Channel channel) {
    int historyCutoff = preferences.getInt(historyCutoffKey, historyCutoffDefault);
    Instant since = Instant.now().minus(historyCutoff, ChronoUnit.HOURS);
    return getMessagesSince(channel, since);
  }

  @SuppressLint("CheckResult")
  public Observable<List<Message>> getMessagesSince(Channel channel, Instant since) {
    Instant[] mostRecent = {since};
    poll = BehaviorSubject.createDefault(List.of());
    return poll
        .subscribeOn(scheduler)
        .doOnNext((List<Message> msgs) -> {
          msgs
              .stream()
              .reduce((msg1, msg2) -> msg2)
              .ifPresent((msg) -> mostRecent[0] = msg.getPosted());
          //noinspection ResultOfMethodCallIgnored
          getBearerToken()
              .observeOn(longPollingScheduler)
              .flatMap((token) ->
                  longPollingProxy.getMessagesSince(token, channel.getKey(), mostRecent[0]))
              .subscribe(poll::onNext);
        })
        .filter((msgs) -> !msgs.isEmpty());
  }

  public Single<Message> sendMessage(Channel channel, Message message) {
    return getBearerToken()
        .flatMap((token) -> proxy.sendMessage(token, channel.getKey(), message));
  }

  private Single<String> getBearerToken() {
    //noinspection ReactiveStreamsNullableInLambdaInTransform
    return signInService
        .refresh()
        .map(GoogleSignInAccount::getIdToken)
        .map((token) -> String.format(BEARER_TOKEN_FORMAT, token))
        .observeOn(scheduler);
  }

}
