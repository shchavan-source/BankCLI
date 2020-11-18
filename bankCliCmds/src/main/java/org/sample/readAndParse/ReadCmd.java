package org.sample.readAndParse;

import java.io.IOException;
import java.util.Scanner;

public class ReadCmd {

    public void readInputCmd() throws IOException {
        Scanner readConsole = new Scanner(System.in);
        
        String s = readConsole.nextLine();
        System.out.println(s);
    }
}
