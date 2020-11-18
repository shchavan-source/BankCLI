package org.ocbc.data;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name = null;
    private Double balance = null;
    private ArrayList<Loan> arrLoan = null;

    public User(String name, Double d) {
        this.setName(name);
        this.setBalance(d);
        arrLoan = new ArrayList<Loan>();
    }

    public ArrayList<Loan> getArrLoan() {
        return arrLoan;
    }

    public void setArrLoan(ArrayList<Loan> arrLoan) {
        this.arrLoan = arrLoan;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return new StringBuffer("Name: ").append(this.name).append(" Balance: ").append(this.balance).append("Loan: ").append(this.arrLoan).toString();

    }

}
