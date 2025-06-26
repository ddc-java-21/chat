package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.chat.databinding.FragmentMainBinding;
import edu.cnm.deepdive.chat.viewmodel.LoginViewModel;

public class MainFragment extends Fragment implements MenuProvider {

  private FragmentMainBinding binding;
  private LoginViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentMainBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    viewModel
        .getAccount()
        .observe(getViewLifecycleOwner(), (account) -> {
          if (account == null) {
            Navigation.findNavController(binding.getRoot())
                .navigate(MainFragmentDirections.showPreLogin());
          }
        });
    requireActivity().addMenuProvider(this);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    return false;
  }

}
