package com.masorange.temporal.hackathon.workflows;

import com.masorange.temporal.hackathon.activities.LlmActivity;
import com.masorange.temporal.hackathon.activities.SlackActivity;
import com.masorange.temporal.hackathon.activities.model.ChannelMessages;
import com.masorange.temporal.hackathon.activities.model.PendingTask;
import com.masorange.temporal.hackathon.activities.model.PendingTasks;
import com.masorange.temporal.hackathon.activities.model.TaskResponse;
import com.masorange.temporal.hackathon.activities.model.TaskStatusEnum;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@WorkflowInterface
public interface IntelligentAgendaScheduler {

  @WorkflowMethod
  void summarizeSlackChannelConversations();

  @SignalMethod
  void processPendingTasks(TaskResponse taskResponse);

  @Slf4j
  class IntelligentAgendaSchedulerImpl implements IntelligentAgendaScheduler {

    private Map<String, TaskStatusEnum> pendingStatuses = new HashMap<>();


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

    private final LlmActivity llmActivity =
        Workflow.newActivityStub(LlmActivity.class, defaultActivityOptions);

    @Override
    public void summarizeSlackChannelConversations() {
      // Do something awesome
      ChannelMessages messages = channelMessages.retrieveMessages("provision", OffsetDateTime.now().minusDays(1));
      PendingTasks pendingtasks = llmActivity.generatePendingTasks(messages);

      for (PendingTask task : pendingtasks.messages()) {
        channelMessages.sendMessage("provision", task.getDescription());
      }

      initTaskStatus(pendingtasks);
      Workflow.await(() -> pendingStatuses.values().stream().allMatch(status -> status != TaskStatusEnum.PENDING));

      for (PendingTask task : pendingtasks.messages()) {
        if (TaskStatusEnum.ACCEPTED.equals(pendingStatuses.get(task.getId()))) {
          channelMessages.createTask(task);
        }
      }

      log.debug("Messages from channel: {}", messages);
    }

    private void initTaskStatus(PendingTasks pendingTasks) {
      for (PendingTask task : pendingTasks.messages()) {
        this.pendingStatuses.put(task.getId(), TaskStatusEnum.PENDING);
      }
    }

    @Override
    public void processPendingTasks(TaskResponse taskResponse) {
      pendingStatuses.put(taskResponse.getId(), taskResponse.getTaskStatusEnum());
    }

  }
}
