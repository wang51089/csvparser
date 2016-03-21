package com.hohai.jx.wjh.dataobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjh on 2016/1/17.
 */
public class Row {
    public List<Cell> cells = new ArrayList<Cell>();
    public int number;
    public List<String> primaryKey = new ArrayList<String>();
    public List<String> titles = new ArrayList<String>();
    public List<String> referencedRows = new ArrayList<String>();
    public int sourceNumber;
    public Table table;
    public Map<String , Object> annotations = new HashMap<String, Object>();

    public boolean add(Cell cell){
        cells.add(cell);
        return true;
    }

}
