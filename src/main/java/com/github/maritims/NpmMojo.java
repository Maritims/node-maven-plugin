package com.github.maritims;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Paths;

@Mojo(name = "npm", defaultPhase = LifecyclePhase.INSTALL)
public class NpmMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "nodePath", required = true)
    String nodePath;

    @Parameter(property = "major", required = true)
    int major;

    @Parameter(property = "minor", required = true)
    int minor;

    @Parameter(property = "patch", required = true)
    int patch;

    @Parameter(property = "sourceCodePath", required = true)
    String sourceCodePath;

    @Parameter(property = "install", required = true)
    boolean install;

    @Parameter(property = "script", required = true)
    String script;

    public void execute() throws MojoExecutionException, MojoFailureException {
        NodeInstaller nodeInstaller = new NodeInstaller(nodePath, nodePath, major, minor, patch);
        String nodeInstallationDirectory;
        try {
            nodeInstaller.download();
            nodeInstallationDirectory = nodeInstaller.extract();
        } catch (IOException e) {
            throw new MojoExecutionException(e);
        }

        NpmWrapper npm = new NpmWrapper(
                nodeInstallationDirectory,
                Paths.get(project.getBasedir().getAbsolutePath(), "src", "main", sourceCodePath).toString()
        );
        try {
            if(install) npm.install();
            npm.run(script);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
