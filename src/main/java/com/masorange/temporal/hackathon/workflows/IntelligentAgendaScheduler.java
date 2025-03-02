package com.masorange.temporal.hackathon.workflows;

import com.masorange.temporal.hackathon.activities.SlackActivity;

import java.time.Duration;
import java.time.OffsetDateTime;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import lombok.extern.slf4j.Slf4j;

@WorkflowInterface
public interface IntelligentAgendaScheduler {

  @WorkflowMethod
  void summarizeSlackChannelConversations();

  @Slf4j
  class IntelligentAgendaSchedulerImpl implements IntelligentAgendaScheduler {

    private final RetryOptions retryoptions = RetryOptions.newBuilder()
        .setInitialInterval(Duration.ofSeconds(1))
        .setMaximumInterval(Duration.ofSeconds(20))
        .setBackoffCoefficient(2)
        .setMaximumAttempts(5000)
        .build();

    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
        .setRetryOptions(retryoptions)
        .setStartToCloseTimeout(Duration.ofSeconds(2))
        .setScheduleToCloseTimeout(Duration.ofSeconds(5000))
        .build();

    private final SlackActivity channelMessages =
        Workflow.newActivityStub(SlackActivity.class, defaultActivityOptions);

    @Override
    public void summarizeSlackChannelConversations() {
      // Do something awesome
      var messages = channelMessages.retrieveMessages("provision", OffsetDateTime.now().minusDays(1));
      log.debug("Messages from channel: {}", messages);
    }
  }
}
