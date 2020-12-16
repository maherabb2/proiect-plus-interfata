package com.app.hipermarket.readers;

import com.app.hipermarket.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DatabaseReader {

    public char simpleRead(String fileName) {
        String path = Constants.DATABASE + fileName;
        char rezultat = ' ';

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            rezultat = line.charAt(0);
            scanner.close();
        }
        catch (FileNotFoundException ex) {
            rezultat = ' ';
        }

        if (rezultat != ' ') {
            DatabaseWriter writer = new DatabaseWriter(fileName);
            writer.write(" ");
        }

        return  rezultat;
    }

    public StringBuilder read(String fileName) {
        String path = Constants.DATABASE + fileName;

//        System.out.println(this.getClass() + ": Called read on " + path + ".");

        StringBuilder rezultat = new StringBuilder();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line != null) {
                    rezultat.append(line);
                }

                if (scanner.hasNextLine()) {
                   rezultat.append("\n");
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            rezultat.append(" ");
        } catch (Exception e) {
            System.out.println(this.getClass() + ": Failed to read " + path + ".");
            e.printStackTrace();
        }

        if (fileName.contains(Constants.MESSAGE_FILE) || fileName.contains(Constants.OUTPUT_FILE)) {
            DatabaseWriter writer = new DatabaseWriter(fileName);
            writer.write(" ");
        }

        return rezultat;
    }
}
