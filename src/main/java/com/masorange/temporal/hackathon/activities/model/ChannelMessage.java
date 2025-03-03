package com.masorange.temporal.hackathon.activities.model;

public class ChannelMessage {

  private final String author;
  private final String content;

  private ChannelMessage(Builder builder) {
    author = builder.author;
    content = builder.content;
  }

  @Override
  public String toString() {
    return "DeviceInfoResponse{" +
        "author='" + author + '\'' +
        ", content='" + content + '\'' +
        '}';
  }

  public String getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public static final class Builder {

    private String author;
    private String content;

    public Builder() {
    }

    public Builder setAuthor(String author) {
      this.author = author;
      return this;
    }

    public Builder setContent(String content) {
      this.content = content;
      return this;
    }

    public ChannelMessage build() {
      return new ChannelMessage(this);
    }
  }
}

