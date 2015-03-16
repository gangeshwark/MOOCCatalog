package com.shwavan.mooccatalog.adapter;

public class NavigationItemAdapter {

    public String title;
    public int counter;
    public int icon;
    public boolean isHeader;
    public int colorSelected = 0;
    public boolean checked = false;
    public boolean removeSelector = false;

    public NavigationItemAdapter(String title, int icon, boolean header, int counter, int colorSelected, boolean removeSelector) {
        this.title = title;
        this.icon = icon;
        this.isHeader = header;
        this.counter = counter;
        this.colorSelected = colorSelected;
        this.removeSelector = removeSelector;
    }

    public NavigationItemAdapter(String title, int icon, boolean header, int counter) {
        this.title = title;
        this.icon = icon;
        this.isHeader = header;
        this.counter = counter;
    }

}
