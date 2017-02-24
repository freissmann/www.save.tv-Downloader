# Downloader for the platform www.save.tv
The downloader is written in Java using other open-source libraries.

# How to build?
For building an executable "fat-jar" the gradle shadow plugin is used.

- Open den project in an IDE and run the task "shadowJar"
or
- run "gradlew shadowJar" in the root directory

This will create the "fat-jar" under ./build/libs/*-all.jar

# How to run?
Either run the "SaveTvDownloaderApp#main" from an IDE or execute the jar from the command-line:
"java -jar save-tv-downloader-1.0-SNAPSHOT-all.jar <PATH_TO_A_PROPERTIES_FILE>"

In either way a ".properties"-file is required to provide the settings needed by the downloaded.
A template ".properties"-file is located in the project's root directory.
