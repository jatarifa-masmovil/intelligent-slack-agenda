package com.masorange.temporal.hackathon.activities.model;

public class TaskResponse {

  private Long id;
  private TaskStatusEnum taskStatusEnum;

  private TaskResponse(Builder builder) {
    id = builder.id;
    taskStatusEnum = builder.taskStatusEnum;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTaskStatusEnum(TaskStatusEnum taskStatusEnum) {
    this.taskStatusEnum = taskStatusEnum;
  }

  public TaskResponse() {
  }

  @Override
  public String toString() {
    return "DeviceInfoResponse{" +
        "id='" + id + '\'' +
        ", taskStatusEnum='" + taskStatusEnum + '\'' +
        '}';
  }

  public Long getId() {
    return id;
  }

  public TaskStatusEnum getTaskStatusEnum() {
    return taskStatusEnum;
  }

  public static final class Builder {

    private Long id;
    private TaskStatusEnum taskStatusEnum;

    public Builder() {
    }

    public Builder setId(Long id) {
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

