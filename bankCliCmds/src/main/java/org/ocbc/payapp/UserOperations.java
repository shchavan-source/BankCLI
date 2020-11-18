package org.ocbc.payapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.System.Logger.Level;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.ocbc.data.Loan;
import org.ocbc.data.User;

public class UserOperations {

    private User loggedInUser;
    private User payUser;
    static Logger logger = Logger.getLogger("PayAnyone");
    Handler fileHandler = null;

    public UserOperations(){
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

    public void login(String[] cmds) {
        loggedInUser = getUser(cmds[1]);
        System.out.println("Hello, " + loggedInUser.getName() + "!");
        printUserBalanceDetails();

    }

    public void topup(String[] cmds) {
        loggedInUser.setBalance(loggedInUser.getBalance() + Double.valueOf(cmds[1]));
        if (null != loggedInUser.getArrLoan() && loggedInUser.getArrLoan().size() > 0) {
            for (int i = 0; i < loggedInUser.getArrLoan().size(); i++) {
                if(loggedInUser.getArrLoan().get(i).getAmount() < 0){
                    String[] payCmds = new String[3];
                    if(loggedInUser.getBalance() + loggedInUser.getArrLoan().get(i).getAmount() < 0){
                        payCmds[0] = "pay";
                        payCmds[1] = loggedInUser.getArrLoan().get(i).getUser();
                        payCmds[2] = "0";
                    }else {
                        payCmds[0] = "pay";
                        payCmds[1] = loggedInUser.getArrLoan().get(i).getUser();
                        payCmds[2] = String.valueOf(loggedInUser.getBalance() + loggedInUser.getArrLoan().get(i).getAmount());
                    }
                    pay(payCmds);
                }
            }
        }
        System.out.println("Your balance is " + loggedInUser.getBalance());
        writeObjectToFile("users/"+loggedInUser.getName(), loggedInUser);
    }

    public void pay(String[] cmds) {
        payUser = getUser(cmds[1]);
        double payAmt = Double.valueOf(cmds[2]);
        double loggedInUserAmt = loggedInUser.getBalance();
        double transferAmt = loggedInUserAmt;
        
        if (null != loggedInUser.getArrLoan() && loggedInUser.getArrLoan().size() > 0 && loggedInUser.getArrLoan().contains(new Loan(0.0, payUser.getName()))){
            double settlementAmt = 0.0;
            for (int i = 0; i < loggedInUser.getArrLoan().size(); i++) {
                Loan tmpLoan = loggedInUser.getArrLoan().get(i);
                if(tmpLoan.getUser().equalsIgnoreCase(payUser.getName())){
                    if(tmpLoan.getAmount() > 0){
                        settlementAmt=tmpLoan.getAmount()-payAmt;
                        if(settlementAmt < 0){
                            settlementAmt=loggedInUserAmt-settlementAmt;
                        }
                        transferAmt = 0.0;
                    } else {
                        settlementAmt = loggedInUserAmt+tmpLoan.getAmount()-payAmt;
                        if (settlementAmt >= 0){
                            transferAmt = -(tmpLoan.getAmount());
                            loggedInUser.setBalance(loggedInUserAmt+tmpLoan.getAmount());
                            payUser.setBalance(settlementAmt+payUser.getBalance());
                            settlementAmt=0.0;
                        }else {
                            payUser.setBalance(loggedInUserAmt+payUser.getBalance());
                        }
                        
                    }
                    if (settlementAmt < 0){
                        loggedInUser.setBalance(0.0);
                    }
                    loggedInUser.getArrLoan().get(i).setAmount(settlementAmt);
                    break;
                }
            }

            for (int i = 0; i < payUser.getArrLoan().size(); i++) {
                Loan tmpLoan = payUser.getArrLoan().get(i);
                if(tmpLoan.getUser().equalsIgnoreCase(loggedInUser.getName())) {
                    payUser.getArrLoan().get(i).setAmount(-settlementAmt);
                }
            }
        } else {
            if (loggedInUserAmt >= payAmt) {
                loggedInUser.setBalance(loggedInUserAmt - payAmt);
                payUser.setBalance(payUser.getBalance()+payAmt);
                transferAmt = payAmt;
            } else {
                loggedInUser.setBalance(0.0);
                loggedInUser.getArrLoan().add(new Loan(loggedInUserAmt-payAmt, payUser.getName()));

                payUser.setBalance(payUser.getBalance()+loggedInUserAmt);
                payUser.getArrLoan().add(new Loan(-(loggedInUserAmt-payAmt), loggedInUser.getName()));
            }
        }

        cleanUsers(loggedInUser);
        cleanUsers(payUser);
        System.out.println("Transferred " + transferAmt + " to " + payUser.getName());
        printUserBalanceDetails();

        writeObjectToFile("users/"+loggedInUser.getName(), loggedInUser);
        writeObjectToFile("users/"+payUser.getName(), payUser);
    }

    private void printUserBalanceDetails() {
        System.out.println("Your balance is " + loggedInUser.getBalance());
        for (int i = 0; i < loggedInUser.getArrLoan().size(); i++) {
            if (loggedInUser.getArrLoan().get(i).getAmount() > 0){
                System.out.println("Owing " + loggedInUser.getArrLoan().get(i).getAmount() + " from " + loggedInUser.getArrLoan().get(i).getUser());
            } else {
                System.out.println("Owing " + -loggedInUser.getArrLoan().get(i).getAmount() + " to " + loggedInUser.getArrLoan().get(i).getUser());
            }
        }
    }

    private void cleanUsers(User cleanLoanUser) {
        for (int i = 0; i < cleanLoanUser.getArrLoan().size(); i++) {
            if (cleanLoanUser.getArrLoan().get(i).getAmount() == 0.0){
                cleanLoanUser.getArrLoan().remove(i);
            }
        }
    }

    private User getUser(String usr) {
        User tmpUsr;
        File userFile = new File("users/" + usr);
        if (userFile.exists()) {
            // System.out.println(usr + "exists.");
            logger.doLog(Level.INFO, usr + " exists.");
            tmpUsr = populateLoggedInUser("users/" + usr);
        } else {
            tmpUsr = new User(usr, 0.0);
            writeObjectToFile("users/" + usr, tmpUsr);
        }

        return tmpUsr;
    }

    private User populateLoggedInUser(String filePath) {
        Object obj = null;
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            obj = objectIn.readObject();

            // System.out.println("The Object has been read from the file");
            
            objectIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (User) obj;
        
    }

    private void writeObjectToFile(String filePath, User user) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(user);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
