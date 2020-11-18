package org.ocbc.payapp;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadOperations {

    private UserOperations userOps;
    static Logger logger = Logger.getLogger("PayAnyone");
    Handler fileHandler = null;

    public ReadOperations() {
        userOps = new UserOperations();
        try {
            fileHandler = new FileHandler("logger/payAnyone.log", 20000, 5);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readInputs(){
        System.out.print("> ");
        Scanner in = new Scanner(System.in);
        String tmp = in.nextLine();
        return tmp;
    }

    public void parseInput(String cmd){
        String[] cmdArr = cmd.split(" ");
        switch (cmdArr[0]){
            case "login":
                // System.out.println("Login command");
                logger.log(Level.INFO, "Login command");
                userOps.login(cmdArr);
                break;
            case "pay":
                // System.out.println("Pay command");
                logger.log(Level.INFO, "Pay command");
                userOps.pay(cmdArr);
                break;
            case "topup":
                // System.out.println("Topup command");
                logger.log(Level.INFO, "Topup command");
                userOps.topup(cmdArr);
                break;
            default:
                System.out.println("Invalid command");
                logger.log(Level.INFO, "Invalid command");
                break;
        }
    }
}
