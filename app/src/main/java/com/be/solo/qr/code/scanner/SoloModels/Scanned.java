package com.be.solo.qr.code.scanner.SoloModels;

public class Scanned {
    private String title,time;

    public Scanned() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Scanned(String title, String time) {
        this.title = title;
        this.time = time;
    }
}
