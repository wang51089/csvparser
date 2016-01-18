package com.hohai.jx.wjh.dataobject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjh on 2016/1/17.
 */
public class Table {
    private List<Column> columns = new ArrayList<Column>();
    private List<Row> rows = new ArrayList<Row>();
    private String id = null;
    private String url = null;
    private String tableDirection = "auto";
    private boolean suppressOutput = false;
    private boolean notes = false;
    private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
    private List<TransformationDefinition> transformationDefinitions = new ArrayList<TransformationDefinition>();

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTableDirection() {
        return tableDirection;
    }

    public void setTableDirection(String tableDirection) {
        this.tableDirection = tableDirection;
    }

    public boolean isSuppressOutput() {
        return suppressOutput;
    }

    public void setSuppressOutput(boolean suppressOutput) {
        this.suppressOutput = suppressOutput;
    }

    public boolean isNotes() {
        return notes;
    }

    public void setNotes(boolean notes) {
        this.notes = notes;
    }

    public List<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<ForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public List<TransformationDefinition> getTransformationDefinitions() {
        return transformationDefinitions;
    }

    public void setTransformationDefinitions(List<TransformationDefinition> transformationDefinitions) {
        this.transformationDefinitions = transformationDefinitions;
    }
}
