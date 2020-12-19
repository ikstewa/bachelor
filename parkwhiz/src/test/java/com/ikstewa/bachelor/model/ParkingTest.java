package com.ikstewa.bachelor.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParkingTest {

  @Test
  void read_from_parkwhiz() {

    final var sourceJson =
        "{\n"
            + "\t\"start_time\": \"2020-12-19T09:00:00.000-08:00\",\n"
            + "\t\"site_url\": \"/mt-bachelor-ski-resort-parking-2/dec-19-2020-daily-parking-1076063/\",\n"
            + "\t\"availability\": {\n"
            + "\t\t\"approximate_low\": 0.0,\n"
            + "\t\t\"approximate_high\": 0.0,\n"
            + "\t\t\"available\": 0\n"
            + "\t},\n"
            + "\t\"_embedded\": {\n"
            + "\t\t\"pw:venue\": {\n"
            + "\t\t\t\"id\": 478498,\n"
            + "\t\t\t\"name\": \"Mt. Bachelor Ski Resort\",\n"
            + "\t\t\t\"address1\": \"13000 SW Century Dr\",\n"
            + "\t\t\t\"city\": \"Bend\",\n"
            + "\t\t\t\"state\": \"OR\",\n"
            + "\t\t\t\"postal_code\": \"97703\"\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}";
    final var expected =
        Parking.builder()
            .date(LocalDate.of(2020, 12, 19))
            .siteUrl("/mt-bachelor-ski-resort-parking-2/dec-19-2020-daily-parking-1076063/")
            .availableCount(0)
            .build();

    final var deser = Mapper.readValue(sourceJson, Parking.class);

    Assertions.assertEquals(expected, deser);

    Assertions.assertEquals(expected, Mapper.readValue(Mapper.writeValue(deser), Parking.class));
  }

  @Test
  void test() {
    var p = Parking.builder().date(LocalDate.now()).siteUrl("foo-bar").availableCount(1).build();

    var json = Mapper.writeValue(p);

    System.out.println(json);

    var deser = Mapper.readValue(json, Parking.class);
    System.out.println(deser);
  }
}
