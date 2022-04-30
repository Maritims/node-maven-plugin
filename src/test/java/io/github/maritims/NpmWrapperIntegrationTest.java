package io.github.maritims;

import io.github.maritims.node.NodeConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpmWrapperIntegrationTest {
    @BeforeAll
    static void beforeAll() {
        try {
            FileUtils.deleteDirectory(Paths.get("src", "test", "resources", "node", "dist").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> runScript() {
        return Stream.of(
                Arguments.arguments("build", true)
        );
    }

    @ParameterizedTest(name = "Execute npm run {0} and expect {1}")
    @MethodSource
    public void runScript(String script, boolean shouldRun) {
        // arrange
        Path node = Paths.get("node");
        NpmWrapper sut = new NpmWrapper(
                new NodeConfiguration(node, node, 16, 14, 2),
                Paths.get("src", "test", "resources", "node").toAbsolutePath().toString()
        );

        // act
        boolean wasRun = sut.run(script);

        // assert
        assertEquals(shouldRun, wasRun);
    }
}
