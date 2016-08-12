package net.daverix.urlforward;

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
