package de.web.f_reissmann.connection;

import de.web.f_reissmann.config.SaveTvConfig;
import de.web.f_reissmann.file.FilenameUtil;
import de.web.f_reissmann.parser.SaveTvResponseParser;
import de.web.f_reissmann.recording.Recording;
import de.web.f_reissmann.recording.RecordingUtil;
import de.web.f_reissmann.recording.format.RecordingFormat;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * The access point for interacting with save.tv.
 * It provides methods for retrieval, download und deletion.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public class SaveTvConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveTvConnection.class);

    /**
     * The SaveTv config.
     */
    private final SaveTvConfig config;

    /**
     * The {@link HttpClient} used for making any GET- or POST-calls.
     */
    private final HttpClient client;

    /**
     * Used for parsing the raw string responses into POJOs etc.
     */
    private final SaveTvResponseParser responseParser = new SaveTvResponseParser();

    /**
     * Ctor.
     *
     * @param config the SaveTv config
     * @param client the {@link HttpClient} which is used to make all requests
     */
    private SaveTvConnection(SaveTvConfig config, HttpClient client) {
        this.config = config;
        this.client = client;
    }

    /**
     * Factory-Method for creating an already logged-in connection to SaveTv.
     *
     * @param config         the config including credentials and other settings used for logging in
     * @param clientSupplier a supplier for supplying a {@link HttpClient} which is used to make all requests
     * @return a logged in connection to SaveTv
     * @throws UnableToLoginException if an exception occurred while trying to connect
     */
    public static SaveTvConnection login(SaveTvConfig config, Supplier<HttpClient> clientSupplier) {
        SaveTvConnection saveTvConnection = new SaveTvConnection(config, clientSupplier.get());

        try {
            saveTvConnection.tryLogin();
        } catch (IOException e) {
            throw new UnableToLoginException("Unable to login", e);
        }

        return saveTvConnection;
    }

    /**
     * Retrieves at most the {@link SaveTvConfig#getEntriesPerRequest()} newest recordings.
     * <p>
     * Sort-order:
     * Recordings which will be removed soon are located at the start of the list. To not loose recordings from save.tv
     * due to automatic deletion.
     *
     * @return a sorted list of {@link Recording}s
     */
    public List<Recording> retrieveRecordings() {
        URI requestUri = SaveTvUriUtils.videoArchiveUri(config.getEntriesPerRequest(), config.getEntryMinAge());

        return responseParser.extractRecordings(executeGetOnUri(requestUri));
    }

    /**
     * Downloads the given recording to the folder defined in the {@link SaveTvConfig}.
     * <p>
     * Format-Selection:
     * The preferred format for the downloaded recording is also defined in the {@link SaveTvConfig}.
     *
     * @param recording the {@link Recording} to download
     */
    public void download(Recording recording) {
        RecordingFormat format = RecordingFormat.getPreferredFormat(recording, config.getFormatSelection());

        URI requestForRecordingDownloadUri = SaveTvUriUtils.getDownloadUriFor(recording, format);

        String downloadDestination = config.getDownloadDestination();
        String fileName = FilenameUtil.sanitize(RecordingUtil.toFilename(recording, format));

        File downloadedFile = new File(downloadDestination, fileName + ".mp4");
        try {
            FileUtils.forceMkdirParent(downloadedFile);

            LOGGER.info("Start downloading: '{}' to '{}'", fileName, downloadDestination);
            String downloadUrl = responseParser.extractDownloadUri(executeGetOnUri(requestForRecordingDownloadUri));

            FileUtils.copyURLToFile(new URL(downloadUrl), downloadedFile);
            LOGGER.info("Finished download.");
        } catch (IOException e) {
            throw new IllegalStateException("", e);
        }
    }

    /**
     * Deletes the given recording from the online-archive.
     * <p>
     * Attention:
     * There is no possibility to access a deleted recording.
     *
     * @param recording the {@link Recording} to delete
     */
    public void deleteRecording(Recording recording) {
        URI requestUri = SaveTvUriUtils.deleteRecordUri(recording.getTelecastId());

        executeGetOnUri(requestUri);
        LOGGER.info("Deleted from Online-Archive: '{}' (URL='{}')", recording.getTitle(), requestUri);
    }

    private String executeGetOnUri(URI uri) {
        try {
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse res = client.execute(httpGet);
            HttpEntity entity = res.getEntity();

            if (entity == null) {
                throw new IOException("The response did not include an entity");
            }

            return convertEntityToString(entity);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to execute get on uri: " + uri, e);
        }
    }

    private String convertEntityToString(HttpEntity entity) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        }
        return sb.toString();
    }

    private void tryLogin() throws IOException {
        String username = config.getUsername();
        String password = config.getPassword();

        HttpPost httpPost = new HttpPost(SaveTvUriUtils.loginUri());
        httpPost.setEntity(SaveTvCredentialsFormatter.toFormEntity(username, password));

        HttpResponse response = client.execute(httpPost);

        checkLoginResponse(response);
    }

    private void checkLoginResponse(HttpResponse response) throws IOException {
        if (!responseParser.hasLoggedInCorrectly(response)) {
            throw new IOException("Response is not correct. Headers: " + Arrays.toString(response.getAllHeaders()));
        }
    }

    private static class UnableToLoginException extends RuntimeException {
        private UnableToLoginException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
