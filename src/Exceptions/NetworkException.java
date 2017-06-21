package Exceptions;

import java.io.IOException;

/**
 * Created by Michele on 28/05/2017.
 */
public class NetworkException extends IOException {

    public NetworkException() {super();}

    public NetworkException( String message ) { super(message);}

    public NetworkException(Throwable e) {super(e);}
}
