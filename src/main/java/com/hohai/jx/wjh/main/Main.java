package com.hohai.jx.wjh.main;

import com.hohai.jx.wjh.services.CSVParser;


import java.io.*;
import java.util.Scanner;

/**
 * Created by wjh on 2016/1/16.
 */
public class Main {
    //D:/tree-ops.csv-metadata.json
    public static final String DISLAY_DELIMETER = "\t";
    public static void main(String[] args) {
        String metaFilePath = null;
        System.out.println("Please enter the metaData file path:");
        Scanner scanner = new Scanner(System.in);
        metaFilePath = scanner.nextLine().trim();
        CSVParser csvParser = null;
        if( metaFilePath != null){
            try {
                csvParser = new CSVParser(metaFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            System.out.println();
            System.out.println();
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("\\\\MAIN MENU");
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("1. modify meta data");
            System.out.println("2. create tabular data model");
            System.out.println("3. display tabular data model");
            System.out.println("Please enter (1\\2\\3): ");
            int order = scanner.nextInt();

            if ( order == 1 && csvParser != null ) {
                try {
                    csvParser.displayMetadata();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if ( order == 2 && csvParser != null ) {
                try {
                    csvParser.createTabularModel();
                    System.out.println("creation completed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if ( order == 3 && csvParser != null ) {
                csvParser.displayTabularModel();
            }
        }
    }
}
