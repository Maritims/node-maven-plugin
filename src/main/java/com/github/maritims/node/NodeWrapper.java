package com.github.maritims.node;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
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

public abstract class NodeWrapper {
    private static final Logger log = LoggerFactory.getLogger(NodeWrapper.class);

    private final NodeConfiguration nodeConfiguration;
    protected final String projectSourceCodeDirectory;

    public NodeWrapper(NodeConfiguration nodeConfiguration, String projectSourceCodeDirectory) {
        this.nodeConfiguration = nodeConfiguration;
        this.projectSourceCodeDirectory = projectSourceCodeDirectory;
    }

    private String getVersionString() {
        return "node-v" + nodeConfiguration.getMajorVersion() + "." + nodeConfiguration.getMinorVersion() + "." + nodeConfiguration.getPatchVersion() + "-linux-x64";
    }

    private String getFileName() {
        return getVersionString() + ".tar.gz";
    }

    public Path getDownloadFilePath() {
        return nodeConfiguration.getDownloadDirectory().resolve(getFileName());
    }

    protected Path getNodeExe() {
        return nodeConfiguration.getExtractionDirectory()
                .resolve(Paths.get(getVersionString(), "bin", "node"))
                .toAbsolutePath();
    }

    protected NodePaths getNodePaths() {
        return new NodePaths(nodeConfiguration.getExtractionDirectory().resolve(getVersionString()));
    }

    public boolean download() throws IOException {
        if(Files.exists(getDownloadFilePath())) {
            log.info("The file '" + getDownloadFilePath() + "' already exists. Skipping download.");
            return true;
        }

        if(!Files.exists(nodeConfiguration.getDownloadDirectory())) {
            log.info("The specified download directory '" + nodeConfiguration.getDownloadDirectory() + "' does not exist. Attempting to create it.");
            Files.createDirectories(nodeConfiguration.getDownloadDirectory());
        }

        String url = "https://nodejs.org/dist/v" + nodeConfiguration.getMajorVersion() + "." + nodeConfiguration.getMinorVersion() + "." + nodeConfiguration.getPatchVersion() + "/" + getFileName();
        log.info("Downloading " + url);
        InputStream is = new URL(url).openStream();
        Files.copy(is, getDownloadFilePath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("Successfully downloaded " + url + " to " + getDownloadFilePath());

        return true;
    }

    public boolean extract() {
        TarArchiveInputStream tarArchiveInputStream;
        if(!Files.exists(getDownloadFilePath())) {
            log.error("File " + getDownloadFilePath() + " does not exist");
            return false;
        }

        try {
            InputStream is = Files.newInputStream(getDownloadFilePath());
            GzipCompressorInputStream gz = new GzipCompressorInputStream(is);
            tarArchiveInputStream = new TarArchiveInputStream(gz);
        } catch (IOException e) {
            log.error("Unable to get input stream for archive file", e);
            return false;
        }

        TarArchiveEntry tarEntry;
        try {
            tarEntry = tarArchiveInputStream.getNextTarEntry();
        } catch (IOException e) {
            log.error("Unable to get next entry in archive", e);
            return false;
        }

        String destinationDirectory;
        try {
            destinationDirectory = new File(nodeConfiguration.getExtractionDirectory().toString()).getCanonicalPath();
        } catch (IOException e) {
            log.error("Unable to get canonical path for extraction destination", e);
            return false;
        }

        while(tarEntry != null) {
            Path path = Paths.get(destinationDirectory, tarEntry.getName());
            File file = new File(path.toString());

            if(tarEntry.isDirectory() && !file.exists() && !file.mkdirs()) {
                log.error("Unable to create directory: " + path + ". Aborting.");
                return false;
            }

            if(tarEntry.isFile()) {
                try {
                    if(!file.exists() && !file.createNewFile()) {
                        log.error("Unable to create file: " + path + ". Aborting.");
                        return false;
                    }
                } catch (IOException e) {
                    log.error("Unable to create file from archive entry", e);
                    return false;
                }

                if(!file.setExecutable((tarEntry.getMode() & 0100) > 0)) {
                    log.error("Unable to mark file as executable: " + path + ". Aborting.");
                    return false;
                }

                OutputStream os;
                try {
                    os = Files.newOutputStream(file.toPath());
                } catch (IOException e) {
                    log.error("Unable to create output stream for file " + file.getAbsolutePath());
                    return false;
                }

                try {
                    IOUtils.copy(tarArchiveInputStream, os);
                } catch (IOException e) {
                    log.error("Unable to copy content from archive entry to file input stream", e);
                    return false;
                }

                IOUtils.closeQuietly(os);
            }

            try {
                tarEntry = tarArchiveInputStream.getNextTarEntry();
            } catch (IOException e) {
                log.error("Unable to move to next archive entry", e);
                return false;
            }
        }

        IOUtils.closeQuietly(tarArchiveInputStream);
        return Files.exists(nodeConfiguration.getExtractionDirectory().resolve(getVersionString()));
    }

    /**
     * Executes the specified script.
     * @param script The script to execute.
     * @return A boolean indicating whether the script was executed successfully.
     */
    public abstract boolean run(String script) throws IOException, InterruptedException;
}
