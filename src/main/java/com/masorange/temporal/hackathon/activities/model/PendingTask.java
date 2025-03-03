package com.masorange.temporal.hackathon.activities.model;

public class PendingTask {

  private final String id;
  private final String description;
  private final String priority;

  private PendingTask(Builder builder) {
    id = builder.id;
    description = builder.description;
    priority = builder.priority;
  }

  @Override
  public String toString() {
    return "DeviceInfoResponse{" +
        "id='" + id + '\'' +
        ", description='" + description + '\'' +
        ", priority='" + priority + '\'' +
        '}';
  }

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getPriority() {
    return priority;
  }

  public static final class Builder {

    private String id;
    private String description;
    private String priority;

    public Builder() {
    }

    public Builder setId(String id) {
      this.id = id;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setPriority(String priority) {
      this.priority = priority;
      return this;
    }


    public PendingTask build() {
      return new PendingTask(this);
    }
  }
}

