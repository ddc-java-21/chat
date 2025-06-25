package edu.cnm.deepdive.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class Channel {

  @Id
  @GeneratedValue
  @Column(name = "channel_id", nullable = false, updatable = false)
  @JsonIgnore
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  @JsonProperty(value = "key", access = Access.READ_ONLY)
  private UUID externalKey;

  @Column(nullable = false, updatable = true, unique = true, length = 30)
  private String title;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private Instant created;

  @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,orphanRemoval = true)
  @JsonIgnore
  private final List<Message> messages = new LinkedList<>();

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getTitle() {
    return title;
  }

  public Channel setTitle(String title) {
    this.title = title;
    return this;
  }

  public Instant getCreated() {
    return created;
  }

  public List<Message> getMessages() {
    return messages;
  }

  // TODO: 6/24/25 Implement hashCode and equals.

  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
