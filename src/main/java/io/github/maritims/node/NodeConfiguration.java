package io.github.maritims.node;

import java.nio.file.Path;

public class NodeConfiguration {
    private final Path downloadDirectory;
    private final Path extractionDirectory;
    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;

    public NodeConfiguration(Path downloadDirectory, Path extractionDirectory, int majorVersion, int minorVersion, int patchVersion) {
        this.downloadDirectory = downloadDirectory;
        this.extractionDirectory = extractionDirectory;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
    }

    public Path getDownloadDirectory() {
        return downloadDirectory;
    }

    public Path getExtractionDirectory() {
        return extractionDirectory;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getPatchVersion() {
        return patchVersion;
    }
}
