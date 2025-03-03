package com.masorange.temporal.hackathon.activities.model;

public class ActivityResult {

  private String result;

  public void setResult(String result) {
    this.result = result;
  }

  public ActivityResult() {
  }

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

