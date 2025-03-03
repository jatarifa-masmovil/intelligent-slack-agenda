package com.masorange.temporal.hackathon.activities;

import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.PendingTasks;
import io.temporal.activity.ActivityInterface;
import java.util.List;

@ActivityInterface
public interface LlmActivity {

  PendingTasks generatePendingTasks(ChannelMessages channelMessages);

  class LlmActivityImpl implements LlmActivity {

    @Override
    public PendingTasks generatePendingTasks(ChannelMessages channelMessages) {
      return new PendingTasks(List.of());
    }
  }
}
