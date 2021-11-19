package com.be.solo.qr.code.scanner.SoloModels;

public class SoloCreatingQRModel {

    private int icon;
    private String item_name;
    private int color;

    public SoloCreatingQRModel(int icon, String item_name, int color) {
        this.icon = icon;
        this.item_name = item_name;
        this.color = color;
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

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
