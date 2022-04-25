package io.github.maritims;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

abstract public class AbstractNodeWrapperTest {
    @BeforeEach
    @AfterEach
    void beforeAndAfter() throws IOException {
        FileUtils.deleteDirectory(new File(Paths.get("node").toString()));
        FileUtils.deleteDirectory(new File(Paths.get("src", "main", "node", "dist").toString()));
    }
}
