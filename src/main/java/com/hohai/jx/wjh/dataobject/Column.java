package com.hohai.jx.wjh.dataobject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjh on 2016/1/17.
 */
public class Column {
    public String aboutURL = null;
    public List<Cell> cells = new ArrayList<Cell>();
    public String datatype = "String" ;
    public String defaultValue = "";
    public String lang = "und";
    public String name = null;
    public String nullValue = "";
    public int number ;
    public boolean ordered = false;
    public String propertyUrl = null;
    public boolean required  = false;
    public String separator = null;
    public int sourceNumber ;
    public boolean suppressoutput = false;
    public Table table;
    public String textDirection = "auto";
    public List<String> titles = new ArrayList<String>();
    public String valueUrl = null;
    public boolean virtual = false;
    public Map<String , Object> annotations = new HashMap<String, Object>();
}
