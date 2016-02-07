package com.hohai.jx.wjh.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hohai.jx.wjh.dataobject.Cell;
import com.hohai.jx.wjh.dataobject.Column;
import com.hohai.jx.wjh.dataobject.ResultTable;
import com.hohai.jx.wjh.dataobject.Row;
import com.hohai.jx.wjh.services.CSVParser;


import java.io.*;
import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by wjh on 2016/1/16.
 */
public class Main {
    public static final String DISLAY_DELIMETER = "\t";
    public static void main(String[] args) {
        CSVParser csvParser = new CSVParser();
        ResultTable resultTable = null;
        try {
            //java -jar csvparser-1.0-SNAPSHOT-jar-with-dependencies.jar D:/dialect.json D:/tabular_data_file.txt
            /*resultTable  = csvParser.parse("D:/dialect.json" , "D:/tabular_data_file.txt");*/
            resultTable = csvParser.parse(args[0] , args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //1.print the tatble:
        System.out.println();
        System.out.println("the tabular data is as follows:");

        for( Row row : resultTable.getT().getRows() ){
            for( Cell cell : row.getCells()){
                System.out.print(cell.getStringValue());
                System.out.print(DISLAY_DELIMETER);
            }
            System.out.println();
        }

        //2.print the column annotation:
        System.out.println();
        System.out.println("The columns have the annotations shown in the following table");
        List<Column> columns = resultTable.getT().getColumns();
        System.out.println("column id\ttable id\tNumber\tsource number\tcells\ttitles");
        for( int i = 1 ; i <= columns.size() ; i++ ){
            System.out.print("C");
            System.out.print(i);
            System.out.print(DISLAY_DELIMETER);
            System.out.print(columns.get(i - 1).getTable().getId());
            System.out.print(DISLAY_DELIMETER);
            System.out.print(columns.get(i-1).getNumber());
            System.out.print(DISLAY_DELIMETER);
            System.out.print(columns.get(i-1).getSourceNumber());
            System.out.print(DISLAY_DELIMETER);
            List<Cell> cells = columns.get(i-1).getCells();
            StringBuilder ce = new StringBuilder();
            for(int j=0 ; j < cells.size() ; j++){
                ce.append("C"+(j+1)+"."+i);
                ce.append(",");
            }
            ce.deleteCharAt(ce.length()-1);
            System.out.print(ce.toString());
            System.out.print(DISLAY_DELIMETER);
            ArrayNode arrayNode = (ArrayNode) resultTable.getM().get("tableSchema").get("columns").get(i-1).get("title");
            ce = new StringBuilder();
            for(int j = 0 ; j < arrayNode.size() ; j ++){
                ce.append(arrayNode.get(j).asText());
                ce.append(",");
            }
            ce.deleteCharAt(ce.length()-1);
            System.out.print(ce.toString());
            System.out.print("\n");
        }
        columns = null;

        //3.print the row annotation:
        System.out.println();
        System.out.println("The rows have the annotations shown in the following table");
        List<Row> rows = resultTable.getT().getRows();
        System.out.println("row id\ttable id\tNumber\tsource number\tcells");
        for( int i = 1 ; i <= rows.size() ; i++ ){
            System.out.print("R");
            System.out.print(i);
            System.out.print(DISLAY_DELIMETER);
            System.out.print(rows.get(i - 1).getTable().getId());
            System.out.print(DISLAY_DELIMETER);
            System.out.print(rows.get(i-1).getNumber());
            System.out.print(DISLAY_DELIMETER);
            System.out.print(rows.get(i-1).getSourceNumber());
            System.out.print(DISLAY_DELIMETER);
            List<Cell> cells = rows.get(i-1).getCells();
            StringBuilder ce = new StringBuilder();
            for(int j=0 ; j < cells.size() ; j++){
                ce.append("C"+i+"."+(j+1));
                ce.append(",");
            }
            ce.deleteCharAt(ce.length()-1);
            System.out.print(ce.toString());
            System.out.print("\n");
        }

        //4.print the cell annotation:
        System.out.println();
        System.out.println("The rows have the annotations shown in the following table");
        rows = resultTable.getT().getRows();
        System.out.println("cell id\ttable id\tcolumn\trow\tstring value\tvalue");
        for( int i = 1 ; i <= rows.size() ; i++ ){
            List<Cell> cells =  rows.get(i-1).getCells();
            for(int j = 1 ; j <= cells.size() ; j++){
                System.out.print("C"+i+"."+j);
                System.out.print(DISLAY_DELIMETER);
                System.out.print(cells.get(j - 1).getTable().getId());
                System.out.print(DISLAY_DELIMETER);
                System.out.print("R"+i);
                System.out.print(DISLAY_DELIMETER);
                System.out.print("C"+j);
                System.out.print(DISLAY_DELIMETER);
                System.out.print(cells.get(j-1).getStringValue());
                System.out.print(DISLAY_DELIMETER);
                System.out.print(cells.get(j-1).getValue());
                System.out.print("\n");
            }
        }
        //5.print the meta data
        System.out.println();
        System.out.println("The meta data is as follows");
        System.out.println(jsonFormatter(resultTable.getM().toString()));
    }

    public static String jsonFormatter(String uglyJSONString){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }
}
