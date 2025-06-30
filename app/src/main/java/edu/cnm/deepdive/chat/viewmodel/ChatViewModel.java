package edu.cnm.deepdive.chat.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.model.dto.User;
import edu.cnm.deepdive.chat.service.ChatService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class ChatViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = ChatViewModel.class.getSimpleName();
  private final ChatService chatService;
  private final MutableLiveData<User> currentUser;
  private final MutableLiveData<List<Channel>> channels;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public ChatViewModel(ChatService chatService) {
    this.chatService = chatService;
    currentUser = new MutableLiveData<>();
    channels = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchCurrentUser();
    fetchChannels();
  }

  public LiveData<User> getCurrentUser() {
    return currentUser;
  }

  public LiveData<List<Channel>> getChannels() {
    return channels;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  private void fetchCurrentUser() {
    throwable.setValue(null);
    chatService
        .getMyProfile()
        .subscribe(
            currentUser::postValue,
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  private void fetchChannels() {
    throwable.setValue(null);
    chatService
        .getChannels()
        .subscribe(
            channels::postValue,
            this::postThrowable,
            pending
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
