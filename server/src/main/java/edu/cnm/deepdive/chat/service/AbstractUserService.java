package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.entity.User;
import java.util.UUID;

public interface AbstractUserService {

  User getOrAddUser(String oauthKey, String displayName, User profile);

  User getCurrentUser();

  User getUser(User requestor, UUID key);

  User getMe(User requestor);

  User updateMe(User requestor, User delta);

}
