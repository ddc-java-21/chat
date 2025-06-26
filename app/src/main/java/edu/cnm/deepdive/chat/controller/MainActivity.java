package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.databinding.ActivityMainBinding;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;
  private AppBarConfiguration appBarConfig;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUpUI();
    setUpNavigation();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfig);
  }

  private void setUpUI() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    EdgeToEdge.enable(this);
    setContentView(binding.getRoot());
  }

  private void setUpNavigation() {
    setSupportActionBar(binding.toolbar);
    appBarConfig = new AppBarConfiguration.Builder
        (R.id.pre_login_fragment, R.id.login_fragment, R.id.main_fragment)
        .build();
    NavHostFragment host = binding.navHostFragment.getFragment();
    navController = host.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
  }


}