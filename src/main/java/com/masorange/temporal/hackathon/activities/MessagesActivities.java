package com.masorange.temporal.hackathon.activities;

import com.masorange.temporal.hackathon.activities.model.ChannelMessages;

import java.time.OffsetDateTime;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface MessagesActivities {

  ChannelMessages retrieveMessages(OffsetDateTime fromDate);

}
