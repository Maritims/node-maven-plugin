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

@Mojo(name = "node", defaultPhase = LifecyclePhase.INSTALL)
public class NpmMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "nodePath", required = true)
    String nodePath;

    @Parameter(property = "path", required = true)
    String path;

    @Parameter(property = "command")
    String command;

    @Parameter(property = "install")
    boolean install = false;

    public void execute() throws MojoExecutionException, MojoFailureException {
        NpmWrapper npm = new NpmWrapper(Paths.get(project.getBasedir().getAbsolutePath(), "src", "main", path, "package.json").toString(), Paths.get(nodePath, "bin", "npm").toString());
        try {
            if(install) npm.install();
            else npm.run(command);
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException(e);
        }
    }
}
