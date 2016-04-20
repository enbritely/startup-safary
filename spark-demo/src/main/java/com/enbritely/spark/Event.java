package com.enbritely.spark;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by joe on 2016.04.20..
 */
public class Event implements Serializable {
    private String sessionId;
    private Long timestamp;
    private String type;

    public Event(String type, String sessionId, Long timestamp) {
        this.type = type;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getType() { return type; }
}