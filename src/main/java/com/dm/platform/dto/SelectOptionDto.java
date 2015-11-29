package com.dm.platform.dto;

/**
 * Created by cgj on 2015/11/29.
 */
public class SelectOptionDto {
    public SelectOptionDto() {

    }

    public SelectOptionDto(String defaultText) {
        this.text = defaultText;
        this.value = "";
        this.selected = true;
    }

    private String text;
    private String value;
    private boolean selected;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
