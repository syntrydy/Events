package com.it.mougang.gasmyr.events.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by gamyr for project: Events on 5/22/16 11:03.
 */
@IgnoreExtraProperties
public class CALL {
    public CALL(){
    }
    private int id;
    private String duration;
    private String date;
    private String from;
    private String to;
    private String callType;

    public CALL(int id, String duration, String date, String from, String to, String callType) {
        this.id = id;
        this.duration = duration;
        this.date = date;
        this.from = from;
        this.to = to;
        this.callType = callType;
    }

    public CALL(String duration, String date, String from, String to, String callType) {
        this.duration = duration;
        this.date = date;
        this.from = from;
        this.to = to;
        this.callType = callType;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCallType() {
        return callType;
    }
}
