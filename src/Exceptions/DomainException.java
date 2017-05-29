package Exceptions;

import java.io.IOException;

/**
 * Created by Michele on 28/05/2017.
 */
public class DomainException extends IOException {

    public DomainException() { super(); }

    public DomainException(String message) { super(message); }
}
