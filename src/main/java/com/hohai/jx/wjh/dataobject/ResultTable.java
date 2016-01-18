package com.hohai.jx.wjh.dataobject;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by wjh on 2016/1/17.
 */
public class ResultTable {
    Table t;
    ObjectNode m;

    public Table getT() {
        return t;
    }

    public void setT(Table t) {
        this.t = t;
    }

    public ObjectNode getM() {
        return m;
    }

    public void setM(ObjectNode m) {
        this.m = m;
    }
}
