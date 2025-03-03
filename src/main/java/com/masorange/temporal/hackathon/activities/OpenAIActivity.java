package com.masorange.temporal.hackathon.activities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masorange.temporal.hackathon.activities.model.TaskList;

import com.google.gson.annotations.SerializedName;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionMessage;
import com.openai.models.ChatModel;
import io.temporal.activity.ActivityInterface;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

@ActivityInterface
public interface OpenAIActivity {


  TaskList calculateTasks(String chat);

  class OpenAIActivityImpl implements OpenAIActivity {

    @Override
    @SneakyThrows
    public TaskList calculateTasks(String chat) {
      // - The `OPENAI_API_KEY` environment variable
      OpenAIClient client = OpenAIOkHttpClient.fromEnv();
      ChatCompletionCreateParams.Builder createParamsBuilder = ChatCompletionCreateParams.builder()
          .model(ChatModel.GPT_3_5_TURBO)
          .maxCompletionTokens(4096)
          .addSystemMessage(IOUtils.toString(OpenAIActivity.class.getResourceAsStream("/system-message.txt")))
          .addUserMessage(chat);
      List<ChatCompletionMessage> messages =
          client.chat().completions().create(createParamsBuilder.build()).choices().stream()
              .map(ChatCompletion.Choice::message)
              .toList();
      var tasksString = messages.stream()
          .flatMap(message -> message.content().stream())
          .collect(Collectors.joining("\n"));

      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(tasksString, TaskList.class);
    }
  }
}
