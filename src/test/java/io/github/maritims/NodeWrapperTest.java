package io.github.maritims;

import io.github.maritims.node.NodeConfiguration;
import io.github.maritims.node.NodeWrapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeWrapperTest extends AbstractNodeWrapperTest {
    public static Stream<Arguments> download_and_extract() {
        Path path = Paths.get("node");
        return Stream.of(
                Arguments.arguments(new NodeConfiguration(path, path, 16, 14, 2))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void download_and_extract(NodeConfiguration nodeConfiguration) throws IOException {
        // arrange
        NodeWrapper sut = new NodeWrapper(nodeConfiguration, null) {
            @Override
            public boolean run(String script, String environmentVariables) {
                return false;
            }
        };

        // act
        boolean isDownloaded = sut.download();
        boolean isExtracted = sut.extract(false);

        // assert
        assertTrue(isDownloaded);
        assert(isExtracted);
        assertTrue(new File(String.valueOf(sut.getDownloadFilePath())).exists());
    }
}