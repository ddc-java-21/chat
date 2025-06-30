package edu.cnm.deepdive.chat.model.dto;

import com.google.gson.annotations.Expose;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;

public class User {

  @Expose(serialize = false)
  private final UUID key = null;

  @Expose(serialize = false)
  private final Instant created = null;

  @Expose
  private String displayName;

  @Expose
  private URL avatar;

  public UUID getKey() {
    return key;
  }

  public Instant getCreated() {
    return created;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public URL getAvatar() {
    return avatar;
  }

  public void setAvatar(URL avatar) {
    this.avatar = avatar;
  }
}
