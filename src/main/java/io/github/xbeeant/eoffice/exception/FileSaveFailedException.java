package io.github.xbeeant.eoffice.exception;

/**
 * @author xiaobiao
 * @version 2021/11/4
 */
public class FileSaveFailedException extends RuntimeException{

    public FileSaveFailedException(Exception e) {
        super(e);
    }

    public FileSaveFailedException() {
    }
}

