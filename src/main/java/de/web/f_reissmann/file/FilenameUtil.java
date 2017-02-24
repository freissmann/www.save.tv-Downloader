package de.web.f_reissmann.file;

import de.web.f_reissmann.url.URLEncoderUtil;

/**
 * Utility-Class for sanitizing a String to be a valid filename.
 *
 * @author Fabian Reißmann
 * @since 23.02.2017
 */
public final class FilenameUtil {

    private FilenameUtil() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Converts the given String to another one which can safely be used as a filename.
     *
     * @param filename the filename to sanitize
     * @return a valid filename
     */
    public static String sanitize(String filename) {
        return new Sanitizer(filename)
                .sanitizeUmlaute()
                .encodeUnknownChars()
                .finish();
    }

    /**
     * Used for sanitizing a filename.
     * <p>
     * Provides a fluent style API. Subsequent calls of the same sanitizing method are <code>idempotent</code>
     */
    private static class Sanitizer {

        /**
         * Used for replacing german "Umlaute" with corresponding chars.
         */
        private static final String[][] UMLAUT_REPLACEMENTS = {
                {"Ä", "Ae"},
                {"Ü", "Ue"},
                {"Ö", "Oe"},
                {"ä", "ae"},
                {"ü", "ue"},
                {"ö", "oe"},
                {"ß", "ss"}
        };

        private String filenameToSanitize;

        private Sanitizer(String filenameToSanitize) {
            this.filenameToSanitize = filenameToSanitize;
        }

        /**
         * Replaces all "Umlaute" which other chars.
         *
         * @return <code>this</code>, for fluent style
         */
        private Sanitizer sanitizeUmlaute() {
            for (String[] umlauts : UMLAUT_REPLACEMENTS) {
                filenameToSanitize = filenameToSanitize.replace(umlauts[0], umlauts[1]);
            }
            return this;
        }

        /**
         * Encodes any char which could not be used in URLs (and therefore are invalid for filenames as well).
         *
         * @return <code>this</code>, for fluent style
         */
        private Sanitizer encodeUnknownChars() {
            filenameToSanitize = URLEncoderUtil.encodeSafely(filenameToSanitize);
            return this;
        }

        private String finish() {
            return filenameToSanitize;
        }
    }

}
