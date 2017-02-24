package de.web.f_reissmann.recording.format;

import de.web.f_reissmann.recording.Recording;
import de.web.f_reissmann.recording.format.selection.SelectionStrategyFactory;

import java.util.stream.Stream;

/**
 * Represents one possible format in which a {@link Recording} can be downloaded.
 * <p>
 * Instances of this class are <code>strictly immutable</code>.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public class RecordingFormat {

    /**
     * Whether or not the downloaded {@link Recording} will be cut (which means is free of ads).
     */
    private final boolean isCut;

    /**
     * The quality in which the {@link Recording} will be downloaded.
     */
    private final Quality quality;

    /**
     * Ctor.
     *
     * @param isCut   if the format is without advertisement.
     * @param quality the quality for this {@link RecordingFormat}
     */
    public RecordingFormat(boolean isCut, Quality quality) {
        this.isCut = isCut;
        this.quality = quality;
    }

    public boolean isCut() {
        return isCut;
    }

    /**
     * The quality code is the internal code used by save.tv
     *
     * @return the quality-code
     */
    public int getQualityCode() {
        return quality.getFormatCode();
    }

    public String getQualityName() {
        return quality.name();
    }

    public static RecordingFormat getPreferredFormat(Recording recording, String formatSelection) {
        SelectionStrategyFactory.Order selectionOrder = SelectionStrategyFactory.Order.valueOf(formatSelection);

        return SelectionStrategyFactory.create(selectionOrder)
                .selectFormat(recording);
    }

    /**
     * Defines the quality in which a {@link Recording} can be downloaded.
     * <p>
     * These qualities are provided by SaveTv.
     *
     * @author Fabian Reißmann
     * @since 19.02.2017
     */
    public enum Quality {
        MOBILE(4),
        SD(5),
        HD(6);

        private final int formatCode;

        Quality(int formatCode) {
            this.formatCode = formatCode;
        }

        private int getFormatCode() {
            return formatCode;
        }

        /**
         * Returns the <code>enum</code> which corresponds to the given quality-code.
         *
         * @param codeToCheck the quality-code which should be checked
         * @return the corresponding {@link Quality}
         * @throws IllegalArgumentException if there is no {@link Quality} for that code
         */
        public static Quality fromCode(int codeToCheck) {
            return Stream.of(Quality.values())
                    .filter(quality -> quality.getFormatCode() == codeToCheck)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
