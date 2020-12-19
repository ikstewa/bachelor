package com.ikstewa.bachelor;

import com.ikstewa.bachelor.model.Parking;
import java.time.LocalDate;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ParkWhizClientTest {

  private static ParkWhizClient client;

  @BeforeAll
  static void init() {
    client = new ParkWhizClient();
  }

  @Test
  void test_single_day() {
    final var date = LocalDate.of(2021, 03, 04);
    final var parking = client.getParking(date);

    Assertions.assertEquals(date, parking.date());
  }

  @Test
  void test_week() {
    final var start = LocalDate.of(2021, 03, 4);
    final var end = LocalDate.of(2021, 03, 8);
    final var parking = client.getParking(start, end);

    Assertions.assertEquals(5, parking.size());

    Assertions.assertArrayEquals(
        IntStream.rangeClosed(4, 8).mapToObj(i -> LocalDate.of(2021, 03, i)).toArray(),
        parking.stream().map(Parking::date).toArray());
  }

  @Test
  void test_date_param() {
    final var expectedStart = "2020-12-19T00:00:00-08:00";
    final var expectedEnd = "2020-12-20T00:00:00-08:00";
    final var date = LocalDate.of(2020, 12, 19);

    Assertions.assertEquals(expectedStart, ParkWhizClient.toDateQueryAfter(date));
    Assertions.assertEquals(expectedEnd, ParkWhizClient.toDateQueryBefore(date));
  }
}
