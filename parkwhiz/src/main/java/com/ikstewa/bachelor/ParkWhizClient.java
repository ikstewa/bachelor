package com.ikstewa.bachelor;

import com.google.common.annotations.VisibleForTesting;
import com.ikstewa.bachelor.model.Mapper;
import com.ikstewa.bachelor.model.Parking;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ParkWhizClient {

  private static final String BACHELOR_VENUE = "478498";
  static final ZoneId ZONE_ID = ZoneId.of("America/Los_Angeles");
  private static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private final OkHttpClient client;

  public ParkWhizClient() {
    this.client = new OkHttpClient();
  }

  public Parking getParking(LocalDate date) {
    return query(date, date).iterator().next();
  }

  public List<Parking> getParking(LocalDate startDateInclusive, LocalDate endDateInclusive) {
    return query(startDateInclusive, endDateInclusive);
  }

  private List<Parking> query(LocalDate firstDate, LocalDate lastDate) {
    final var url =
        HttpUrl.parse(String.format("https://api.parkwhiz.com/v4/venues/%s/events", BACHELOR_VENUE))
            .newBuilder()
            .addQueryParameter("fields", "start_time,site_url,availability")
            .addQueryParameter("sort", "start_time")
            .addQueryParameter(
                "q",
                String.format(
                    "starting_after:%s starting_before:%s",
                    toDateQueryAfter(firstDate), toDateQueryBefore(lastDate)))
            .build();

    try {
      try (Response response = client.newCall(new Request.Builder().url(url).build()).execute()) {
        return Mapper.readListValue(response.body().string(), Parking.class);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @VisibleForTesting
  static String toDateQueryAfter(LocalDate date) {
    return FORMAT.format(date.atStartOfDay(ZONE_ID));
  }

  @VisibleForTesting
  static String toDateQueryBefore(LocalDate date) {
    return FORMAT.format(date.plusDays(1).atStartOfDay(ZONE_ID));
  }
}
