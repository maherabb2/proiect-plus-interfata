package com.app.hipermarket.persoane;

import java.util.ArrayList;

public class Admins {
    private ArrayList<Admin> admins;

    public Admins(ArrayList<Admin> admins) {
        this.admins = admins;
    }

    public Admin findAdmin(String username) {
        for(Admin admin: admins) {
            if(admin.getUser().equals(username)) {
                return admin;
            }
        }

        return null;
    }
}
