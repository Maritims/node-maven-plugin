package com.github.maritims;

import com.github.maritims.node.NodeConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NpmWrapperTest extends AbstractNodeWrapperTest {
    public static Stream<Arguments> run() {
        return Stream.of(
                Arguments.arguments("Attempt npm install if script is 'install'", "install"),
                Arguments.arguments("Attempt running npm script if script is defined in package.json", "build")
        );
    }

    @Test
    @DisplayName("Run 'npm install' when script is 'install'")
    public void runInstall() throws IOException, InterruptedException {
        // arrange
        Path node = Paths.get("node");
        NpmWrapper sut = spy(new NpmWrapper(
                new NodeConfiguration(node, node, 16, 14, 2),
                Paths.get("src", "main", "node").toAbsolutePath().toString()
        ));
        doReturn(true).when(sut).download();
        doReturn(true).when(sut).extract();
        doReturn(0).when(sut).doSystemCall(any(ProcessBuilder.class));

        // act
        boolean success = sut.run("install");

        // assert
        assertTrue(success);
        verify(sut, times(1)).install();
    }

    @ParameterizedTest(name = "Run npm run {0} when {0} is described in the scripts section in package.json")
    @CsvSource({ "build" })
    public void runScript(String script) throws IOException, InterruptedException {
        // arrange
        Path node = Paths.get("node");
        NpmWrapper sut = spy(new NpmWrapper(
                new NodeConfiguration(node, node, 16, 14, 2),
                Paths.get("src", "main", "node").toAbsolutePath().toString()
        ));
        doReturn(true).when(sut).download();
        doReturn(true).when(sut).extract();
        doReturn(0).when(sut).doSystemCall(any(ProcessBuilder.class));

        // act
        boolean success = sut.run(script);

        // assert
        assertTrue(success);
        verify(sut, times(1)).run(script);
    }
}