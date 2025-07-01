package edu.cnm.deepdive.chat.model.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;

public class User {

  private static final String TO_STRING_FORMAT = 
      "%1$s{key=%2$s, displayName=%3$s, avatar=%4$s, created=%5$s}";

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

  @Override
  public int hashCode() {
    //noinspection DataFlowIssue
    return key.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof User other) {
      //noinspection ConstantValue,DataFlowIssue
      comparison = this.key.equals(other.key);
    } else {
      comparison = false;
    }
    return comparison;
  }

  @NonNull
  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT, 
        getClass().getSimpleName(), key, displayName, avatar, created);
  }

}
