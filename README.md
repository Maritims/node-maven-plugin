# node-maven-plugin
Maven plugin for running node commands.

## Configuration
Add the the plugin groupId to the `<pluginGroups />` section of settings.xml:
```xml
<pluginGroups>
    <pluginGroup>com.github.maritims</pluginGroup>
</pluginGroups>
```

Add the plugin configuration to the pom.xml in your Maven project:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.maritims</groupId>
            <artifactId>node-maven-plugin</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <configuration>
                <install>true</install>
                <script>build</script>
                <sourceCodePath>svelte</sourceCodePath>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>npm</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Options
| Option                  | Type    | Value                    | Default value | Explanation                                                                                                                 |
|-------------------------|---------|--------------------------|---------------|-----------------------------------------------------------------------------------------------------------------------------|
| nodePath                | string  | node                     | node          | Path to directory within project base directory where node should be downloaded and installed.                              |
| major                   | int     | 16                       | 16            | Major version of node.                                                                                                      |
| minor                   | int     | 14                       | 14            | Minor version of node.                                                                                                      |
| patch                   | int     | 2                        | 2             | Patch version of node.                                                                                                      |
| sourceCodeDirectoryName | string  | svelte, node, vue, react | N/A           | Path to directory within the src directory containing package.json. The directory should live alongside the java directory. | 
| install                 | boolean | true, false              | N/A           | Indicates whether packages should be installed.                                                                             |
| script                  | string  | build                    | N/A           | Indicates which script to run. The script must be present in package.json.                                                  |

## Install npm packages
`mvn node:npm -DsourceCodeDirectoryName=<sourceCodeDirectoryName> -Dinstall`

## Run npm script
`mvn node:npm -DsourceCodeDirectoryName=<sourceCodeDirectoryName> -Dscript <script>`

## Define output directory for npm build
How you define the output directory for your yarn build depends on the bundling software you decide to use. You typically want the yarn build to put the file(s) somewhere in src/main/webapp, or a sub-directory.