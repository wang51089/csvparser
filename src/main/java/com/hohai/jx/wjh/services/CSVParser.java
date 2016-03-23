package com.hohai.jx.wjh.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hohai.jx.wjh.common.CSVModel;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by wjh on 2016/3/20.
 */
public class CSVParser {
    public static Map<String, String> context = new HashMap<String, String>() {
        {
            put("csvw", "http://www.w3.org/ns/csvw#");
            put("dc", "http://purl.org/dc/terms/");
            put("dcat", "http://www.w3.org/ns/dcat#");
            put("foaf", "http://xmlns.com/foaf/0.1/");
            put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            put("schema", "http://schema.org/");
            put("xsd", "http://www.w3.org/2001/XMLSchema#");
        }
    };
    public static String encoding = "utf-8";
    public static String[] lineTerminators = new String[]{"\r\n", "\n"};
    public static String quoteChar = "\"";
    public static boolean doubleQuote = true;
    public static int skipRows = 0;
    public static String commentPrefix = "#";
    public static boolean header = true;
    public static int headerRowCount = 1;
    public static String delimiter = ",";
    public static int skipColumns = 0;
    public static boolean skipBlankRows = false;
    public static boolean skipInitialSpace = false;
    public static String trim = "true";
    public static String escapeCharacter = "\\";
    //meta data
    private ObjectNode metaData = null;
    private ObjectNode table = null;

    public void setDefaultDialect() {
        encoding = "utf-8";
        lineTerminators = new String[]{"\r\n", "\n"};
        quoteChar = "\"";
        doubleQuote = true;
        skipRows = 0;
        commentPrefix = "#";
        header = true;
        headerRowCount = 1;
        delimiter = ",";
        skipColumns = 0;
        skipBlankRows = false;
        skipInitialSpace = false;
        trim = "true";
        escapeCharacter = "\\";
    }

    public CSVParser(String metaFilePath) throws IOException {
        File metaFile = new File(metaFilePath);
        ObjectMapper objectMapper = new ObjectMapper();
        metaData = (ObjectNode) objectMapper.readTree(metaFile);
    }

    /**
     * entry: edit metadata
     *
     * @throws IOException
     */
    public void displayMetadata() throws IOException {
        if (metaData != null) {
            displayObjectNode(metaData);
        }
    }

    /**
     * entry: build the model
     *
     * @return
     * @throws Exception
     */
    public void createTabularModel() throws Exception {
        table = createModel(metaData);
    }

    /**
     * entry: display the model
     */
    public void displayTabularModel() {
        displayObjectModel(table);
    }

