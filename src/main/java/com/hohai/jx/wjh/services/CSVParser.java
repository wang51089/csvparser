package com.hohai.jx.wjh.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hohai.jx.wjh.dataobject.*;

import java.io.*;
import java.util.Scanner;

/**
 * Created by wjh on 2016/1/17.
 */
public class CSVParser {

    public ResultTable parse(String dialectFilePath, String toBeParsedFile) throws IOException {
        //get the dialect description
        ObjectMapper mapper = new ObjectMapper();
        File dialectFile = new File(dialectFilePath);
        ObjectNode dialect = (ObjectNode) mapper.readTree(dialectFile);
        //1.Create a new table T with the annotations
        Table T = new Table();
        //2.Create a metadata document structure M that looks like bla bla
        ObjectNode M = mapper.createObjectNode();
        M.put("@context", "http://www.w3.org/ns/csvw");
        ArrayNode comments = M.putArray("rdfs:comment");
        ObjectNode tableSchema = M.putObject("tableSchema");
        ArrayNode columns = tableSchema.putArray("columns");
        //3.If the URL of the tabular data file being parsed is known, set the url property on M to that URL
        Scanner scanner = new Scanner(System.in);
        System.out.println("input the table id:");
        String id = scanner.nextLine().trim();
        T.setId(id);
        System.out.println("Do you know the URL of the tabular data file?[Y/N]");
        String yn = scanner.nextLine().trim();
        if ("y".equalsIgnoreCase(yn) || "yes".equalsIgnoreCase(yn)) {
            System.out.println("input the URL of the tabular data file:");
            String url = scanner.nextLine().trim();
            T.setUrl(url);
        }
        //4.Set source row number to 1
        int sourceRowNumber = 1;
        //5.Read the file using the encoding
        FileInputStream fileInputStream = new FileInputStream(toBeParsedFile);
        Reader reader = new InputStreamReader(fileInputStream, dialect.get("encoding").asText().trim());
        BufferedReader bufferedReader = new BufferedReader(reader);
        //6.Repeat the following the number of times indicated by skip rows
        int skipRows = dialect.get("skipRows").asInt();
        String commentPrefix = dialect.get("commentPrefix").asText();
        for (int i = 0; i < skipRows; i++) {
            String rowContent = bufferedReader.readLine();
            if (commentPrefix != null && rowContent.startsWith(commentPrefix)) {
                String comment = rowContent.substring(1).trim();
                comments.add(comment);
            } else if (!"".equals(rowContent)) {
                comments.add(rowContent);
            }
            sourceRowNumber++;
        }
        //7.Repeat the following the number of times indicated by header row count
        int headerRowCount = dialect.get("headerRowCount").asInt();
        int skipColumns = dialect.get("skipColumns").asInt();
        String quoteChar = dialect.get("quoteChar").asText().trim();
        String delimiter = dialect.get("delimiter").asText().trim();
        for (int i = 0; i < headerRowCount; i++) {
            String rowContent = bufferedReader.readLine();
            if (commentPrefix != null && rowContent.startsWith(commentPrefix)) {
                String comment = rowContent.substring(1).trim();
                comments.add(comment);
            } else {
                String[] cells = parseCells(rowContent, delimiter, quoteChar);
                cells = skipSkipColumns(cells , skipColumns);
                for (int j = 0; j < cells.length; j++) {
                    if (cells[j].trim().equals("")){
                        continue;
                    }else if( columns.get(j) == null ){
                        ObjectNode column = columns.addObject();
                        ArrayNode titles = column.putArray("title");
                        titles.add(cells[j]);
                    }else{
                        ObjectNode column = (ObjectNode) columns.get(j);
                        ArrayNode titles = (ArrayNode) column.get("title");
                        titles.add(cells[j]);
                    }
                }
            }
            sourceRowNumber++;
        }
        //8.If header row count is zero, create an empty column description object in M.tableSchema.columns for each column in the current row after skip columns.
        if (headerRowCount == 0) {
            String rowContent = bufferedReader.readLine();
            String[] cells = rowContent.split(delimiter);
            for (int j = skipColumns; j < cells.length; j++) {
                ObjectNode column = columns.addObject();
            }
        }
        //9.Set row number to 1.
        int rowNumber = 1;
        //10.While it is possible to read another row, do the following:
        boolean skipBlankRows = dialect.get("skipBlankRows").asBoolean();
        String row;
        while ((row = bufferedReader.readLine()) != null) {
            int sourceColumnNumber = 1;
            if (commentPrefix != null && row.startsWith(commentPrefix)) {
                String comment = row.substring(1).trim();
                comments.add(comment);
            } else {
                String[] cells = parseCells(row ,delimiter ,quoteChar);
                if( isEmptyRow(cells) && skipBlankRows==true ){
                    continue;
                }else{
                    Row R = new Row();
                    R.setTable(T);
                    R.setNumber(rowNumber);
                    R.setSourceNumber(sourceRowNumber);
                    T.getRows().add(R);
                    cells = skipSkipColumns(cells , skipColumns);
                    sourceColumnNumber += skipColumns;

                    for (int i = 0; i < cells.length; i++) {
                        Column C;
                        if (T.getColumns().size() < i+1) {
                            C = new Column();
                            C.setTable(T);
                            C.setNumber(i+1);
                            C.setSourceNumber(sourceColumnNumber);
                            T.getColumns().add(i,C);
                        }else {
                            C = T.getColumns().get(i);
                        }
                        Cell D = new Cell();
                        D.setTable(T);
                        D.setColumn(C);
                        D.setRow(R);
                        D.setStringValue(cells[i]);
                        D.setValue(cells[i]);
                        C.getCells().add(D);
                        R.getCells().add(D);
                        sourceColumnNumber++;
                    }
                }
            }
            sourceRowNumber++;
        }
        //11.If M.rdfs:comment is an empty array, remove the rdfs:comment property from M.
        if (comments.isEmpty(null)) {
            M.remove("rdfs:comment");
        }
        //12.Return the table T and the embedded metadata M
        ResultTable resultTable = new ResultTable();
        resultTable.setT(T);
        resultTable.setM(M);
        return resultTable;

    }

    private boolean isEmptyRow(String[] cells) {
        for( String cell:cells ){
            if( !"".equals(cell) ){
                return false;
            }
        }
        return true;
    }

    private String[] parseCells(String rowContent, String delimiter,String quoteChar) {
        String[] cells = rowContent.split(delimiter);
        for( int i = 0 ; i < cells.length ; i++ ){
            if(cells[i].startsWith(quoteChar)){
                cells[i] = cells[i].substring(1,cells[i].length()-1);
            }
        }
        return cells;

    }

    private String[] skipSkipColumns(String[] cells , int skipColumns){
        String[] skipedCells = new String[cells.length-skipColumns];
        for (int i = 0; i < skipedCells.length; i++) {
            skipedCells[i] = cells[i + skipColumns];
        }
        return skipedCells;
    }
}
