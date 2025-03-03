package com.masorange.temporal.hackathon.activities;

import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.TaskList;
import io.temporal.activity.ActivityInterface;
import java.time.OffsetDateTime;

@ActivityInterface
public interface MessagesActivities {

	ChannelMessages retrieveMessages(OffsetDateTime fromDate);

	void createTasksList(TaskList taskList);

}
