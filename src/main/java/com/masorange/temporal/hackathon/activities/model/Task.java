package com.masorange.temporal.hackathon.activities.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  public enum Priorite {
    HIGH,
    MEDIUM,
    LOW
  }

  public enum Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED
  }

  private long id;
  private String description;
  private Priorite priority;
  private Status status;
}
