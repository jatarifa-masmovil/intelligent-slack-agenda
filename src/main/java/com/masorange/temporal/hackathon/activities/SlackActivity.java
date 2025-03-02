package com.masorange.temporal.hackathon.activities;

import java.time.OffsetDateTime;
import java.util.List;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface SlackActivity {

  ChannelMessages retrieveMessages(String channelId, OffsetDateTime fromDate);

  class SlackActivityImpl implements SlackActivity {

    @Override
    public ChannelMessages retrieveMessages(String channelId, OffsetDateTime fromDate) {
      return new ChannelMessages(List.of());
    }
  }
}
