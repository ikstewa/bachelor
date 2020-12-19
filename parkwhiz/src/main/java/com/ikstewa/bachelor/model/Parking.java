package com.ikstewa.bachelor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.ikstewa.bachelor.model.Parking.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@AutoValue
@JsonDeserialize(builder = Builder.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class Parking {

  @JsonProperty
  public abstract LocalDate date();

  @JsonProperty
  public abstract String siteUrl();

  @JsonProperty
  public abstract Integer availableCount();

  public static Builder builder() {
    return new AutoValue_Parking.Builder();
  }

  @AutoValue.Builder
  @JsonPOJOBuilder(withPrefix = "")
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public abstract static class Builder {

    private static DateTimeFormatter FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public abstract Builder date(LocalDate val);

    public Builder startTime(String val) {
      return this.date(LocalDateTime.parse(val, FORMAT).toLocalDate());
    }

    public abstract Builder siteUrl(String val);

    public abstract Builder availableCount(Integer val);

    public Builder availability(Map<String, Object> val) {
      return availableCount((Integer) val.get("available"));
    }

    public abstract Parking build();

    @JsonCreator
    private static Builder builder() {
      return Parking.builder();
    }
  }
}
