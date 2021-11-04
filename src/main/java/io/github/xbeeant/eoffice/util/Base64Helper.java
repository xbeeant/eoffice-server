package io.github.xbeeant.eoffice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * @author xiaobiao
 * @version 2021/7/3
 */
public class Base64Helper {
    private static final Logger logger  = LoggerFactory.getLogger(Base64Helper.class);
    /**
     * Decode the value using Base64.
     * @param base64Value the Base64 String to decode
     * @return the Base64 decoded value
     * @since 1.2.2
     */
    public static String base64Decode(String base64Value) {
        try {
            byte[] decodedCookieBytes = Base64.getDecoder().decode(base64Value);
            return new String(decodedCookieBytes);
        }
        catch (Exception ex) {
            logger.debug("Unable to Base64 decode value: " + base64Value);
            return null;
        }
    }

    /**
     * Encode the value using Base64.
     * @param value the String to Base64 encode
     * @return the Base64 encoded value
     * @since 1.2.2
     */
    public static String base64Encode(String value) {
        byte[] encodedCookieBytes = Base64.getEncoder().encode(value.getBytes());
        return new String(encodedCookieBytes);
    }
}
