package io.github.maritims;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageJson {
    private final Map<String, String> scripts = new HashMap<>();

    private PackageJson() {}

    public static PackageJson get(String sourceCodeDirectory) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(sourceCodeDirectory + "/package.json");
        return objectMapper.readValue(file, PackageJson.class);
    }

    public Map<String, String> getScripts() {
        return scripts;
    }
}
