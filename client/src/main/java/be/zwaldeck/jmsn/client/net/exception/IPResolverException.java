package be.zwaldeck.jmsn.client.net.exception;

public class IPResolverException extends Exception {

    public IPResolverException() {
        super("Unable to resolve your public IP. Are you connected to the internet?");
    }
}
