package de.web.f_reissmann.recording.format.selection;

import de.web.f_reissmann.recording.Recording;
import de.web.f_reissmann.recording.format.RecordingFormat;

import java.util.Comparator;

/**
 * /**
 * This is the ABC for defining a strategy which is used to determine what {@link RecordingFormat} should be preferred
 * for the downloaded {@link Recording}s.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public abstract class BaseFormatSelectionStrategy {

    /**
     * Selects the format from the given {@link Recording}.
     * Therefore uses the {@link Comparator} provided by the{@link #getSelectionComparator()}-method to sort all
     * available {@link RecordingFormat}s and select the first.
     * <p>
     * This is the <code>template-method</code> for the {@link RecordingFormat} selection.
     *
     * @param recording the {@link Recording} to select the format from
     * @return the {@link RecordingFormat} which was selected by the strategy
     * @throws IllegalStateException, if the recording did not have any available formats
     */
    public final RecordingFormat selectFormat(Recording recording) {
        return recording.getAvailableFormats()
                .stream()
                .sorted(getSelectionComparator())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Implements the sorting mechanism to determine the preferred order of all available {@link RecordingFormat}s.
     *
     * @return a {@link Comparator} which is used by the <code>template-method</code>
     */
    protected abstract Comparator<RecordingFormat> getSelectionComparator();
}
