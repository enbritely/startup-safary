package com.enbritely.spark;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by joe on 2016.04.20..
 */
public class Converter {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Event jsonToEvent(String json) throws IOException {
        Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        return new Event((String) map.get("type"), (String) map.get("sid"), (Long) map.get("ts"));
    }

}
