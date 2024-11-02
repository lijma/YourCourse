package com.mf.wetalker.infra.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MfObjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MfObjectMapper() {

    }

    public static <T extends Object> String readObjectsAsString(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("failed to convert object: {} to string", t);
            return "";
        }
    }

    public static <T extends Object> List<T> getObjectsFromJsonString(String str, TypeReference<List<T>> refer) {
        try {
            return objectMapper.readValue(str, refer);
        } catch (JsonProcessingException e) {
            log.error("failed to convert objects: {} from string: {}", refer, str);
            return null;
        }
    }

    public static <T extends Object> T getObjectFromJsonString(String str, Class<T> refer) {
        try {
            return objectMapper.readValue(str, refer);
        } catch (JsonProcessingException e) {
            log.error("failed to convert object: {} from string: {}", refer, str);
            return null;
        }
    }
}
