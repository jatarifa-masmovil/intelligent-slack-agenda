package com.masorange.temporal.hackathon.activities;

import com.masorange.temporal.hackathon.activities.model.ActivityResult;
import com.masorange.temporal.hackathon.activities.model.ChannelMessage;
import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.Task;
import io.temporal.activity.ActivityInterface;
import java.time.OffsetDateTime;
import java.util.List;

@ActivityInterface
public interface SlackActivity {

  ChannelMessages retrieveMessages(String channelId, OffsetDateTime fromDate);

  ActivityResult sendMessage(String channelId, String message);

  ActivityResult createTask(Task pendingTask);

  class SlackActivityImpl implements SlackActivity {

    @Override
    public ChannelMessages retrieveMessages(String channelId, OffsetDateTime fromDate) {
      return new ChannelMessages(List.of(
          new ChannelMessage.Builder().setAuthor("author").setContent("content").build(),
          new ChannelMessage.Builder().setAuthor("author2").setContent("content2").build())
      );
    }

    @Override
    public ActivityResult sendMessage(String channelId, String message) {
      return new ActivityResult("OK");
    }

    @Override
    public ActivityResult createTask(Task pendingTask) {
      return new ActivityResult("OK");
    }
  }
}
