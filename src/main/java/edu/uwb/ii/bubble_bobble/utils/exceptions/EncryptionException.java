package edu.uwb.ii.bubble_bobble.utils.exceptions;

public class EncryptionException extends Exception  {

    EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
