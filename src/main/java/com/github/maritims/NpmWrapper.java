package com.github.maritims;

import com.github.maritims.node.NodeConfiguration;
import com.github.maritims.node.NodeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Responsible for running npm commands.
 */
public class NpmWrapper extends NodeWrapper {
    private static final Logger log = LoggerFactory.getLogger(NpmWrapper.class);

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

    protected int doSystemCall(ProcessBuilder pb) {
        try {
            return pb.start().waitFor();
        } catch (InterruptedException | IOException e) {
            log.error("Unable to execute system call", e);
            return -1;
        }
    }

    /**
     * Run the npm install command.
     * @return A boolean indicating whether the install command was executed successfully.
     */
    public boolean install() {
        return doSystemCall(new ProcessBuilder(getNpmCliJs().toAbsolutePath().toString(), "install")
                .directory(new File(projectSourceCodeDirectory))
                .inheritIO()) == 0;
    }

    public boolean runScript(String script) {
        if(!getPackageJson().getScripts().containsKey(script)) {
            log.error(script + " is not a valid script in package.json");
            return false;
        }

        return doSystemCall(new ProcessBuilder(getNpmCliJs().toAbsolutePath().toString(), "run", script)
                .directory(new File(projectSourceCodeDirectory))
                .inheritIO()) == 0;
    }

    @Override
    public boolean run(String script) {
        download();
        extract();

        return "install".equalsIgnoreCase(script) ? install() : runScript(script);
    }
}
