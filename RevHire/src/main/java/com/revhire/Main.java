package com.revhire;

import com.revhire.ui.MainMenu;

public class Main {

    public static void main(String[] args) {

        try {
            new MainMenu().start();
        } catch (Exception e) {
            System.out.println("Application terminated unexpectedly. Please restart.");
        }
    }
}
