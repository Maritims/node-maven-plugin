package com.github.maritims.node;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NodePaths {
    private final Path nodeDirectory;

    public NodePaths(Path nodeDirectory) {
        this.nodeDirectory = nodeDirectory;
    }

    public Path getBin() {
        return nodeDirectory.resolve("bin");
    }

    public Path getNodeModule(String name) {
        return nodeDirectory.resolve(Paths.get("lib", "node_modules", name));
    }
}
