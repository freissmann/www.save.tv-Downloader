package de.web.f_reissmann.url;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility-Class used for encoding a String in a safely manner using the {@link URLEncoder}.
 *
 * @author Fabian Reißmann
 * @since 24.02.2017
 */
public final class URLEncoderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLEncoderUtil.class);

    private URLEncoderUtil() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Encodes the given String in a safely manner to {@link StandardCharsets#UTF_8}.
     *
     * @param stringToEncode the String to encode
     * @return encoded String
     */
    public static String encodeSafely(String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // Should never happen, since we are using constant here
            LOGGER.error("Somehow the constant UTF-8 encoding threw an exception", e);
        }
        return stringToEncode;
    }
}
