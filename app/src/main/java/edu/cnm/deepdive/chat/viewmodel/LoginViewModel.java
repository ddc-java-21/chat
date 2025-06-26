package edu.cnm.deepdive.chat.viewmodel;

import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.chat.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javax.inject.Inject;

/** @noinspection deprecation*/
@HiltViewModel
public class LoginViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = LoginViewModel.class.getSimpleName();

  private final GoogleSignInService service;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> refreshThrowable;
  private final MutableLiveData<Throwable> signInThrowable;
  private final CompositeDisposable pending;

  @Inject
  LoginViewModel(GoogleSignInService service) {
    this.service = service;
    account = new MutableLiveData<>();
    refreshThrowable = new MutableLiveData<>();
    signInThrowable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<Throwable> getSignInThrowable() {
    return signInThrowable;
  }

  public LiveData<Throwable> getRefreshThrowable() {
    return refreshThrowable;
  }

  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  public void refresh() {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service
        .refresh()
        .subscribe(
            account::postValue,
            (throwable) -> postThrowable(throwable, refreshThrowable),
            pending // subscribe method will not only turn the power on, it will take the claim check and put it into that pending bucket
        );
  }

  public void startSignIn(@NonNull ActivityResultLauncher<Intent> launcher) {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service.startSignIn(launcher);
  }

  public void completeSignIn(@NonNull ActivityResult result) { // invoked from the UI (could be when the view is created, or could be invoked from sign-in button from UI) --> UI thread
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    service
        .completeSignIn(result)
        .subscribe(
            account::postValue,          // since I don't know it's on the UI thread, we must use postValue
            (throwable) -> postThrowable(throwable, signInThrowable),
            pending
        );
  }

  public void signOut() {
    refreshThrowable.setValue(null);
    signInThrowable.setValue(null);
    Disposable disposable = service
        .signOut()
        .doFinally(() -> account.postValue(null)) // doesn't happen in order: doFinally doesn't execute until the stream is done
        .doOnError((throwable) -> postThrowable(throwable, signInThrowable))
        .subscribe();
    pending.add(disposable);
  }

  private void postThrowable(
      @NonNull Throwable throwable, @NonNull MutableLiveData<Throwable> target) {
    Log.e(TAG, throwable.getMessage(), throwable); // log, then put into live data
    target.postValue(throwable);
  }

}
