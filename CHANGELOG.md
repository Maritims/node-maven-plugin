# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- `NpmMojo` now accepts the property `environmentVariables` in the format `FOO_BAR=helloworld BAR_BAZ=loremipsum` and so on, just like you would prefix an `npm` command on the command line.

## [1.0.3] - 2022-24-30
### Added
- Added node bin directory to ProcessBuilder path environment variable.
- Created Dockerfile to test project without pollution from host system.
- `NodeWrapper.extract` now takes a `boolean` argument `verbose` which currently only dictates whether to verbosely log archive extraction.
- `PackageJson.get` now handles exceptions related to opening the file `package.json` and deserialization attempts.

### Changed
- Extraction is now skipped if the target directory already exists.

### Removed
- Removed unnecessary `IOException` from `NpmWrapper.run` method signature.

## [1.0.2] - 2022-04-25
### Added
- Added javadoc, source and gpg plugins

### Changed
- Group id was changed from com.github.maritims to io.github.maritims
- CI now uses `mvn verify` instead of `mvn package`

## [1.0.1] - 2022-04-24
### Added
- Npm mojo.
- Npm wrapper.
- Node installer.
- Initial commit.

### Removed
- Yarn mojo.

[Unreleased]: https://github.com/Maritims/node-maven-plugin/compare/node-maven-plugin-1.0.3...HEAD
[1.0.3]: https://github.com/Maritims/node-maven-plugin/compare/node-maven-plugin-1.0.2...node-maven-plugin-1.0.3
[1.0.2]: https://github.com/Maritims/node-maven-plugin/compare/node-maven-plugin-1.0.1...node-maven-plugin-1.0.2
[1.0.1]: https://github.com/Maritims/node-maven-plugin/compare/node-maven-plugin-1.0.0...node-maven-plugin-1.0.1
[1.0.0]: https://github.com/Maritims/node-maven-plugin/releases/tag/node-maven-plugin-1.0.0