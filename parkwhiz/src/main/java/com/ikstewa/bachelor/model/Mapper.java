package com.ikstewa.bachelor.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

public final class Mapper {

  private static final ObjectMapper MAPPER =
      new ObjectMapper().registerModule(new JavaTimeModule()).registerModule(new Jdk8Module());

  private Mapper() {}

  public static <T> List<T> readListValue(String json, Class<? extends T> clazz) {
    try {
      return MAPPER.readValue(
          json, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readValue(String json, Class<? extends T> clazz) {
    try {
      return MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> String writeValue(T obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
