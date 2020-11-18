package org.ocbc.data;

import java.io.Serializable;

public class Loan implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -878230835944366177L;

    private double amount;
    private String user;

    public Loan(double amount, String user) {
        this.setAmount(amount);
        this.setUser(user);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString(){
        return new StringBuffer().append("User: ").append(this.user).append("Amount: ").append(this.amount).toString();
    }

    @Override
    public boolean equals(Object obj){
        boolean retVal = false;
        if(obj instanceof Loan){
            Loan tmp = (Loan) obj;
            retVal = tmp.getUser().equalsIgnoreCase(this.user);
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.user != null ? this.user.hashCode() : 0);
        return hash;
    }
    
}
