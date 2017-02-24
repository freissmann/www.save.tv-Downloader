package de.web.f_reissmann.parser;

import com.google.common.collect.ComparisonChain;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.web.f_reissmann.recording.Recording;
import de.web.f_reissmann.recording.format.RecordingFormat;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This parser is used to extract the useful information from the responses received from save.tv.
 * <p>
 * For instance:
 * - Checks if a login was successful
 * - Uses the extracted information to create {@link Recording}s
 * - Extract the download-url for a specific {@link Recording}
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public class SaveTvResponseParser {

    private static final boolean NOT_IN_PARALLEL = false;

    /**
     * Checks if the response tells, that the login was successful
     *
     * @param response the {@link HttpResponse} to check
     * @return true, if the login was successfull; false, otherwise
     */
    public boolean hasLoggedInCorrectly(HttpResponse response) {
        Header header = response.getFirstHeader("location");

        return header != null
                && !containsErrorCode49(header.getValue());
    }

    /**
     * Extracts the download-url from the response.
     *
     * @param responseForDownloadUri the {@link HttpResponse} to parse
     * @return a String representing an URL
     */
    public String extractDownloadUri(String responseForDownloadUri) {
        JsonObject jsonObject = toJsonObject(responseForDownloadUri);

        return jsonObject.get("DOWNLOADURL").getAsString();
    }

    /**
     * Extracts a list of {@link Recording}s from the response.
     *
     * @param response the {@link HttpResponse} to parse
     * @return list of {@link Recording}s
     */
    public List<Recording> extractRecordings(String response) {
        return toRecordings(extractArchiveEntries(response));
    }

    private boolean containsErrorCode49(String locationHeader) {
        return locationHeader.toLowerCase()
                .contains("errorcodeid_49");
    }

    private List<JsonObject> extractArchiveEntries(String response) {
        JsonObject recordings = toJsonObject(response);

        JsonArray archiveEntries = recordings.get("ARRVIDEOARCHIVEENTRIES")
                .getAsJsonArray();

        return StreamSupport.stream(archiveEntries.spliterator(), NOT_IN_PARALLEL)
                .map(this::toJsonObject)
                .map(fullEntry -> fullEntry.get("STRTELECASTENTRY"))
                .map(this::toJsonObject)
                .collect(Collectors.toList());
    }

    private List<Recording> toRecordings(List<JsonObject> archiveEntries) {

        return archiveEntries.stream()
                .map(this::toRecording)
                .sorted(sortRecordings())
                .collect(Collectors.toList());
    }

    private Comparator<? super Recording> sortRecordings() {

        return (recording1, recording2) -> ComparisonChain.start()
                .compare(recording1.getDaysLeft(), recording2.getDaysLeft())
                .result();
    }

    private Recording toRecording(JsonObject json) {
        String telecastIdString = json.get("ITELECASTID").getAsString();

        long telecastId = new BigDecimal(telecastIdString).longValue();
        String title = json.get("STITLE").getAsString();
        String subTitle = json.get("SSUBTITLE").getAsString();
        String episode = json.get("SFOLGE").getAsString();
        int daysLeft = json.get("IDAYSLEFTBEFOREDELETE").getAsInt();

        Set<RecordingFormat> availableFormats = extractAvailableFormats(json);

        return new Recording.Builder()
                .withTelecastId(telecastId)
                .withTitle(title)
                .withSubTitle(subTitle)
                .withEpisode(episode)
                .withDaysLeft(daysLeft)
                .withAvailableFormats(availableFormats)
                .build();
    }

    private Set<RecordingFormat> extractAvailableFormats(JsonObject json) {
        JsonArray formats = json.get("ARRALLOWDDOWNLOADFORMATS")
                .getAsJsonArray();

        return StreamSupport.stream(formats.spliterator(), NOT_IN_PARALLEL)
                .map(this::toJsonObject)
                .map(this::toRecordingFormat)
                .collect(Collectors.toSet());
    }

    private RecordingFormat toRecordingFormat(JsonObject json) {
        boolean isCut = json.get("BADCUTENABLED").getAsBoolean();
        int recordFormatId = json.get("RECORDINGFORMATID").getAsInt();
        RecordingFormat.Quality quality = RecordingFormat.Quality.fromCode(recordFormatId);

        return new RecordingFormat(isCut, quality);
    }

    private JsonObject toJsonObject(String json) {
        JsonElement parsed = new JsonParser().parse(json);

        return toJsonObject(parsed);
    }

    private JsonObject toJsonObject(JsonElement element) {
        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        }

        throw new IllegalStateException("The given JsonElement is not a valid JSON-Object: " + element);
    }

}
