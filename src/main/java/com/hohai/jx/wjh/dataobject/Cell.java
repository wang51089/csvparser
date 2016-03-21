package com.hohai.jx.wjh.dataobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjh on 2016/1/17.
 */
public class Cell {
    public String aboutURL = null;
    public Column column = null;
    public List<String> errors = new ArrayList<String>();
    public boolean ordered = false;
    public String propertyURL = null;
    public Row row = null;
    public String stringValue = null ;
    public Table table = null;
    public String textDirection = "auto";
    public String value = null;
    public String valueURL = null;

    public Map<String , Object> annotations = new HashMap<String, Object>();
}
