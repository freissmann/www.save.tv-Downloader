package de.web.f_reissmann.recording.format.selection;

/**
 * Utility-Class for creating a {@link BaseFormatSelectionStrategy}.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public final class SelectionStrategyFactory {

    private SelectionStrategyFactory() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Creates the strategy for selecting the {@link de.web.f_reissmann.recording.format.RecordingFormat}
     * from a {@link de.web.f_reissmann.recording.Recording}.
     *
     * @param order the {@link Order} which the resulting strategy should obey
     * @return a {@link BaseFormatSelectionStrategy} for selecting the format of the downloaded recording
     */
    public static BaseFormatSelectionStrategy create(Order order) {
        switch (order) {
            case CUT_BEST_QUALITY:
                return new CutBestQualityFirst();
            case CUT_LOWEST_QUALITY:
                return new CutLowestQualityFirst();
            default:
                throw new IllegalStateException("Should not be happen, since we are using enum here");
        }
    }

    /**
     * The possible orders.
     */
    public enum Order {
        // At first cut and with best possible quality
        CUT_BEST_QUALITY,
        // At first cut and with lowest possible quality
        CUT_LOWEST_QUALITY
    }
}
