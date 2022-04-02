package com.github.maritims;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "install", defaultPhase = LifecyclePhase.COMPILE)
public class YarnInstallMojo extends AbstractMojo {
    @Parameter(property = "path", required = true)
    String path;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            new ProcessBuilder("yarn", "install")
                    .directory(new File("src/main/" + this.path)).redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectInput(ProcessBuilder.Redirect.INHERIT)
                    .start()
                    .waitFor();
        } catch (InterruptedException | IOException e) {
            throw new MojoExecutionException(e);
        }
    }
}
