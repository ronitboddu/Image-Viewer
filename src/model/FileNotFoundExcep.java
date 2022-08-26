package model;

/**
 * Exception to check if file exists or not.
 * @author Shivani Singh, ss5243@rit.edu
 * @author Ronit Boddu, rb1209@rit.edu
 */
public class FileNotFoundExcep extends QTException{
    /**
     * Constructor to pass the message to super class.
     * @param message message string.
     */
    public FileNotFoundExcep(String message) {
        super(message);
    }
}
