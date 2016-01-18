package com.hohai.jx.wjh.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjh on 2016/1/17.
 */
public class Row {
    private Table table;
    private int number;
    private int sourceNumber;
    private List<String> primaryKey = new ArrayList<String>();
    private List<String> referencedRows = new ArrayList<String>();
    private List<Cell> cells = new ArrayList<Cell>();

    public boolean add(Cell cell){
        cells.add(cell);
        return true;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(int sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<String> getReferencedRows() {
        return referencedRows;
    }

    public void setReferencedRows(List<String> referencedRows) {
        this.referencedRows = referencedRows;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
