package io.github.maritims;

import io.github.maritims.node.NodeConfiguration;
import io.github.maritims.node.NodeWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        download();
        extract(false);

        return doSystemCall(new ProcessBuilder(getNpmCliJs().toAbsolutePath().toString(), "install")
                .directory(new File(projectSourceCodeDirectory))
                .inheritIO()) == 0;
    }

    protected boolean runScript(String script, Map<String, String> environmentVariables) {
        if(!getPackageJson().getScripts().containsKey(script)) {
            log.error(script + " is not a valid script in package.json");
            return false;
        }

        ProcessBuilder pb = new ProcessBuilder(getNpmCliJs().toAbsolutePath().toString(), "run", script);
        pb.environment().put("PATH", pb.environment().get("PATH") + ":" + getNodePaths().getBin().toAbsolutePath());
        for(Map.Entry<String, String> environmentVariable : environmentVariables.entrySet()) {
            pb.environment().put(environmentVariable.getKey(), environmentVariable.getValue());
        }
        return doSystemCall(pb
                .directory(new File(projectSourceCodeDirectory))
                .inheritIO()) == 0;
    }

    protected Map<String, String> getEnvironmentVariables(String environmentVariables) {
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isBlank(environmentVariables)) {
            return map;
        }

        Pattern pattern = Pattern.compile("([A-Z]+_[A-Z0-9]+)=([a-zA-Z0-9,-]+)\\s?");
        Matcher matcher = pattern.matcher(environmentVariables);
        while(matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }

        return map;
    }

    @Override
    public boolean run(String script, String environmentVariables) {
        download();
        extract(false);

        return "install".equalsIgnoreCase(script) ? install() : runScript(script, getEnvironmentVariables(environmentVariables));
    }
}
