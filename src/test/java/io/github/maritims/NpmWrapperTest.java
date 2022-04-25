package io.github.maritims;

import io.github.maritims.node.NodeConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NpmWrapperTest extends AbstractNodeWrapperTest {
    public static Stream<Arguments> run() {
        return Stream.of(
                Arguments.arguments("Attempt npm install if script is 'install'", "install"),
                Arguments.arguments("Attempt running npm script if script is defined in package.json", "build")
        );
    }

    public static Stream<Arguments> runScript() {
        return Stream.of(
                Arguments.arguments("build", true),
                Arguments.arguments("foo", false)
        );
    }

    @Test
    @DisplayName("Run 'npm install' when script is 'install'")
    public void runInstall() throws IOException, InterruptedException {
        // arrange
        Path node = Paths.get("node");
        NpmWrapper sut = Mockito.spy(new NpmWrapper(
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

    @ParameterizedTest(name = "Execute npm run {0} and expect {1}")
    @MethodSource
    public void runScript(String script, boolean shouldRun) throws IOException, InterruptedException {
        // arrange
        Path node = Paths.get("node");
        NpmWrapper sut = Mockito.spy(new NpmWrapper(
                new NodeConfiguration(node, node, 16, 14, 2),
                Paths.get("src", "main", "node").toAbsolutePath().toString()
        ));

        PackageJson packageJson = mock(PackageJson.class);
        Map<String, String> scripts = new HashMap<>();
        scripts.put("build", null);
        when(packageJson.getScripts()).thenReturn(scripts);

        doReturn(true).when(sut).download();
        doReturn(true).when(sut).extract();
        doReturn(0).when(sut).doSystemCall(any(ProcessBuilder.class));
        doReturn(packageJson).when(sut).getPackageJson();

        // act
        boolean wasRun = sut.run(script);

        // assert
        assertEquals(shouldRun, wasRun);
        verify(sut, times(1)).run(script);
    }
}