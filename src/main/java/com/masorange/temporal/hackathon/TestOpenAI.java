package com.masorange.temporal.hackathon;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionMessage;
import com.openai.models.ChatModel;
import org.apache.commons.io.IOUtils;

public final class TestOpenAI {
  private TestOpenAI() {}

  @SuppressWarnings("all")
  public static void main(String[] args) throws Exception {
    // - The `OPENAI_API_KEY` environment variable
    OpenAIClient client = OpenAIOkHttpClient.fromEnv();
    ChatCompletionCreateParams.Builder createParamsBuilder = ChatCompletionCreateParams.builder()
        .model(ChatModel.GPT_3_5_TURBO)
        .maxCompletionTokens(4096)
        .addSystemMessage(IOUtils.toString(TestOpenAI.class.getResourceAsStream("/system-message.txt")))
        .addUserMessage(IOUtils.toString(TestOpenAI.class.getResourceAsStream("/user-message.txt")));
    List<ChatCompletionMessage> messages =
        client.chat().completions().create(createParamsBuilder.build()).choices().stream()
            .map(ChatCompletion.Choice::message)
            .toList();
    var tasksString = messages.stream()
        .flatMap(message -> message.content().stream())
        .collect(Collectors.joining("\n"));

    ObjectMapper mapper = new ObjectMapper();
    var json = mapper.readTree(tasksString);

    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
  }
}