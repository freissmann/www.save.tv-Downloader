package de.web.f_reissmann.recording.format.selection;

import com.google.common.collect.ComparisonChain;
import de.web.f_reissmann.recording.format.RecordingFormat;

import java.util.Comparator;

/**
 * The strategy for selecting a {@link RecordingFormat}:
 * 1. Prefer cut (ad-free)
 * 2. Prefer best quality
 * 3. Then, anything else
 * <p>
 * So the order is:
 * - Ad-free HD
 * - Ad-free SD
 * - Ad-free MOBILE
 * - HD
 * - SD
 * - MOBILE
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public class CutBestQualityFirst extends BaseFormatSelectionStrategy {

    @Override
    protected Comparator<RecordingFormat> getSelectionComparator() {
        return (o1, o2) -> ComparisonChain.start()
                .compareTrueFirst(o1.isCut(), o2.isCut())
                .compare(o2.getQualityCode(), o1.getQualityCode())
                .result();
    }
}
