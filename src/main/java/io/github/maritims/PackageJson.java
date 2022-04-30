package io.github.maritims;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageJson {
    private static final Logger log = LoggerFactory.getLogger(PackageJson.class);

    private final Map<String, String> scripts = new HashMap<>();

    private PackageJson() {}

    public static PackageJson get(String sourceCodeDirectory) {
        ObjectMapper objectMapper = new ObjectMapper();
        String packageJsonPath = sourceCodeDirectory + "/package.json";
        File file = new File(packageJsonPath);

        if(!file.exists()) {
            log.error(packageJsonPath + " does not exist.");
            return new PackageJson();
        }

        try {
            return objectMapper.readValue(file, PackageJson.class);
        } catch (IOException e) {
            log.error("Unable to deserialize contents of " + packageJsonPath, e);
            return new PackageJson();
        }
    }

    public Map<String, String> getScripts() {
        return scripts;
    }
}
