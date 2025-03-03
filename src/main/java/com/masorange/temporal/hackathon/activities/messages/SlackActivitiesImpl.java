package com.masorange.temporal.hackathon.activities.messages;

import com.masorange.temporal.hackathon.activities.ChannelMessages;
import com.masorange.temporal.hackathon.activities.MessagesActivities;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import io.temporal.activity.Activity;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SlackActivitiesImpl implements MessagesActivities {

	private final Slack slack;
	//Requires env variable SLACK_TOKEN
	private final String token = System.getenv("SLACK_TOKEN");

	public SlackActivitiesImpl(Slack slack) {
		this.slack = slack;
	}

	@Override
	public ChannelMessages retrieveMessages(OffsetDateTime fromDate) {
		try {
			var client = slack.methods();
			var result = client.conversationsList(r -> r.token(token));
			Map<String, List<String>> channelMessages = new HashMap<>();

			for (Conversation channel : result.getChannels()) {
				var channelId = channel.getId();

				ConversationsHistoryResponse history = client.conversationsHistory(r -> r
					.token(token)
					.channel(channelId)
					.latest(fromDate.toString())
				);

				channelMessages.put(channel.getName(),
					history.getMessages().stream().filter(message -> Objects.isNull(message.getSubtype()))
						.map(Message::getText)
						.collect(Collectors.toList()));
			}
			return new ChannelMessages(channelMessages);
		} catch (IOException | SlackApiException e) {
			System.out.println("Error: " + e.getMessage());
			throw Activity.wrap(e);
		}
	}
}
