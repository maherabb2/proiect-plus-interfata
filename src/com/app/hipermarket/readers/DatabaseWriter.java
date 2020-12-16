package com.app.hipermarket.readers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseWriter {
    private final static String DATABASE = "database/";
    private final String fileName;

    public DatabaseWriter(String fileName) {
        this.fileName = DATABASE + fileName;
    }

    public boolean write(String value) {
//        System.out.println(this.getClass() + ": Called write with \'" + value + "\' on " + fileName + ".");

        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(value);
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println(this.getClass() + ": Failed to write " + value + "in" + fileName + ".");
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeAll(ArrayList list) {
        System.out.println(this.getClass() + ": Called write with \'" + list + "\' on " + fileName + ".");

        try {
            FileWriter writer = new FileWriter(fileName);

            for (Object element : list) {
                writer.write(element.toString());
            }

            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println(this.getClass() + ": Failed to write " + list + "in" + fileName + ".");
            e.printStackTrace();
            return false;
        }
    }

    public void allowAccess(boolean access) {
        File file = new File(fileName);
        file.setReadable(access);
    }
}
