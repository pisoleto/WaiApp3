package com.jesus.waiapp;

/**
 * Created by Jesus Manuel on 27/11/2017.
 */

public class Messages {


    private  String message;
    private String type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;
    private long time;
    private boolean seen;

    public Messages(String message, Boolean seen, long time, String type, String from) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.type = type;
        this.from = from;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Messages(){

    }




}
