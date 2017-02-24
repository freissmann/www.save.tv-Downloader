package de.web.f_reissmann.recording;

import de.web.f_reissmann.recording.format.RecordingFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility-Class used for formatting a recording as raw-filename String.
 *
 * @author Fabian Reißmann
 * @since 23.02.2017
 */
public final class RecordingUtil {

    private RecordingUtil() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Uses the given {@link Recording} to create a raw filename String.
     * <p>
     * Attention:
     * Raw means, that there might be characters which are not allowed to be present in a filename
     *
     * @param recording the {@link Recording} to use for creating the filename
     * @param format    the {@link RecordingFormat} to use for creating the filename
     * @return a raw filename String
     */
    public static String toFilename(Recording recording, RecordingFormat format) {
        StringBuilder builder = new StringBuilder()
                .append(recording.getTitle());

        tryAppend(builder, recording.getEpisode());
        tryAppend(builder, recording.getSubTitle());
        tryAppend(builder, format.getQualityName());
        tryAppend(builder, String.valueOf(recording.getTelecastId()));

        return builder.toString()
                .replaceAll(" ", "_");
    }

    private static void tryAppend(StringBuilder builder, String title) {
        if (StringUtils.isNotEmpty(title)) {
            builder.append('-');
            builder.append(title);
        }
    }
}
