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


# License
MIT License

Copyright (c) 2017 Fabian Reißmann

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
