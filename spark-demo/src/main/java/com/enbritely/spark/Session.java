package com.enbritely.spark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 2016.04.20..
 */
public class Session {

    private String sessionId;
    private List<Long> clickTimestamps = new ArrayList<>();

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public void addClick(Long timestamp) {
        clickTimestamps.add(timestamp);
    }

    public int getClickCount() {
        return clickTimestamps.size();
    }

    public String getSessionId() {
        return sessionId;
    }
}
