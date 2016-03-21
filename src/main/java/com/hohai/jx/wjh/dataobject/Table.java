package com.hohai.jx.wjh.dataobject;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjh on 2016/1/17.
 */
public class Table {
    public List<Column> columns = new ArrayList<Column>();
    public String tableDirection = "auto";
    public List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
    public String id = null;
    public List<String> notes = new ArrayList<String>(); //any number of additional annotations on the table. This annotation may be empty.
    public List<Row> rows = new ArrayList<Row>();
    public String schema = null;
    public boolean suppressOutput = false;
    public List<Object> transformations = new ArrayList<Object>();
    public String url = null;
    public Map<String , Object> annotations = new HashMap<String, Object>();
}
