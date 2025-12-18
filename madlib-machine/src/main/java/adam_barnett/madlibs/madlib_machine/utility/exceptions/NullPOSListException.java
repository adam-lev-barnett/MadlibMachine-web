package adam_barnett.madlibs.madlib_machine.utility.exceptions;

public class NullPOSListException extends Exception {
    public NullPOSListException(String message) {
        super("Cannot prompt user for replacement words with null POS list");
    }
}
