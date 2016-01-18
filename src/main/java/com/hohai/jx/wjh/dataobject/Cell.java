package com.hohai.jx.wjh.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjh on 2016/1/17.
 */
public class Cell {
    private Table table;
    private Column column;
    private Row row;
    private String stringValue ;
    private String value;
    private List<String> errors = new ArrayList<String>();
    private String textDirection = "auto";
    private boolean ordered = false;
    private String aboutURL = null;
    private String propertyURL = null;
    private String valueURL = null;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(String textDirection) {
        this.textDirection = textDirection;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public String getAboutURL() {
        return aboutURL;
    }

    public void setAboutURL(String aboutURL) {
        this.aboutURL = aboutURL;
    }

    public String getPropertyURL() {
        return propertyURL;
    }

    public void setPropertyURL(String propertyURL) {
        this.propertyURL = propertyURL;
    }

    public String getValueURL() {
        return valueURL;
    }

    public void setValueURL(String valueURL) {
        this.valueURL = valueURL;
    }
}
