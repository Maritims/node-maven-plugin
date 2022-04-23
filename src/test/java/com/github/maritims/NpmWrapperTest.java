package com.github.maritims;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class NpmWrapperTest {
    static boolean isNodeInstalled = false;
    static boolean isDistFolderDeleted = false;

    @BeforeAll
    static void beforeAll() throws IOException {
        NodeInstaller nodeInstaller = new NodeInstaller("node", "node", 16, 14, 2);

        boolean isDownloaded = nodeInstaller.download();
        boolean isExtracted = nodeInstaller.extract();

        Path distFolderPath = Paths.get("src", "main", "node", "dist");
        FileUtils.deleteDirectory(new File(distFolderPath.toString()));

        isDistFolderDeleted = !new File(distFolderPath.toString()).exists();
        isNodeInstalled = isDownloaded && isExtracted;
    }

    @ParameterizedTest
    @CsvSource({ "install", "build" })
    public void run(String command) throws IOException, InterruptedException {
        // assume
        assumeTrue(isNodeInstalled, "Node is not installed");
        assumeTrue(isDistFolderDeleted, "The dist folder was not deleted");

        // arrange
        NpmWrapper sut = new NpmWrapper(
                Paths.get("node", "node-v16.14.2-linux-x64").toAbsolutePath().toString(),
                Paths.get("src", "main", "node").toAbsolutePath().toString()
        );

        // act
        boolean success = sut.run(command);
        boolean distFolderExists = new File(Paths.get("src", "main", "node", "dist").toString()).exists();

        // assert
        assertTrue(success);
        assertTrue(distFolderExists);
    }
}