package io.github.maritims;

import io.github.maritims.node.NodeConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mojo(name = "npm", defaultPhase = LifecyclePhase.INSTALL)
public class NpmMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "nodePath", required = true, defaultValue = "node")
    String nodePath;

    @Parameter(property = "major", required = true, defaultValue = "16")
    int major;

    @Parameter(property = "minor", required = true, defaultValue = "14")
    int minor;

    @Parameter(property = "patch", required = true, defaultValue = "2")
    int patch;

    @Parameter(property = "sourceCodeDirectoryName", required = true)
    String sourceCodeDirectoryName;

    @Parameter(property = "install", required = true, defaultValue = "false")
    boolean install;

    @Parameter(property = "script", required = true)
    String script;

    @Parameter(property = "environmentVariables")
    String environmentVariables;

    public void execute() throws MojoExecutionException, MojoFailureException {
        NpmWrapper npm = new NpmWrapper(
                new NodeConfiguration(Paths.get(nodePath), Paths.get(nodePath), major, minor, patch),
                Paths.get(project.getBasedir().getAbsolutePath(), "src", "main", sourceCodeDirectoryName).toString()
        );

        if(install) npm.install();
        npm.run(script, environmentVariables);
    }
}
