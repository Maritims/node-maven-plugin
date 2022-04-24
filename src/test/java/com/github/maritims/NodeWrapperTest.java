package com.github.maritims;

import com.github.maritims.node.NodeConfiguration;
import com.github.maritims.node.NodeWrapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeWrapperTest {
    public static Stream<Arguments> download() {
        Path path = Paths.get("node");
        return Stream.of(
                Arguments.arguments(new NodeConfiguration(path, path, 16, 14, 2))
        );
    }

    public static Stream<Arguments> extract() {
        Path path = Paths.get("node");
        return Stream.of(
                Arguments.arguments(new NodeConfiguration(path, path, 16, 14, 2))
        );
    }

    @ParameterizedTest
    @MethodSource
    @Order(1)
    public void download(NodeConfiguration nodeConfiguration) throws IOException {
        // arrange
        NodeWrapper sut = new NodeWrapper(nodeConfiguration, null) {
            @Override
            public boolean run(String script) {
                return false;
            }
        };

        // act
        boolean success = sut.download();

        // assert
        assertTrue(success);
        assertTrue(new File(String.valueOf(sut.getDownloadFilePath())).exists());
    }

    @ParameterizedTest
    @MethodSource
    @Order(2)
    public void extract(NodeConfiguration nodeConfiguration) {
        // arrange
        NodeWrapper sut = new NodeWrapper(nodeConfiguration, null) {
            @Override
            public boolean run(String script) {
                return false;
            }
        };

        // act
        boolean success = sut.extract();

        // assert
        assertTrue(success);
    }
}