package de.web.f_reissmann.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

/**
 * The config which provides the necessary settings.
 * <p>
 * Instances of this class are <code>strictly immutable</code>.
 *
 * @author Fabian Reißmann
 * @since 23.02.2017
 */
public class SaveTvConfig {

    /**
     * Defaults, if they are not provided in the config.
     */
    private static final int NUMBER_OF_ENTRIES_PER_REQUEST_DEFAULT = 500;
    private static final boolean DELETE_ON_SUCCESS_DEFAULT = false;
    private static final String FORMAT_SELECTION_DEFAULT = "CUT_BEST_QUALITY";
    private static final int MIN_AGE_DEFAULT = 3;

    private final Configuration config;

    private SaveTvConfig(Configuration config) {
        this.config = config;
    }

    /**
     * Loads the {@link SaveTvConfig} with the settings from the given properties-file.
     *
     * @param pathToConfig the complete path to the properties-file
     * @return an instance of {@link SaveTvConfig}
     */
    public static SaveTvConfig from(String pathToConfig) {
        try {
            PropertiesBuilderParameters params = new Parameters()
                    .properties()
                    .setFile(new File(pathToConfig));

            Configuration config = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(params)
                    .getConfiguration();

            return new SaveTvConfig(config);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(String.format("Unable to create config for: '%s'", pathToConfig), e);
        }
    }

    public String getUsername() {
        return config.getString(Constants.USERNAME);
    }

    public String getPassword() {
        return config.getString(Constants.PASS);
    }

    public String getDownloadDestination() {
        return config.getString(Constants.DOWNLOAD_DESTINATION);
    }

    public String getFormatSelection() {
        return config.getString(Constants.FORMAT_SELECTION, FORMAT_SELECTION_DEFAULT);
    }

    public boolean shouldDeleteOnSuccess() {
        return config.getBoolean(Constants.DELETE_ON_SUCCESS, DELETE_ON_SUCCESS_DEFAULT);
    }

    public int getEntriesPerRequest() {
        return config.getInt(Constants.ENTRIES_PER_REQUEST, NUMBER_OF_ENTRIES_PER_REQUEST_DEFAULT);
    }

    public int getEntryMinAge() {
        return config.getInt(Constants.MIN_AGE, MIN_AGE_DEFAULT);
    }

    /**
     * Defines the property-keys which can be used in the ".properties"-file.
     */
    private static class Constants {

        /**
         * Mandatory.
         * This properties must be defined in order to make the downloader work correctly.
         */
        private static final String USERNAME = "username";
        private static final String PASS = "password";
        private static final String DOWNLOAD_DESTINATION = "download.destination";

        /**
         * Optional.
         * Defaults will be provided for the following properties, if none is defined in the ".properties"-file
         */
        private static final String FORMAT_SELECTION = "download.format.selection";
        private static final String DELETE_ON_SUCCESS = "download.delete.on.success";
        private static final String ENTRIES_PER_REQUEST = "retrieve.entries.per.request";
        private static final String MIN_AGE = "retrieve.entries.age.min";

        private Constants() {
            throw new UnsupportedOperationException("Utility-Class should not be instantiated.");
        }
    }
}
