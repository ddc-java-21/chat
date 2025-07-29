package edu.cnm.deepdive.chat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class ZenQuote {

  @JsonProperty(value = "q", access = Access.WRITE_ONLY)
  private String quote;

  @JsonProperty(value = "a", access = Access.WRITE_ONLY)
  private String author;

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

}
