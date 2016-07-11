package com.it.mougang.gasmyr.events.database;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by gamyr for project: Events on 5/22/16 10:58.
 */
@IgnoreExtraProperties
public class SMS {

    public SMS(){

    }
    private int id;
    private String boby;
    private String date;
    private String sender;
    private String receiver;
    private String smsType;

    public SMS(String boby, String date, String sender, String receiver,String smsType) {
        this.boby = boby;
        this.date = date;
        this.sender = sender;
        this.receiver=receiver;
        this.smsType = smsType;
    }

    public SMS(int id, String boby, String date, String sender,String receiver, String smsType) {
        this.id = id;
        this.boby = boby;
        this.date = date;
        this.sender = sender;
        this.receiver=receiver;
        this.smsType = smsType;
    }

    public String getBoby() {
        return boby;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getSmsType() {
        return smsType;
    }

    public String getReceiver() {
        return receiver;
    }
}
