package edu.cnm.deepdive.chat.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
    name = "user_profile"
//    indexes = {
//        @Index(columnList = "created")
//    }
)
public class User {

  @Id
  @Column(name = "user_profile_id", nullable = false, updatable = false)
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  private UUID externalKey;

  @Column(nullable = false, updatable = false, length = 30, unique = true)
  private String oauthKey;

  @Column(nullable = false, updatable = true, length = 30)
  private String displayName;
  
  @Column(nullable = true, updatable = true)
  private URL avatar;
  
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Instant created;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getOauthKey() {
    return oauthKey;
  }

  public User setOauthKey(String oauthKey) {
    this.oauthKey = oauthKey;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public User setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public URL getAvatar() {
    return avatar;
  }

  public User setAvatar(URL avatar) {
    this.avatar = avatar;
    return this;
  }

  public Instant getCreated() {
    return created;
  }

  // TODO: 6/24/2025 Implement hashcode and equals
  @PrePersist
  void generateFieldsValues() {
    externalKey = UUID.randomUUID();
  }
  
}
