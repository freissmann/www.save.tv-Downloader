package de.web.f_reissmann.recording;


import de.web.f_reissmann.recording.format.RecordingFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pojo. Which represents one saved recording from the online archive.
 * <p>
 * Instances of this class are <code>strictly immutable</code>.
 * They can only be created via the {@link Recording.Builder}.
 *
 * @author Fabian Reißmann
 * @since 19.02.2017
 */
public class Recording {

    private final long telecastId;
    private final String title;
    private final String subTitle;
    private final String episode;
    private final int daysLeft;
    private final Set<RecordingFormat> availableFormats;

    private Recording(long telecastId,
                      String title,
                      String subTitle,
                      String episode,
                      int daysLeft,
                      Set<RecordingFormat> availableFormats) {
        this.telecastId = telecastId;
        this.title = title;
        this.subTitle = subTitle;
        this.episode = episode;
        this.daysLeft = daysLeft;
        this.availableFormats = availableFormats;
    }

    public long getTelecastId() {
        return telecastId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getEpisode() {
        return episode;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public List<RecordingFormat> getAvailableFormats() {
        return new ArrayList<>(availableFormats);
    }

    /**
     * This builder is used to create a {@link Recording} in a fluent way.
     * <p>
     * Does not do any kind of input validation. So if all the necessary parameters are not filled, the built
     * {@link Recording} might not work correctly.
     */
    public static class Builder {

        private long telecastId;
        private String title;
        private String subTitle;
        private String episode;
        private int daysLeft;
        private Set<RecordingFormat> availableFormats;

        /**
         * Adds the unique id to the {@link Recording} to be built.
         *
         * @param telecastId the unique id
         * @return <code>this</code>, for fluent style
         */
        public Builder withTelecastId(long telecastId) {
            this.telecastId = telecastId;

            return this;
        }

        /**
         * Adds the title to the {@link Recording} to be built.
         *
         * @param title the title of the {@link Recording}
         * @return <code>this</code>, for fluent style
         */
        public Builder withTitle(String title) {
            this.title = title;

            return this;
        }

        /**
         * Adds the sub-title to the {@link Recording} to be built.
         *
         * @param subTitle the sub-title of the {@link Recording}
         * @return <code>this</code>, for fluent style
         */
        public Builder withSubTitle(String subTitle) {
            this.subTitle = subTitle;

            return this;
        }

        /**
         * Adds the episode to the {@link Recording} to be built.
         *
         * @param episode the episode of the {@link Recording}
         * @return <code>this</code>, for fluent style
         */
        public Builder withEpisode(String episode) {
            this.episode = episode;

            return this;
        }

        /**
         * Adds the number of days left, before the {@link Recording} will be removed from the online archive,
         * to the {@link Recording} to be built.
         *
         * @param daysLeft the number of days before the recording will be deleted
         * @return <code>this</code>, for fluent style
         */
        public Builder withDaysLeft(int daysLeft) {
            this.daysLeft = daysLeft;

            return this;
        }

        /**
         * Adds the available format to the {@link Recording} to be build.
         *
         * @param availableFormats the {@link RecordingFormat}s in which the recording can be downloaded
         * @return <code>this</code>, for fluent style
         */
        public Builder withAvailableFormats(Collection<RecordingFormat> availableFormats) {
            this.availableFormats = new HashSet<>(availableFormats);

            return this;
        }

        /**
         * Builds the {@link Recording} with the defined values.
         *
         * @return the built {@link Recording}
         */
        public Recording build() {
            return new Recording(telecastId, title, subTitle, episode, daysLeft, availableFormats);
        }

    }
}
