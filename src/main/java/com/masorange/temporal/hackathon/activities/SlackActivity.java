package com.masorange.temporal.hackathon.activities;

import com.masorange.temporal.hackathon.activities.model.ActivityResult;
import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.PendingTask;
import io.temporal.activity.ActivityInterface;
import java.time.OffsetDateTime;
import java.util.List;

@ActivityInterface
public interface SlackActivity {

  ChannelMessages retrieveMessages(String channelId, OffsetDateTime fromDate);

  ActivityResult sendMessage(String channelId, String message);

  ActivityResult createTask(PendingTask pendingTask);

  class SlackActivityImpl implements SlackActivity {

    @Override
    public ChannelMessages retrieveMessages(String channelId, OffsetDateTime fromDate) {
      return new ChannelMessages(List.of());
    }

    @Override
    public ActivityResult sendMessage(String channelId, String message) {
      return new ActivityResult("OK");
    }

    @Override
    public ActivityResult createTask(PendingTask pendingTask) {
      return new ActivityResult("OK");
    }
  }
}
