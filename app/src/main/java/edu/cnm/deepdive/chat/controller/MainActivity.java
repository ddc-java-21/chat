package edu.cnm.deepdive.chat.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActivityNavigator.Extras;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.MainNavGraphDirections;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.databinding.ActivityMainBinding;
import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.viewmodel.ChatViewModel;
import edu.cnm.deepdive.chat.viewmodel.LoginViewModel;
import java.util.List;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();
  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;
  private LoginViewModel loginViewModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupNavigation();
    setupViewModel();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }

  private void setupUI() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    binding.navDrawer
        .getMenu()
        .findItem(R.id.sign_out)
        .setOnMenuItemClickListener((item) -> {
          loginViewModel.signOut();
          return true;
        });
    // TODO: 6/30/2025 Attach listeners to UI widgets.
    setContentView(binding.getRoot());
  }

  private void setupNavigation() {
    setSupportActionBar(binding.appBarMain.toolbar);
    NavigationView navigationView = binding.navDrawer;
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.main_fragment, R.id.channel_fragment)
        .setOpenableLayout(binding.getRoot())
        .build();
    navController = ((NavHostFragment) (binding.appBarMain.navHostFragment.getFragment()))
        .getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
  }

  private void setupViewModel() {
    ViewModelProvider provider = new ViewModelProvider(this);
    loginViewModel = provider.get(LoginViewModel.class);
    Lifecycle lifecycle = getLifecycle();
    lifecycle.addObserver(loginViewModel);
    loginViewModel
        .getAccount()
        .observe(this, this::handleAccount);
    ChatViewModel chatViewModel = provider.get(ChatViewModel.class);
    lifecycle.addObserver(chatViewModel);
    chatViewModel
        .getCurrentUser()
        .observe(this, (user) -> {
          // TODO: 7/1/2025 Personalize UI for user. 
        });
    chatViewModel
        .getChannels()
        .observe(this, this::handleChannels);
  }

  private void handleChannels(List<Channel> channels) {
    Menu menu = binding.navDrawer.getMenu();
    menu.removeGroup(R.id.channels);
    channels.forEach((channel) -> menu
        .add(R.id.channels, Menu.NONE, 1, channel.getTitle())
        .setCheckable(true)
        .setOnMenuItemClickListener((item) -> selectChannel(channel, item)));
  }

  private boolean selectChannel(Channel channel, MenuItem item) {
    item.setChecked(true);
    navController.navigate(MainNavGraphDirections.showChannel(channel));
    binding.getRoot().closeDrawer(GravityCompat.START);
    return true;
  }

  /**
   * @noinspection deprecation
   */
  private void handleAccount(GoogleSignInAccount account) {
    if (account == null) {
      Extras extras = new Extras.Builder()
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
          .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          .build();
      navController.navigate(MainNavGraphDirections.showLogin());
    }
  }
}
