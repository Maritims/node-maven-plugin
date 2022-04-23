package com.github.maritims;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Responsible for installing Node.
 */
public class NodeInstaller {
    private static final Logger log = LoggerFactory.getLogger(NodeInstaller.class);

    private final String downloadDirectory;
    private final String extractionDirectory;
    private final int major;
    private final int minor;
    private final int patch;

    private String getFileName() {
        return "node-v" + major + "." + minor + "." + patch + "-linux-x64.tar.gz";
    }

    public Path getDownloadTargetPath() {
        return Paths.get(downloadDirectory, getFileName());
    }

    public NodeInstaller(String downloadDirectory, String extractionDirectory, int major, int minor, int patch) {
        this.downloadDirectory = downloadDirectory;
        this.extractionDirectory = extractionDirectory;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /***
     * Downloads the specified version of Node and stores it in {@link this#downloadDirectory}.
     * @return A boolean value indicating whether the download succeeded.
     * @throws IOException Thrown in case of an invalid URL is encountered or something goes wrong during the download process.
     */
    public boolean download() throws IOException {
        if(StringUtils.isNotBlank(downloadDirectory)) {
            log.info(downloadDirectory + " does not exist. Attempting to create it.");
            Files.createDirectories(Paths.get(downloadDirectory));
        }

        if(Files.exists(getDownloadTargetPath())) {
            log.info(getDownloadTargetPath() + " already exists. Skipping download.");
            return true;
        }

        String url = "https://nodejs.org/dist/v" + major + "." + minor + "." + patch + "/" + getFileName();
        log.info("Downloading " + url);
        InputStream is = new URL(url).openStream();
        Files.copy(is, getDownloadTargetPath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("Successfully downloaded " + url + " to " + getDownloadTargetPath());

        return true;
    }

    /***
     * Extracts the specified version of Node in {@link this#extractionDirectory}.
     * @return The installation directory if the operation is successful, null if not.
     * @throws IOException Thrown in case something goes wrong during the extraction process.
     */
    public String extract() throws IOException {
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(Files.newInputStream(Paths.get(getDownloadTargetPath().toString()))));
        TarArchiveEntry tarEntry = tarArchiveInputStream.getNextTarEntry();
        String destinationDirectory = new File(extractionDirectory).getCanonicalPath();

        while(tarEntry != null) {
            Path path = Paths.get(destinationDirectory, tarEntry.getName());
            File file = new File(path.toString());

            if(tarEntry.isDirectory()) {
                if(!file.exists() && !file.mkdirs()) {
                    log.error("Unable to create directory: " + path + ". Aborting.");
                    return null;
                }
            } else if(tarEntry.isFile()) {
                if(!file.exists() && !file.createNewFile()) {
                    log.error("Unable to create file: " + path + ". Aborting.");
                    return null;
                }

                if(!file.setExecutable((tarEntry.getMode() & 0100) > 0)) {
                    log.error("Unable to mark file as executable: " + path + ". Aborting.");
                    return null;
                }

                OutputStream os = Files.newOutputStream(file.toPath());
                IOUtils.copy(tarArchiveInputStream, os);
                IOUtils.closeQuietly(os);
            }

            tarEntry = tarArchiveInputStream.getNextTarEntry();
        }

        IOUtils.closeQuietly(tarArchiveInputStream);
        return Paths.get(extractionDirectory, "node-v" + major + "." + minor + "." + patch + "-linux-x64").toString();
    }
}
