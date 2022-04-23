package com.github.maritims;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Responsible for running npm commands.
 */
public class NpmWrapper extends NodeWrapper {
    public NpmWrapper(String nodeDirectory, String sourceCodeDirectory) {
        super(nodeDirectory, sourceCodeDirectory);
    }

    /**
     * Path to the npm client JavaScript file.
     * @return Returns the path to the npm client JavaScript file.
     */
    private String getNpmCliJs() {
        return Paths.get(nodeDirectory, "lib", "node_modules", "npm", "bin", "npm-cli.js").toAbsolutePath().toString();
    }

    /**
     * Run the npm install command.
     * @return A boolean indicating whether the install command was executed successfully.
     */
    public boolean install() throws IOException, InterruptedException {
        return new ProcessBuilder(getNodeExe(), getNpmCliJs(), "install")
                .directory(new File(sourceCodeDirectory))
                .inheritIO()
                .start()
                .waitFor() == 0;
    }

    @Override
    public boolean run(String script) throws IOException, InterruptedException {
        if("install".equalsIgnoreCase(script)) return install();
        if(!PackageJson.get(sourceCodeDirectory).getScripts().containsKey(script)) throw new IllegalArgumentException(script + " is not a valid script in package.json");

        return new ProcessBuilder(getNodeExe(), getNpmCliJs(), "run", script)
                .directory(new File(sourceCodeDirectory))
                .inheritIO()
                .start()
                .waitFor() == 0;
    }
}
