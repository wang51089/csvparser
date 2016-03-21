package com.hohai.jx.wjh.dataobject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjh on 2016/3/15.
 */
public class TableGroup {
    public String id = null;
    public List<String> notes = new ArrayList<String>();
    public List<Table> tables = new ArrayList<Table>();
    public Map<String , Object> annotations = new HashMap<String, Object>();
}
