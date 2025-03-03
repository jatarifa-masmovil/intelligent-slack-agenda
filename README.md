# Automating Task Extraction from Slack Messages Using ChatGPT and Jira Integration

## Overview

This automation aims to retrieve Slack messages from the past day,
extract actionable tasks using ChatGPT, and allow the user to either
create Jira tasks or reject them directly from Slack. The process continues until all tasks receive a response.

## Workflow Steps

1. **Retrieve Slack Messages**
    - The system fetches all messages from the last 24 hours from all public channels via the Slack API.

2. **Generate Task List with ChatGPT**
    - The retrieved messages are processed by ChatGPT to extract potential tasks and generate a structured task list.

3. **User Decision in Slack (PENDING)**
    - The user is presented with the generated tasks in Slack.
    - For each task, the user can:
        - **Create a Jira Task:** The task is sent to Jira and recorded.
        - **Reject the Task:** The task is dismissed.

4. **Iterate Until Completion**
    - The process continues until all tasks are either created in Jira or rejected.

5. **Tasks summary send to Slack Channel**
    - Tasks prioritized summary is sent to the Slack channel.

## Squence Diagram

```mermaid 
sequenceDiagram
    participant Workflow
    participant SlackToWorkflowSignalEndpoint
    participant Slack API
    participant ChatGPT
    participant Jira
    Workflow ->> Slack API: Retrieve messages from the last day
    Slack API -->> Workflow: Return messages
    Workflow ->> ChatGPT: Prompt to generate task list from messages
    ChatGPT -->> Workflow: Return generated task list
    loop Until all responses (for every task) are processed
        Slack API ->> SlackToWorkflowSignalEndpoint: Received slack response
        SlackToWorkflowSignalEndpoint ->> Workflow: Workflow Signal with a task response
        alt User creates Jira task
            Workflow ->> Jira: Create Jira task
            Jira -->> Workflow: Task created
        else Workflow rejects task
            Workflow ->> Workflow: Task rejected
        end
    end
    Workflow ->> Slack API: Tasks summary send to Slack Channel
    Workflow ->> Workflow: Process completed
```