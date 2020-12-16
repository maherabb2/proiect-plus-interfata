package com.app.hipermarket.parser;

import com.app.hipermarket.persoane.Admin;

import java.util.ArrayList;

public class AdminParser {
    public Admin parse(String rawData) {
        Admin admin = null;

        if (rawData != null && !rawData.isEmpty()) {
            String[] items = rawData.split(";");

            String username = items[0];
            String password = items[1];
            admin = new Admin(username, password);
        }

        return admin;
    }

    public ArrayList<Admin> parseAll(String data) {
        ArrayList<Admin> admins = new ArrayList<>();

        for (String element: data.split("\n")) {
            Admin admin = parse(element);
            if(admin != null) {
                admins.add(admin);
            }
        }

        return admins;
    }
}
