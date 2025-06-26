package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class PreLoginFragment extends Fragment {

  private View root;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.fragment_pre_login, container, false);
    return root; // layout has nothing in it --> we are inflating it and returning it; BUT if we returned null, onViewCreated would never be called :D
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LoginViewModel viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner(); // whenever we are going to observe live data in an activity
    viewModel
        .getAccount()
        .observe(owner, (account) -> {
          if (account != null) {
            Navigation.findNavController(root) // DONE: 6/23/25 Navigate to ListFragment.
                .navigate(PreLoginFragmentDirections.showList());
          }
        });
    viewModel
        .getRefreshThrowable()
        .observe(owner, (throwable) -> {
          if (throwable != null) {
            Navigation.findNavController(root) // DONE: 6/23/25 Navigate to LoginFragment.
                .navigate(PreLoginFragmentDirections.showLogin());
          }
        });
    viewModel.refresh();
  }

  @Override
  public void onDestroyView() {
    root = null; // just in fragment you need to assign widgets null
    super.onDestroyView();
  }

}
