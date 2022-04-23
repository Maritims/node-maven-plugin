package com.github.maritims;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeInstallerTest {
    public static Stream<Arguments> download() {
        return Stream.of(
                Arguments.arguments("", "node", 16, 14, 2)
        );
    }

    public static Stream<Arguments> extract() {
        return Stream.of(
                Arguments.arguments("", "node", 16, 14, 2)
        );
    }

    @ParameterizedTest
    @MethodSource
    @Order(1)
    public void download(String downloadDirectory, String extractionDirectory, int major, int minor, int patch) throws IOException {
        // arrange
        NodeInstaller sut = new NodeInstaller(downloadDirectory, extractionDirectory, major, minor, patch);

        // act
        boolean success = sut.download();

        // assert
        assertTrue(success);
        assertTrue(new File(String.valueOf(sut.getDownloadTargetPath())).exists());
    }

    @ParameterizedTest
    @MethodSource
    @Order(2)
    public void extract(String downloadDirectory, String extractionDirectory, int major, int minor, int patch) throws IOException {
        // arrange
        NodeInstaller sut = new NodeInstaller(downloadDirectory, extractionDirectory, major, minor, patch);

        // act
        String installationDirectory = sut.extract();

        // assert
        assertTrue(StringUtils.isNotBlank(installationDirectory));
    }
}