package io.github.xbeeant.eoffice.exception;

/**
 * @author xiaobiao
 * @version 2021/11/22
 */
public class ResourceMissingException extends RuntimeException{

    public ResourceMissingException(String message, Exception e) {
        super(message, e);
    }

    public ResourceMissingException(String message) {
        super(message);
    }
}
