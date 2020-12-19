package com.ikstewa.bachelor.slack;

import com.google.gson.JsonObject;
import java.util.Optional;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageSender {

  private final OkHttpClient client;
  private final String webhookUrl;

  public MessageSender(final String webhookUrl) {
    this.client = new OkHttpClient();
    this.webhookUrl = webhookUrl;
  }

  public void sendMessage(Optional<String> channel, String text) {

    final var requestBody = payload(channel, text);
    final var request =
        new Request.Builder()
            .url(webhookUrl)
            .post(
                RequestBody.create(
                    requestBody, MediaType.parse("application/javascript; charset=utf-8")))
            .build();

    System.out.println("Sending request: " + requestBody);
    try {
      try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
          throw new RuntimeException("Failed to send webhook: " + response);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String payload(Optional<String> channel, String text) {
    JsonObject json = new JsonObject();
    json.addProperty("text", text);
    json.addProperty("username", "Parking Bot");
    json.addProperty("icon_emoji", ":ghost:");
    channel.ifPresent(c -> json.addProperty("channel", c));
    return json.toString();
  }
}
