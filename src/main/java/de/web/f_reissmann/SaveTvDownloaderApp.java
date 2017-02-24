package de.web.f_reissmann;

import de.web.f_reissmann.config.SaveTvConfig;
import de.web.f_reissmann.connection.SaveTvConnection;
import de.web.f_reissmann.recording.Recording;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The Application to start the downloader.
 * <p>
 * Needs a valid ".properties"-file as first argument.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public final class SaveTvDownloaderApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveTvDownloaderApp.class);

    private SaveTvDownloaderApp() {
        throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
    }

    /**
     * Starts the application.
     *
     * @param args the ".properties"-file to get the configuration parameters from
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Wrong arguments:s <path_to_config>");
        }

        String configPath = args[0];
        SaveTvConfig saveTvConfig = SaveTvConfig.from(configPath);

        LOGGER.info("Started Downloader using config: '{}' ", configPath);

        SaveTvConnection saveTvConnection = SaveTvConnection.login(saveTvConfig, HttpClients::createDefault);

        List<Recording> recordings = saveTvConnection.retrieveRecordings();

        for (Recording recording : recordings) {
            saveTvConnection.download(recording);

            if (saveTvConfig.shouldDeleteOnSuccess()) {
                saveTvConnection.deleteRecording(recording);
            }
            LOGGER.info("Finished recording\n");
        }

        LOGGER.info("Downloader finished");
    }

}
