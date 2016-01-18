package com.hohai.jx.wjh.main;

import com.hohai.jx.wjh.dataobject.Cell;
import com.hohai.jx.wjh.dataobject.ResultTable;
import com.hohai.jx.wjh.dataobject.Row;
import com.hohai.jx.wjh.services.CSVParser;


import java.io.*;

/**
 * Created by wjh on 2016/1/16.
 */
public class Main {
    public static void main(String[] args) {
        CSVParser csvParser = new CSVParser();
        ResultTable table = null;
        try {
            table  = csvParser.parse("D:/dialect.json" , "D:/tabular_data_file.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("the table is as follows:");
        for( Row row : table.getT().getRows() ){
            for( Cell cell : row.getCells()){
                System.out.print(cell.getStringValue());
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.println("The meta data is as follows");
        System.out.println(table.getM().toString());
    }
}
