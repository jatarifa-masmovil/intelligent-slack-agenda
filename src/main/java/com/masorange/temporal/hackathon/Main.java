package com.masorange.temporal.hackathon;

import com.masorange.temporal.hackathon.activities.OpenAIActivity.OpenAIActivityImpl;
import com.masorange.temporal.hackathon.activities.messages.SlackActivitiesImpl;
import com.masorange.temporal.hackathon.workflows.IntelligentAgendaScheduler;
import com.masorange.temporal.hackathon.workflows.IntelligentAgendaScheduler.IntelligentAgendaSchedulerImpl;

import com.slack.api.Slack;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

  public static void main(String[] args) {
    var serviceStub = WorkflowServiceStubs.newLocalServiceStubs();
    var client = WorkflowClient.newInstance(serviceStub);
    var factory = WorkerFactory.newInstance(client);
    var worker = factory.newWorker("agenda-tasklisk");

    worker.registerWorkflowImplementationTypes(IntelligentAgendaSchedulerImpl.class);
    worker.registerActivitiesImplementations(new SlackActivitiesImpl(Slack.getInstance()), new OpenAIActivityImpl());
    factory.start();

    var options = WorkflowOptions.newBuilder()
        .setTaskQueue("agenda-tasklisk")
        .setWorkflowId("IntelligentAgendaScheduler")
        .setCronSchedule("*/1 * * * *")  // Each minute
        .build();
    var workflow = client.newWorkflowStub(IntelligentAgendaScheduler.class, options);
    try {
      var execution = WorkflowClient.start(workflow::summarizeSlackChannelConversations);
      log.info("Execution: {}", execution);
    } catch (WorkflowExecutionAlreadyStarted e) {
      log.info("Scheduler already running");
    }
  }
}