package model;

/**
 *  A class used to communicate errors with pixels out of range
 * @author Shivani Singh, ss5243@rit.edu
 * @author Ronit Boddu, rb1209@rit.edu
 */
public class OutOfRangePixel extends QTException{
    /**
     * Constructor to pass the message to super class.
     * @param message message string.
     */
    public OutOfRangePixel(String message) {
        super(message);
    }
}
