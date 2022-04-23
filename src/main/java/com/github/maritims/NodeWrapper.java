package com.github.maritims;

import java.io.IOException;
import java.nio.file.Paths;

abstract public class NodeWrapper {
    /**
     * Path to the directory where the node-based source code is located.
     */
    protected final String sourceCodeDirectory;
    /**
     * Path to the directory where node is installed.
     */
    protected final String nodeDirectory;

    protected NodeWrapper(String nodeDirectory, String sourceCodeDirectory) {
        this.nodeDirectory = nodeDirectory;
        this.sourceCodeDirectory = sourceCodeDirectory;
    }

    /**
     * Absolute path to node executable.
     * @return Returns the absolute path to the node executable on the filesystem.
     */
    protected String getNodeExe() {
        return Paths.get(nodeDirectory, "bin", "node").toAbsolutePath().toString();
    }

    /**
     * Executes the specified script.
     * @param script The script to execute.
     * @return A boolean indicating whether the script was executed successfully.
     */
    public abstract boolean run(String script) throws IOException, InterruptedException;
}
