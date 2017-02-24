package de.web.f_reissmann.connection;

import de.web.f_reissmann.recording.Recording;
import de.web.f_reissmann.recording.format.RecordingFormat;
import de.web.f_reissmann.url.URLEncoderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

/**
 * Utility-Class for creating the {@link URI}s used for accessing save.tv.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
final class SaveTvUriUtils {

    private static final String SCHEME = "https";
    private static final String HOST = "www.save.tv";

    private static final String NOT_SET = null;
    private static final String NO_SEARCH = "";

    private SaveTvUriUtils() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Creates the {@link URI} used for logging in.
     *
     * @return an {@link URI}
     */
    static URI loginUri() {
        return tryMakeUri(SCHEME, HOST, "/STV/M/Index.cfm");
    }

    /**
     * Creates the {@link URI} used for retrieving the recordings from the online-archive.
     *
     * @param numberOfEntries the number of entries to receive at most
     * @return an {@link URI}
     */
    static URI videoArchiveUri(int numberOfEntries) {
        return videoArchiveUri(numberOfEntries, NO_SEARCH);
    }

    /**
     * Creates the {@link URI} used for retrieving the recordings from the online-archive.
     * <p>
     * But retrieves only recordings which match the given <code>searchString</code>-
     *
     * @param numberOfEntries the number of entries to receive at most
     * @param searchString    the recordings must match this
     * @return an {@link URI}
     */
    static URI videoArchiveUri(int numberOfEntries, String searchString) {
        LocalDate now = LocalDate.now();

        String startDate = now.minusYears(1).toString();
        String endDate = now.toString();
        String encodedSearchString = URLEncoderUtil.encodeSafely(searchString);

        String query = String.format("iEntriesPerPage=%d&iRecordingState=1&dStartdate=%s&dEnddate=%s&sSearchString=%s",
                numberOfEntries,
                startDate,
                endDate,
                encodedSearchString);

        return tryMakeUri(SCHEME, HOST, "/STV/M/obj/archive/JSON/VideoArchiveApi.cfm", query);
    }

    /**
     * Creates the {@link URI} used for downloading the given {@link Recording} from the online-archive.
     *
     * @param recording the {@link Recording} which should be downloaded
     * @param format    the {@link RecordingFormat} in which the download should happen
     * @return an {@link URI}
     */
    static URI getDownloadUriFor(Recording recording, RecordingFormat format) {
        return tryMakeUri(SCHEME, HOST, "/STV/M/obj/cRecordOrder/croGetDownloadUrl2.cfm", toDownloadQuery(recording, format));
    }

    private static String toDownloadQuery(Recording recording, RecordingFormat format) {
        return String.format("TelecastId=%d&iFormat=%d&bAdFree=%b",
                recording.getTelecastId(),
                format.getQualityCode(),
                format.isCut()
        );
    }

    /**
     * Creates the {@link URI} used for removing a {@link RecordingFormat} from the online-archive.
     *
     * @param telecastId the id of the {@link Recording} which should be deleted
     * @return an {@link URI}
     */
    static URI deleteRecordUri(long telecastId) {
        return tryMakeUri(SCHEME, HOST, "/STV/M/obj/cRecordOrder/croDelete.cfm", "TelecastID=" + telecastId);
    }

    private static URI tryMakeUri(String scheme, String host, String path) {
        return tryMakeUri(scheme, host, path, NOT_SET);
    }

    private static URI tryMakeUri(String scheme, String host, String path, String query) {
        try {
            return new URI(scheme, host, path, query, NOT_SET);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to create URI", e);
        }
    }
}
