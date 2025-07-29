package edu.cnm.deepdive.chat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZenQuote {

  @JsonProperty("q")
  private String quote;

  @JsonProperty("a")
  private String author;

  public String getQuote() {
    return quote;
  }

  public ZenQuote setQuote(String quote) {
    this.quote = quote;
    return this;
  }

  public String getAuthor() {
    return author;
  }

  public ZenQuote setAuthor(String author) {
    this.author = author;
    return this;
  }
}
