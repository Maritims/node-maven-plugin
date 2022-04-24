package com.github.maritims;

import com.github.maritims.node.NodeConfiguration;
import com.github.maritims.node.NodeWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Responsible for running npm commands.
 */
public class NpmWrapper extends NodeWrapper {
    public NpmWrapper(NodeConfiguration nodeConfiguration, String projectSourceCodeDirectory) {
        super(nodeConfiguration, projectSourceCodeDirectory);
    }

    /**
     * Path to the npm client JavaScript file.
     * @return Returns the path to the npm client JavaScript file.
     */
    private Path getNpmCliJs() {
        return getNodePaths().getNodeModule("npm").resolve(Paths.get("bin", "npm-cli.js"));
    }

    /**
     * Run the npm install command.
     * @return A boolean indicating whether the install command was executed successfully.
     */
    public boolean install() throws IOException, InterruptedException {
        return new ProcessBuilder(getNpmCliJs().toAbsolutePath().toString(), "install")
                .directory(new File(projectSourceCodeDirectory))
                .inheritIO()
                .start()
                .waitFor() == 0;
    }

    @Override
    public boolean run(String script) throws IOException, InterruptedException {
        download();
        extract();

        if("install".equalsIgnoreCase(script)) return install();
        if(!PackageJson.get(projectSourceCodeDirectory).getScripts().containsKey(script)) throw new IllegalArgumentException(script + " is not a valid script in package.json");

        return new ProcessBuilder(getNpmCliJs().toAbsolutePath().toString(), "run", script)
                .directory(new File(projectSourceCodeDirectory))
                .inheritIO()
                .start()
                .waitFor() == 0;
    }
}
