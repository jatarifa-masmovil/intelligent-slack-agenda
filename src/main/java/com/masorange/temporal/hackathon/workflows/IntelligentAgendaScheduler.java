package com.masorange.temporal.hackathon.workflows;

import com.masorange.temporal.hackathon.activities.MessagesActivities;
import com.masorange.temporal.hackathon.activities.OpenAIActivity;
import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.Task;
import com.masorange.temporal.hackathon.activities.model.TaskList;
import com.masorange.temporal.hackathon.activities.model.TaskResponse;
import com.masorange.temporal.hackathon.activities.model.TaskStatusEnum;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@WorkflowInterface
public interface IntelligentAgendaScheduler {

  @WorkflowMethod
  void summarizeSlackChannelConversations();

  @SignalMethod
  void processPendingTasks(TaskResponse taskResponse);

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
    private final MessagesActivities slackActivity =
        Workflow.newActivityStub(MessagesActivities.class, defaultActivityOptions);
    private final OpenAIActivity openAIActivity = Workflow.newActivityStub(OpenAIActivity.class,
        defaultActivityOptions);
    private final Map<Long, TaskStatusEnum> pendingStatuses = new HashMap<>();

    @Override
    @SneakyThrows
    public void summarizeSlackChannelConversations() {
      // Do something awesome
      ChannelMessages messages = slackActivity.retrieveMessages(OffsetDateTime.now().minusMinutes(10));
      var allMessages = String.join("\n", messages.messages().get("temporal-poc"));
      var tasks = openAIActivity.calculateTasks(allMessages).getTasks();
/*
      for (Task task : pendingtasks.getTasks()) {
        slackActivity.sendMessage("provision", task.getDescription());
      }

      initTaskStatus(pendingtasks);
      Workflow.await(() -> pendingStatuses.values().stream().allMatch(status -> status != TaskStatusEnum.PENDING));

      for (Task task : pendingtasks.getTasks()) {
        if (TaskStatusEnum.ACCEPTED.equals(pendingStatuses.get(task.getId()))) {
          slackActivity.createTask(task);
        }
      }
 */
      System.out.println("Messages from channel: " + new ObjectMapper().writeValueAsString(tasks));
    }

    private void initTaskStatus(TaskList taskList) {
      for (Task task : taskList.getTasks()) {
        this.pendingStatuses.put(task.getId(), TaskStatusEnum.PENDING);
      }
    }

    @Override
    public void processPendingTasks(TaskResponse taskResponse) {
      pendingStatuses.put(taskResponse.getId(), taskResponse.getTaskStatusEnum());
      log.info("Messages from channel: {}", pendingStatuses);
    }

  }
}
