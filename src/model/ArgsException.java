package model;

/**
 * Exception to check the number of arguments passed to the program.
 * @author Shivani Singh, ss5243@rit.edu
 * @author Ronit Boddu, rb1209@rit.edu
 */
public class ArgsException extends QTException{
    /**
     * Constructor to pass the message to super class.
     * @param message message string.
     */
    public ArgsException(String message) {
        super(message);
    }
}
