package kfs.kfsstats.utils;

/**
 *
 * @author pavedrim
 */
public class KfsStatException extends RuntimeException{

    public KfsStatException(String msg, Throwable w) {
        super(msg, w);
    }
}
