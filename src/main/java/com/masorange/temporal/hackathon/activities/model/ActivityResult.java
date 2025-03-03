package com.masorange.temporal.hackathon.activities.model;

public class ActivityResult {

  private final String result;

  @Override
  public String toString() {
    return "DeviceInfoResponse{" +
        "result='" + result + '\'' +
        '}';
  }

  public String getResult() {
    return result;
  }

  public ActivityResult(String result) {
    this.result = result;
  }
}

