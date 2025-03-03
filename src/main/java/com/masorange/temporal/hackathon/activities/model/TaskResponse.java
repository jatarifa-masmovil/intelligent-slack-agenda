package com.masorange.temporal.hackathon.activities.model;

public class TaskResponse {

  private final String id;
  private final TaskStatusEnum taskStatusEnum;

  private TaskResponse(Builder builder) {
    id = builder.id;
    taskStatusEnum = builder.taskStatusEnum;
  }

  @Override
  public String toString() {
    return "DeviceInfoResponse{" +
        "id='" + id + '\'' +
        ", taskStatusEnum='" + taskStatusEnum + '\'' +
        '}';
  }

  public String getId() {
    return id;
  }

  public TaskStatusEnum getTaskStatusEnum() {
    return taskStatusEnum;
  }

  public static final class Builder {

    private String id;
    private TaskStatusEnum taskStatusEnum;

    public Builder() {
    }

    public Builder setId(String id) {
      this.id = id;
      return this;
    }

    public Builder setTaskStatusEnum(TaskStatusEnum taskStatusEnum) {
      this.taskStatusEnum = taskStatusEnum;
      return this;
    }


    public TaskResponse build() {
      return new TaskResponse(this);
    }
  }
}

