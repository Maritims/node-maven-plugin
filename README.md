# yarn-maven-plugin
Maven plugin for running yarn commands.

## Configuration
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.maritims</groupId>
            <artifactId>yarn-maven-plugin</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <configuration>
                <install>true</install>
                <script>build</script>
                <nodePath>node</nodePath>
                <major>16</major>
                <minor>14</minor>
                <patch>2</patch>
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
| Option         | Type    | Value                    | Explanation                                                                                                                 |
|----------------|---------|--------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| nodePath       | string  | node                     | Path to directory within project base directory where node should be downloaded and installed.                              |
| major          | int     | 16                       | Major version of node.                                                                                                      |
| minor          | int     | 14                       | Minor version of node.                                                                                                      |
| patch          | int     | 2                        | Patch version of node.                                                                                                      |
| sourceCodePath | string  | svelte, node, vue, react | Path to directory within the src directory containing package.json. The directory should live alongside the java directory. | 
| install        | boolean | true, false              | Indicates whether or not packages should be installed.                                                                      |
| script         | string  | build                    | Indicates which script to run. The script must be present in package.json.                                                  |

## Install npm packages
`mvn com.github.maritims:yarn-maven-plugin:<version>:npm -Dinstall`

## Run npm script
`mvn com.github.maritims:yarn-maven-plugin:<version>:npm -Dscript <script>`

## Define output directory for npm build
How you define the output directory for your yarn build depends on the bundling software you decide to use. You typically want the yarn build to put the file(s) somewhere in src/main/webapp, or a sub-directory.