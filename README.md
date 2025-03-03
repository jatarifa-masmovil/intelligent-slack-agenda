# Automating Task Extraction from Slack Messages Using ChatGPT and Jira Integration

## Overview

This automation aims to retrieve Slack messages from the past day, extract actionable tasks using ChatGPT, and allow the user to either create Jira tasks or reject them directly from Slack. The process continues until all tasks receive a response.

## Workflow Steps

1. **Retrieve Slack Messages**
    - The system fetches all messages from the last 24 hours for a specific user via the Slack API.

2. **Generate Task List with ChatGPT**
    - The retrieved messages are processed by ChatGPT to extract potential tasks and generate a structured task list.

3. **User Decision in Slack**
    - The user is presented with the generated tasks in Slack.
    - For each task, the user can:
        - **Create a Jira Task:** The task is sent to Jira and recorded.
        - **Reject the Task:** The task is dismissed.

4. **Iterate Until Completion**
    - The process continues until all tasks are either created in Jira or rejected.

## Squence Diagram
```mermaid 
sequenceDiagram
    participant Workflow
    participant SlackToWorkflowSignalEndpoint
    participant Slack API
    participant ChatGPT
    participant Jira

    Workflow->>Slack API: Retrieve messages from the last day
    Slack API-->>Workflow: Return messages
    Workflow->>ChatGPT: Prompt to generate task list from messages
    ChatGPT-->>Workflow: Return generated task list
    loop Until all responses (for every task) are processed
        Slack API->>SlackToWorkflowSignalEndpoint: Received slack response
        SlackToWorkflowSignalEndpoint->>Workflow: Workflow Signal with a task response
        alt User creates Jira task
            Worklfow->>Jira: Create Jira task
            Jira-->>Workflow: Task created
        else Workflow rejects task
            Workflow->>Workflow: Task rejected
        end
    end
    Workflow->>Workflow: Process completed
```