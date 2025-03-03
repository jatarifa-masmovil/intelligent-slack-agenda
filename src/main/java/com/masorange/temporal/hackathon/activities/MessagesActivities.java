package com.masorange.temporal.hackathon.activities;

import io.temporal.activity.ActivityInterface;
import java.time.OffsetDateTime;

@ActivityInterface
public interface MessagesActivities {

	ChannelMessages retrieveMessages(OffsetDateTime fromDate);

}
