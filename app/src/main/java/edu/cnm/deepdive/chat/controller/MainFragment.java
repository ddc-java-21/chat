package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.chat.databinding.FragmentMainBinding;
import edu.cnm.deepdive.chat.viewmodel.LogInViewModel;

public class MainFragment extends Fragment {

  private FragmentMainBinding binding;
  private LogInViewModel viewModel;

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
    viewModel = new ViewModelProvider(requireActivity()).get(LogInViewModel.class);
    viewModel
        .getAccount()
        .observe(getViewLifecycleOwner(), (account) -> {
      if (account == null) {
        Navigation.findNavController(binding.getRoot())
            .navigate(MainFragmentDirections.showPreLogin());
      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
