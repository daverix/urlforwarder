package net.daverix.urlforward;

/**
 * Created by daverix on 12/23/13.
 */
public class UriCombinerException extends Exception {
    public UriCombinerException() {
    }

    public UriCombinerException(String detailMessage) {
        super(detailMessage);
    }

    public UriCombinerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UriCombinerException(Throwable throwable) {
        super(throwable);
    }
}
