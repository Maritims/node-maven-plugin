package com.github.maritims;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

import static java.lang.ProcessBuilder.Redirect.INHERIT;

@Mojo(name = "run", defaultPhase = LifecyclePhase.COMPILE)
public class YarnRunMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "path", required = true)
    String path;

    @Parameter(property = "command")
    String command;

    @Parameter(property = "install")
    boolean install = false;

    private PackageJson packageJson;
    public PackageJson getPackageJson() throws MojoExecutionException {
        if(packageJson == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                File file = new File(project.getBasedir().getAbsolutePath() + "/src/main/" + this.path + "/package.json");
                packageJson = objectMapper.readValue(file, PackageJson.class);
            } catch (IOException e) {
                throw new MojoExecutionException(e);
            }
        }
        return packageJson;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            if(install) new ProcessBuilder("yarn", "install")
                    .directory(new File("src/main/" + this.path))
                    .redirectOutput(INHERIT)
                    .redirectError(INHERIT)
                    .start()
                    .waitFor();

            if(getPackageJson().getScripts().containsKey(command)) new ProcessBuilder("yarn", command)
                    .directory(new File("src/main/" + this.path))
                    .redirectOutput(INHERIT)
                    .redirectError(INHERIT)
                    .redirectInput(INHERIT)
                    .start()
                    .waitFor();
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException(e);
        }
    }
}