    public void displayObjectModel(ObjectNode objectNode) {
        while (true) {
            Iterator<String> fieldNames = objectNode.fieldNames();
            String command = "";
            System.out.println("The model annotations are as follows:");
            int i = 0;
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                System.out.print("  " + fieldName);
                JsonNode jsonNode = objectNode.get(fieldName);
                if (jsonNode.getNodeType() == JsonNodeType.OBJECT) {
                    System.out.println(" --> (OBJECT)");
                } else if (jsonNode.getNodeType() == JsonNodeType.ARRAY) {
                    System.out.println(" --> (ARRAY)");
                } else if (jsonNode.getNodeType() == JsonNodeType.NULL) {
                    System.out.println(" --> (NULL)");
                } else {
                    System.out.println(" --> \"" + jsonNode.asText() + "\"");
                }
                i++;
            }
            System.out.println("Please enter field name for details or exit:");
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else {
                JsonNode jsonNode = objectNode.get(command);
                if (jsonNode != null) {
                    nextModelMenu(jsonNode);
                }
            }
        }
    }

    private void nextModelMenu(JsonNode node) {
        if (node.getNodeType() == JsonNodeType.OBJECT) {
            displayObjectModel((ObjectNode) node);
        } else if (node.getNodeType() == JsonNodeType.ARRAY) {
            displayArrayModel((ArrayNode) node);
        } else {
            displayAtomModel(node);
        }
    }

    private void displayAtomModel(JsonNode jsonNode) {
        while (true) {
            System.out.println("The property value is:");
            System.out.println(" " + jsonNode.asText("(NULL)"));
            System.out.println("Please enter exit to exit:");
            String command = "";
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else {
                System.out.println("illegal input!");
            }
        }
    }

    private void displayArrayModel(ArrayNode arrayNode) {
        while (true) {
            Iterator<JsonNode> jsonNodes = arrayNode.iterator();
            String command = "";
            // display the array
            System.out.println("The array elements are as follows:");
            int i = 0;
            while (jsonNodes.hasNext()) {
                JsonNode jsonNode = jsonNodes.next();
                if (jsonNode.getNodeType() == JsonNodeType.OBJECT) {
                    System.out.println(i + ". (OBJECT)");
                } else if (jsonNode.getNodeType() == JsonNodeType.ARRAY) {
                    System.out.println(i + ". (ARRAY)");
                } else if (jsonNode.getNodeType() == JsonNodeType.NULL) {
                    System.out.println(i + ". (NULL)");
                } else {
                    System.out.println(i + ". \"" + jsonNode.asText() +"\"");
                }
                i++;
            }

            //display details
            System.out.println("Please choose the element for details or exit:");
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else if (StringUtils.isNumeric(command.trim())) {
                JsonNode node = arrayNode.get(Integer.parseInt(command.trim()));
                nextModelMenu(node);
            } else {
                System.out.println("illegal input!");
            }
        }
    }


    private void setDialect(JsonNode dialect) {
        encoding = dialect.get("encoding").asText(encoding);
        ArrayNode lineTerminatorsNode = (ArrayNode) dialect.get("lineTerminators");
        if (lineTerminatorsNode != null) {
            lineTerminators = new String[lineTerminatorsNode.size()];
            for (int i = 0; i < lineTerminatorsNode.size(); i++) {
                lineTerminators[i] = lineTerminatorsNode.get(i).asText();
            }
        }
        quoteChar = dialect.get("quoteChar").asText(quoteChar);
        doubleQuote = dialect.get("doubleQuote").asBoolean(doubleQuote);
        skipRows = dialect.get("skipRows").asInt(skipRows);
        commentPrefix = dialect.get("commentPrefix").asText(commentPrefix);
        header = dialect.get("header").asBoolean(header);
        headerRowCount = dialect.get("headerRowCount").asInt(headerRowCount);
        delimiter = dialect.get("delimiter").asText(delimiter);
        skipColumns = dialect.get("skipColumns").asInt(skipColumns);
        skipBlankRows = dialect.get("skipBlankRows").asBoolean(skipBlankRows);
        skipInitialSpace = dialect.get("skipInitialSpace").asBoolean(skipInitialSpace);
        trim = dialect.get("trim").asText(trim);
    }

    public void displayObjectNode(ObjectNode objectNode) throws IOException {
        while (true) {
            Iterator<String> fieldNames = objectNode.fieldNames();
            String command = "";
            System.out.println("The object node properties are as follows:");
            int i = 0;
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldNode = objectNode.get(fieldName);
                if( fieldNode.getNodeType() == JsonNodeType.OBJECT ){
                    System.out.println(" \"" + fieldName + "\": " + "(OBJECT)");
                }else if( fieldNode.getNodeType() == JsonNodeType.ARRAY ){
                    System.out.println(" \"" + fieldName + "\": " + "(ARRAY)");
                }else if( fieldNode.getNodeType() == JsonNodeType.NULL ){
                    System.out.println(" \"" + fieldName + "\": " + "(NULL)");
                }else {
                    System.out.println(" \"" + fieldName + "\": \"" + fieldNode.asText() + "\"");
                }
                i++;
            }
            System.out.println("Please enter (add/edit/delete) or exit:");
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else if (command.startsWith("add")) {
                String commandPara = command.split(" ")[1];
                System.out.println("Please enter the field name:");
                String fieldName = scanner.nextLine().trim();
                if ("string".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the string:");
                    String fieldValue = scanner.nextLine().trim();
                    objectNode.put(fieldName, fieldValue);
                } else if ("int".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the int:");
                    int fieldValue = Integer.parseInt(scanner.nextLine().trim());
                    objectNode.put(fieldName, fieldValue);
                } else if ("double".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the double:");
                    double fieldValue = Double.parseDouble(scanner.nextLine().trim());
                    objectNode.put(fieldName, fieldValue);
                } else if ("boolean".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the boolean:");
                    boolean fieldValue = Boolean.parseBoolean(scanner.nextLine().trim());
                    objectNode.put(fieldName, fieldValue);
                } else if ("object".equalsIgnoreCase(commandPara) || "array".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the json value:");
                    String fieldValue = scanner.nextLine().trim();
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(fieldValue);
                    objectNode.set(fieldName, jsonNode);
                } else {
                    System.out.println("illegal input!");
                }
            } else if (command.startsWith("delete")) {
                String commandPara = command.split(" ")[1];
                objectNode.remove(commandPara);
            } else if (command.startsWith("edit")) {
                String commandPara = command.split(" ")[1];
                JsonNode node = objectNode.get(commandPara);
                nextMenu(node);
            } else {
                System.out.println("illegal input!");
            }
        }
    }

    private JsonNode getInputJson(String fieldValue) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(fieldValue);
        return jsonNode;
    }


    private void displayArrayNode(ArrayNode arrayNode) throws IOException {
        while (true) {
            Iterator<JsonNode> jsonNodes = arrayNode.iterator();
            String command = "";
            System.out.println("The array elements are as follows:");
            int i = 0;
            while (jsonNodes.hasNext()) {
                JsonNode jsonNode = jsonNodes.next();
                if( jsonNode.getNodeType() == JsonNodeType.OBJECT ){
                    System.out.println( i + ". (OBJECT)");
                }else if( jsonNode.getNodeType() == JsonNodeType.ARRAY ){
                    System.out.println( i + ". (ARRAY)");
                }else if( jsonNode.getNodeType() == JsonNodeType.NULL ){
                    System.out.println( i + ". (NULL)");
                }else {
                    System.out.println( i + ". \""  + jsonNode.asText() + "\"");
                }
                i++;
            }
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else if (command.startsWith("add")) {
                String commandPara = command.split(" ")[1];
                if ("string".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the string:");
                    String fieldValue = scanner.nextLine().trim();
                    arrayNode.add(fieldValue);
                } else if ("int".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the int:");
                    int fieldValue = Integer.parseInt(scanner.nextLine().trim());
                    arrayNode.add(fieldValue);
                } else if ("double".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the double:");
                    double fieldValue = Double.parseDouble(scanner.nextLine().trim());
                    arrayNode.add(fieldValue);
                } else if ("boolean".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the boolean:");
                    boolean fieldValue = Boolean.parseBoolean(scanner.nextLine().trim());
                    arrayNode.add(fieldValue);
                } else if ("object".equalsIgnoreCase(commandPara) || "array".equalsIgnoreCase(commandPara)) {
                    System.out.println("Please enter the json value:");
                    String fieldValue = scanner.nextLine().trim();
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(fieldValue);
                    arrayNode.add(jsonNode);
                } else {
                    System.out.println("illegal input!");
                }
            } else if (command.startsWith("delete")) {
                int commandPara = Integer.parseInt(command.split(" ")[1]);
                arrayNode.remove(commandPara);
            } else if (command.startsWith("edit")) {
                int commandPara = Integer.parseInt(command.split(" ")[1]);
                JsonNode node = arrayNode.get(commandPara);
                nextMenu(node);
            } else {
                System.out.println("illegal input!");
            }
        }
    }

    private void nextMenu(JsonNode node) throws IOException {
        if (node.getNodeType() == JsonNodeType.OBJECT) {
            displayObjectNode((ObjectNode) node);
        } else if (node.getNodeType() == JsonNodeType.ARRAY) {
            displayArrayNode((ArrayNode) node);
        } else {
            displayAtomNode(node);
        }
    }

    private void displayAtomNode(JsonNode jsonNode) {
        while (true) {
            String value = jsonNode.asText("null");
            System.out.println("The node value is:");
            System.out.println("\"" + value + "\"");
            System.out.println("Please enter the command:");
            String command = "";
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().trim();
            if (command.equals("exit")) {
                break;
            } else {
                System.out.println("illegal input!");
            }
        }
    }

    public ObjectNode createModel(ObjectNode objectNode) throws Exception {
        System.out.print("...");
        JsonNode type = objectNode.get("@type");
        if (type != null) {
            if (type.asText().trim().equals("Table")) {
                return singleTableModel(objectNode);
            } else if (type.asText().trim().equals("TableGroup")) {
                return groupTableModel(objectNode);
            }
        } else {
            JsonNode tables = objectNode.get("tables");
            if (tables != null) {
                return groupTableModel(objectNode);
            } else {
                return singleTableModel(objectNode);
            }
        }
        return null;
    }

    /**
     * create a single tabular model according to the  objectNode
     *
     * @param objectNode
     * @return
     * @throws Exception
     */
    private ObjectNode singleTableModel(ObjectNode objectNode) throws Exception {
        //parse the file according to objectNode
        ObjectNode singleTable = parseFile(objectNode);
        System.out.print("...");
        //add annotations
        singleTable = addAnnotations(singleTable, objectNode, CSVModel.TABLE);
        System.out.print("...");
        return singleTable;
    }

    private ObjectNode addAnnotations(ObjectNode table, ObjectNode objectNode, CSVModel csvModel) {
        Iterator<String> fieldNames = objectNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldNode = objectNode.get(fieldName);
            if (fieldName.equals("@context") || fieldName.equals("dialect") || fieldName.equals("@type") || fieldName.equals("@id")) {
                ////do nothing
            } else if (fieldName.equals("tableSchema") && csvModel == CSVModel.TABLE) {
                ObjectNode schemaNode = (ObjectNode) objectNode.get(fieldName);

                ArrayNode columnsNode = (ArrayNode) schemaNode.get("columns");
                table.set("columns", columnsNode);
                ArrayNode foreignKeysNode = (ArrayNode) schemaNode.get("foreignKeys");
                table.set("foreignKeys", foreignKeysNode);


                JsonNode primarykeyNode = schemaNode.get("primaryKey");
                if (primarykeyNode != null) {
                    if (primarykeyNode.getNodeType().equals("ARRAY")) {
                        Iterator<JsonNode> primaryIterator = ((ArrayNode) primarykeyNode).iterator();
                        while (primaryIterator.hasNext()) {
                            String primayKey = primaryIterator.next().asText();
                            addPrimaryKey(table, primayKey);
                        }

                    } else {
                        String primayKey = primarykeyNode.asText();
                        addPrimaryKey(table, primayKey);
                    }
                }
                JsonNode rowTitlesNode = schemaNode.get("rowTitles");
                if (rowTitlesNode != null) {
                    if (rowTitlesNode.getNodeType().equals("ARRAY")) {
                        Iterator<JsonNode> rowTitlesIterator = ((ArrayNode) rowTitlesNode).iterator();
                        while (rowTitlesIterator.hasNext()) {
                            String rowTitle = rowTitlesIterator.next().asText();
                            addRowTitles(table, rowTitle);
                        }

                    } else {
                        String rowTitle = rowTitlesNode.asText();
                        addRowTitles(table, rowTitle);
                    }
                }

            } else if (fieldName.contains(":")) {
                String[] names = fieldName.split(":");
                if (context.containsKey(names[0])) {
                    StringBuilder newName = new StringBuilder();
                    newName.append(context.get(names[0]));
                    newName.append(names[1]);
                    table.set(newName.toString(), fieldNode);
                }
            } else {
                table.set(fieldName, fieldNode);
            }
        }
        return table;
    }

    /**
     * create group tabular model according to the objectNode
     *
     * @param objectNode
     * @return
     */
    private ObjectNode groupTableModel(ObjectNode objectNode) throws Exception {
        //create table group model
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tabularGroupModel = objectMapper.createObjectNode();
        ArrayNode tablesNode = tabularGroupModel.putArray("tables");
        System.out.print("...");
        //parse the meta file to build model
        JsonNode tables = objectNode.get("tables");

        JsonNode groupDialect = objectNode.get("dialect");
        JsonNode tableDirection = objectNode.get("tableDirection");
        JsonNode transformations = objectNode.get("transformations");
        JsonNode tableSchema = objectNode.get("tableSchema");
        if (tables != null) {
            Iterator<JsonNode> iterator = ((ArrayNode) tables).iterator();
            while (iterator.hasNext()) {
                ObjectNode iteratorTable = (ObjectNode) iterator.next();
                if (groupDialect != null && iteratorTable.get("dialect") == null) {
                    iteratorTable.set("dialect", groupDialect);
                }
                if (tableDirection != null && iteratorTable.get("tableDirection") == null) {
                    iteratorTable.set("tableDirection", tableDirection);
                }
                if (transformations != null && iteratorTable.get("transformations") == null) {
                    iteratorTable.set("transformations", transformations);
                }
                if (transformations != null && iteratorTable.get("tableSchema") == null) {
                    iteratorTable.set("tableSchema", tableSchema);
                }
                ObjectNode singleTable = singleTableModel(iteratorTable);
                tablesNode.add(singleTable);
            }
        }
        System.out.print("...");
        addAnnotations(tabularGroupModel, objectNode, CSVModel.GROUPTABLE);
        System.out.print("...");
        return tabularGroupModel;

    }

    /**
     * parse the tabular file according to the dialect to get a simple tabular model
     *
     * @param objectNode
     * @return
     * @throws Exception
     */
    private ObjectNode parseFile(ObjectNode objectNode) throws Exception {
        //get the tabular file
        String urlString = objectNode.get("url").asText().trim();
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        //load the dialect
        ObjectNode dialect = (ObjectNode) objectNode.get("dialect");
        if (dialect != null) {
            setDialect(dialect);
        } else {
            setDefaultDialect();
        }

        //1.Create a new table T with the annotations
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode singleTable = objectMapper.createObjectNode();

        int sourceRowNumber = 1;
        //5.Read the file using the encoding
        Reader reader = new InputStreamReader(inputStream, encoding);
        BufferedReader bufferedReader = new BufferedReader(reader);
        //6.Repeat the following the number of times indicated by skip rows
        sourceRowNumber += skipRows;
        for (int i = 0; i < skipRows; i++) {
            bufferedReader.readLine();
        }
        //7.Repeat the following the number of times indicated by header row count
        sourceRowNumber += headerRowCount;
        for (int i = 0; i < headerRowCount; i++) {
            bufferedReader.readLine();
        }
        //9.Set row number to 1.
        int rowNumber = 1;
        //10.While it is possible to read another row, do the following:
        String rowContent;
        while ((rowContent = bufferedReader.readLine()) != null) {
            int sourceColumnNumber = 1;
            if (commentPrefix != null && rowContent.startsWith(commentPrefix)) {
                //do nothing
            } else {
                //get  the   cells
                List<String> cells = parseCells(rowContent);
                if (isEmptyRow(cells) && skipBlankRows == true) {
                    continue;
                } else {
                    //get  the   rows
                    ArrayNode rows = (ArrayNode) singleTable.get("rows");
                    if (rows == null) {
                        rows = singleTable.putArray("rows");
                    }
                    //create a  row
                    ObjectNode rowNode = objectMapper.createObjectNode();
                    rowNode.set("table", singleTable);
                    rowNode.put("number", rowNumber);
                    rowNode.put("sourceNumber", sourceRowNumber);
                    rows.add(rowNode);

                    //get the  columns
                    cells = skipSkipColumns(cells);
                    sourceColumnNumber += skipColumns;
                    ArrayNode columnsNode = (ArrayNode) singleTable.get("columns");
                    if (columnsNode == null) {
                        columnsNode = singleTable.putArray("columns");
                    }
                    for (int i = 0; i < cells.size(); i++) {
                        //create a column
                        ObjectNode columnNode = (ObjectNode) columnsNode.get(i);
                        if (columnNode == null) {
                            columnNode = objectMapper.createObjectNode();
                            columnsNode.add(columnNode);
                        }
                        columnNode.set("table", singleTable);
                        columnNode.put("number", i + 1);
                        columnNode.put("sourceNumber", sourceColumnNumber);
                        columnsNode.add(columnNode);

                        //create a cell
                        ObjectNode dCell = objectMapper.createObjectNode();
                        dCell.set("table", singleTable);
                        dCell.set("column", columnNode);
                        dCell.set("row", rowNode);
                        dCell.put("stringValue", cells.get(i));
                        dCell.put("value", cells.get(i));

                        //add the  cell to row cells  and   column cells
                        ArrayNode cellsNode = (ArrayNode) columnNode.get("cells");
                        if (cellsNode == null) {
                            cellsNode = objectMapper.createArrayNode();
                            columnNode.set("cells", cellsNode);
                        }
                        cellsNode.add(dCell);
                        cellsNode = (ArrayNode) rowNode.get("cells");
                        if (cellsNode == null) {
                            cellsNode = objectMapper.createArrayNode();
                            rowNode.set("cells", cellsNode);
                        }
                        cellsNode.add(dCell);
                        sourceColumnNumber++;
                    }
                }
            }
            sourceRowNumber++;
        }
        //12.Return the table T and the embedded metadata M
        return singleTable;
    }

    private void addRowTitles(ObjectNode table, String rowTitle) {
        ArrayNode columnsNode = (ArrayNode) table.get("columns");
        ArrayNode rowsNode = (ArrayNode) table.get("rows");
        for (int i = 0; i < columnsNode.size(); i++) {
            if (columnsNode.get(i).get("name").equals(rowTitle)) {
                for (int j = 0; j < rowsNode.size(); j++) {
                    ArrayNode titlesRow = (ArrayNode) rowsNode.get(j).get("titles");
                    if (titlesRow == null) {
                        titlesRow = ((ObjectNode) rowsNode.get(i)).putArray("titles");
                    }
                    titlesRow.add(columnsNode.get(i));
                }
            }
        }
    }

    private void addPrimaryKey(ObjectNode table, String primayKey) {
        int primaryKeyColNum = -1;
        ArrayNode columnsNode = (ArrayNode) table.get("columns");
        for (int i = 0; i < columnsNode.size(); i++) {
            ObjectNode columnNode = (ObjectNode) columnsNode.get(i);
            String columnName = columnNode.get("name").asText();
            if (columnName.equals(primayKey)) {
                primaryKeyColNum = i;
                break;
            }
        }

        ArrayNode rowsNode = (ArrayNode) table.get("rows");
        for (int j = 0; j < rowsNode.size(); j++) {
            ObjectNode rowNode = (ObjectNode) rowsNode.get(j);
            ArrayNode primaryKeyRow = (ArrayNode) rowNode.get("primaryKey");
            if (primaryKeyRow == null) {
                primaryKeyRow = ((ObjectNode) rowNode).putArray("primaryKey");
            }
            primaryKeyRow.add(rowNode.get("cells").get(primaryKeyColNum));
        }

    }

    private void removePrefix(ObjectNode objectNode) {
        //deal with  common  properties
        Iterator<String> iterator = objectNode.fieldNames();
        List<String> fieldNames = new ArrayList<String>();
        while (iterator.hasNext()) {
            fieldNames.add(iterator.next());
        }
        for (String oldName : fieldNames) {
            JsonNode oldNode = objectNode.get(oldName);
            if (oldNode.getNodeType() == JsonNodeType.OBJECT) {
                removePrefix((ObjectNode) oldNode);
            }
            if (oldName.contains(":")) {
                String[] names = oldName.split(":");
                if (context.containsKey(names[0])) {
                    StringBuilder newName = new StringBuilder();
                    newName.append(context.get(names[0]));
                    newName.append(names[1]);
                    objectNode.set(newName.toString(), oldNode);
                    objectNode.remove(oldName);
                }
            }
        }

    }

    private boolean isEmptyRow(List<String> cells) {
        for (String cell : cells) {
            if (!"".equals(cell)) {
                return false;
            }
        }
        return true;
    }


    private List<String> parseCells(String rowContent) throws Exception {
        List<String> listCells = new ArrayList<String>();
        StringBuilder currenctCellValue = new StringBuilder();
        boolean quoted = false;
        char[] chars = rowContent.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == escapeCharacter.charAt(0)) {
                currenctCellValue.append(chars[i + 1]);
                i++;
            } else if (chars[i] == quoteChar.charAt(0)) {
                if (quoted == false) {
                    quoted = true;
                    if (currenctCellValue.length() > 0) {
                        throw new Exception(" currentCellValue is no empty! ");
                    }
                } else {
                    quoted = false;
                    if (chars[i + 1] != delimiter.charAt(0)) {
                        throw new Exception(" the end quoter is not ended with delimiter! ");
                    }
                }
            } else if (chars[i] == delimiter.charAt(0)) {
                if (quoted == true) {
                    currenctCellValue.append(chars[i]);
                } else {
                    String ce = conditionallyTrim(currenctCellValue);
                    listCells.add(ce);
                    currenctCellValue = new StringBuilder();
                }
            } else {
                currenctCellValue.append(chars[i]);
            }
        }
        String ce = conditionallyTrim(currenctCellValue);
        listCells.add(ce);
        return listCells;
    }

    private String conditionallyTrim(StringBuilder currenctCellValue) {
        if (trim.equals("true") || trim.equals("start")) {
            for (int i = 0; i < currenctCellValue.length(); i++) {
                if (currenctCellValue.charAt(i) == " ".charAt(0)) {
                    currenctCellValue.deleteCharAt(i);
                } else {
                    break;
                }
            }
        }
        if (trim.equals("true") || trim.equals("end")) {
            for (int i = currenctCellValue.length() - 1; i >= 0; i--) {
                if (currenctCellValue.charAt(i) == " ".charAt(0)) {
                    currenctCellValue.deleteCharAt(i);
                } else {
                    break;
                }
            }
        }
        return currenctCellValue.toString();
    }

    private List<String> skipSkipColumns(List<String> cells) {
        for (int i = 0; i < skipColumns; i++) {
            cells.remove(i);
        }
        return cells;
    }
}
