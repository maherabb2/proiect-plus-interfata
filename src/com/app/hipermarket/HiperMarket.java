package com.app.hipermarket;

import com.app.hipermarket.menu.MainMenu;
import com.app.hipermarket.menu.Menu;
import com.app.hipermarket.readers.DatabaseReader;

import java.io.File;

public class HiperMarket {
    private String database;

    public HiperMarket(String database) {
        this.database  = database;
    }

    public boolean isValidConfiguration() {
        boolean validDb = false;

        File dbDirectory = new File(database);
        if (dbDirectory.exists() && dbDirectory.isDirectory()) {
            validDb = true;
        }

        return validDb;
    }

    public void startApplication() {
        Menu menu = new MainMenu();
        DatabaseReader databaseReader = new DatabaseReader();

        do {
            if (menu == null) {
                break;
            }

            char value = ' ';
            try {
                value = databaseReader.simpleRead(Constants.INPUT_FILE);
            }
            catch (Exception ex) {
                System.out.println("An exception occured!");
            }

            if (value != ' ') {
                menu.display();
                System.out.println("Enter a commmand: " + value);
                menu = menu.interpretCommand(value);
            }
        } while (true);
    }
}