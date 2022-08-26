package model;

/**
 * Exception to check whether to the number of pixels is square or not.
 * @author Shivani Singh, ss5243@rit.edu
 * @author Ronit Boddu, rb1209@rit.edu
 */
public class ImageSizeException extends QTException{
    /**
     * Constructor to pass the message to super class.
     * @param message message string.
     */
    public ImageSizeException(String message) {
        super(message);
    }
}
