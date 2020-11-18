package org.ocbc.payapp;

public class PayAnyone {

    static ReadOperations readOps;

    public static void main(String[] args) {
        System.out.println("Pay AnyOne Application");
    
        readOps = new ReadOperations();
        while(true){
            String inputCmd = readOps.readInputs();
            if ("exit".equalsIgnoreCase(inputCmd)){
                break;
            }else{
                readOps.parseInput(inputCmd);
            }
        }

    }
}
