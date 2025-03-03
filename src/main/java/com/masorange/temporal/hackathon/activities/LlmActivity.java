package com.masorange.temporal.hackathon.activities;

import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.PendingTask;
import com.masorange.temporal.hackathon.activities.model.PendingTasks;
import io.temporal.activity.ActivityInterface;
import java.util.List;

@ActivityInterface
public interface LlmActivity {

  PendingTasks generatePendingTasks(ChannelMessages channelMessages);

  class LlmActivityImpl implements LlmActivity {

    @Override
    public PendingTasks generatePendingTasks(ChannelMessages channelMessages) {
      return new PendingTasks(List.of(
          new PendingTask.Builder().setId("id1").setDescription("desc1").setPriority("1").build(),
          new PendingTask.Builder().setId("id2").setDescription("desc2").setPriority("2").build()
      ));
    }
  }
}
