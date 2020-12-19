package com.ikstewa.bachelor;

import com.google.common.base.Strings;
import com.ikstewa.bachelor.model.Parking;
import com.ikstewa.bachelor.slack.MessageSender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class Runner {
  public static void main(String[] args) {

    final var webhookUrl = Strings.emptyToNull(System.getProperty("slack_webhook"));

    if (webhookUrl == null) {
      System.out.println("Missing property: 'slack_webhook'");
      System.exit(1);
    }

    final var messageSender = new MessageSender(webhookUrl);
    final var parkWhiz = new ParkWhizClient();

    final var now = LocalDateTime.now(ParkWhizClient.ZONE_ID);

    final LocalDate startDay;
    // skip today if it's after 10 am
    if (now.getHour() >= 10) {
      startDay = now.toLocalDate().plusDays(1);
    } else {
      startDay = now.toLocalDate();
    }

    final var parking = parkWhiz.getParking(startDay, startDay.plusDays(7));

    parking.stream()
        .filter(p -> p.availableCount() > 0)
        .map(Runner::toMessage)
        .forEach(m -> messageSender.sendMessage(Optional.empty(), m));
    //parking.stream()
    //    .map(Runner::toMessage)
    //    .forEach(m -> messageSender.sendMessage(Optional.of("#all-parking"), m));
  }

  private static String toMessage(Parking parking) {
    return String.format(
        "%s Parking available on %s!\n    <https://parkwhiz.com/%s|LINK>",
        parking.availableCount(), parking.date(), parking.siteUrl());
  }
}
