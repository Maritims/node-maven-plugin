package com.github.maritims;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageJson {
    private final Map<String, String> scripts = new HashMap<>();

    private PackageJson() {}

    public Map<String, String> getScripts() {
        return scripts;
    }
}
