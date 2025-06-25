package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.entity.User;
import edu.cnm.deepdive.chat.service.dao.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class UserService implements AbstractUserService {

  private final UserRepository repository;

  @Autowired
  UserService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User getOrAddUser(String oauthKey, User profile) {
    return repository
        .findByOauthKey(oauthKey)
        .or(() -> {
          User user = new User();
          user.setOauthKey(oauthKey);
          user.setDisplayName(profile.getDisplayName());
          user.setAvatar(profile.getAvatar());
          repository.save(user);
          return Optional.of(user);
        })
        .orElseThrow();
  }

  @Override
  public User getCurrentUser() {
    return (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
  }

  @Override
  public User getUser(User requestor, UUID key) {
    return repository
        .findByExternalKey(key)
        .orElseThrow(); // NoSuchElementException
  }

  @Override
  public User getMe(User requestor) {
    return requestor;
  }

  @Override
  public User updateMe(User requestor, User delta) {
    return repository
        .findById(requestor.getId()) // reloads user from the database
        .map((retrieved) -> { // retrieved = pre-updated object
          if (delta.getDisplayName() != null) {
            retrieved.setDisplayName(delta.getDisplayName());
          }
          if (delta.getAvatar() != null) {
            retrieved.setAvatar(delta.getAvatar());
          }
          return repository.save(retrieved); // save returns the updated object (and its type)
        }) // gets skipped if no value is present
        .orElseThrow();
  }

}
