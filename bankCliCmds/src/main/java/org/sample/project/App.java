package org.sample.project;

import org.sample.readAndParse.ReadCmd;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        ReadCmd r = new ReadCmd();
        r.readInputCmd();
    }
}
