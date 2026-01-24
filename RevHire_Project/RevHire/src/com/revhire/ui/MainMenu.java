package com.revhire.ui;

import java.util.Scanner;
import com.revhire.service.AuthService;

public class MainMenu {
    public void start() {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            System.out.println("\n=== REVHIRE ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");


            int ch = sc.nextInt();
            switch (ch) {
                case 1 -> auth.register(sc);
                case 2 -> auth.login(sc);
                case 3 -> auth.forgotPassword(sc);
                case 4 -> auth.changePassword(sc);
                case 5 -> System.exit(0);
            }
        }
    }
}