/***********************************
 * Course: Lehigh CSE017-SU2024
 * Assignment: Final (v01)
 * Name: Yinglong Lin
 * UID: yile22
 * **********************************/

public class CountryFmtException extends Exception {
    private String badString = null;

    // Constructor with message and badString
    public CountryFmtException(String message, String badString) {
        super(message);
        this.badString = badString;
    }

    // Constructor with message, badString, and Throwable
    public CountryFmtException(String message, String badString, Throwable t) {
        super(message, t);
        this.badString = badString;
    }

    // Getter for badString
    public String getBadString() {
        return badString;
    }
}