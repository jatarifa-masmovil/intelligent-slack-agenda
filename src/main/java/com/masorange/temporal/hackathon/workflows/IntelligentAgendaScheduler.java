package com.masorange.temporal.hackathon.workflows;

import com.masorange.temporal.hackathon.activities.MessagesActivities;
import com.masorange.temporal.hackathon.activities.OpenAIActivity;
import com.masorange.temporal.hackathon.activities.model.Task;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@WorkflowInterface
public interface IntelligentAgendaScheduler {

  @WorkflowMethod
  List<Task> summarizeSlackChannelConversations();

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

    private final MessagesActivities channelMessages =
        Workflow.newActivityStub(MessagesActivities.class, defaultActivityOptions);

    private final OpenAIActivity openAIActivity = Workflow.newActivityStub(OpenAIActivity.class,
        defaultActivityOptions);

    @Override
    @SneakyThrows
    public List<Task> summarizeSlackChannelConversations() {
      var messages = channelMessages.retrieveMessages(OffsetDateTime.now().minusDays(1));
      var allMessages = messages.messages().get("temporal-poc").stream().collect(Collectors.joining("\n"));
      var tasks = openAIActivity.calculateTasks(allMessages).getTasks();
      System.out.println(new ObjectMapper().writeValueAsString(tasks));
      return tasks;
    }
  }
}
