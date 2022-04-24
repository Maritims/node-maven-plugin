package com.github.maritims;

import com.github.maritims.node.NodeConfiguration;
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
    static boolean isDistFolderDeleted = false;

    @BeforeAll
    static void beforeAll() throws IOException {
        Path distFolderPath = Paths.get("src", "main", "node", "dist");
        FileUtils.deleteDirectory(new File(distFolderPath.toString()));

        isDistFolderDeleted = !new File(distFolderPath.toString()).exists();
    }

    @ParameterizedTest
    @CsvSource({ "install", "build" })
    public void run(String command) throws IOException, InterruptedException {
        // assume
        assumeTrue(isDistFolderDeleted, "The dist folder was not deleted");

        // arrange
        Path node = Paths.get("node");
        NpmWrapper sut = new NpmWrapper(
                new NodeConfiguration(node, node, 16, 14, 2),
                Paths.get("src", "main", "node").toAbsolutePath().toString()
        );

        // act
        boolean success = sut.run(command);

        // assert
        assertTrue(success);
    }
}