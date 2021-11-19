package com.be.solo.qr.code.scanner.SoloModels;

public class SoloFavoriteModel {

    private String content;
    private String date;
    private String type;
    private int color;
    private int icon;

    public SoloFavoriteModel(String content, String date, String type, int color, int icon) {
        this.content = content;
        this.date = date;
        this.type = type;
        this.color = color;
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
