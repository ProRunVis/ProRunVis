# Program Run Visualizer 

An interactive, visual debugging tool for Java, allowing the user to freely traverse the flow of a program and quickly analyse the programs behaviour. In addition, the provided command line interface allows to instrument code without explicitly tracing or visualizing the control flow. 

**Note:** In the current version it is not possible to analyse the values of program variables or define an entry point for the visualization. 

## Table of Contents 
- [Installation](#installation)
  - [Prerequisites](#prerequisites)
  - [Repository](#repository)
  - [Command line](#command-line)
  - [Web frontend](#web-frontend)
- [Usage](#usage)
  - [CLI](#cli)
  - [Web interface](#web-interface)
  - [Examples](#examples)
- [Credit](#credit)
- [License](#license)

## Installation 

### Prerequisites 

Since the tool is a Java application you need to have Java installed, the minimum requirement is Java 17. 

We use Gradle as primary build tool and since a Gradle wrapper is provided no local installation of Gradle is required. However if you want to use a local version, make sure to use Gradle 8.3 or newer. 

### Repository 

You can clone the repository with
```
git clone https://github.com/ProRunVis/ProRunVis.git
```
or any other common way, i.e. via SSH or through an IDE like VS Code or IntelliJ. 
After cloning the repository, make sure to also clone the frontend submodule. This can be done with: 
```
git submodule update --init --recursive
```
This will clone the frontend submodule into a folder called frontend, which will be necessary if you want to use the web interface. 

### Command Line 

To build the command line application you will only need to build the prorunvis package. For this, using the Gradle wrapper, type: 
```
./gradlew prorunvis:build
```
This will generate `prorunvis/build/libs/prorunvis.jar`.

### Web Frontend 

For the web interface, the process is the same as for the command line interface. 
You can build it using: 
```
./gradlew prorunvis-api:build
```
The jar will be placed in `prorunvis-api/build/libs/prorunvis-api.jar`.
There will also be a plain jar, however that is not needed for running the service. 

## Usage 

### CLI

To use the command line interface, you need the `prorunvis.jar`.
As every JAR it can be executed with:
```
java -jar prorunvis/build/libs/prorunvis.jar
```
For information on arguments and input parameters, you can use the `-h` flag or have a look at the [documentation](Documentation.md).

### Web Interface 

For using the web interface you need the `prorunvis-api.jar`.
Again, this JAR can be executed with:
```
java -jar prorunvis-api/build/libs/prorunvis-api.jar
```
This will start a spring service in an embedded tomcat server locally and you can now access the web interface by navigating to https://localhost:8080/ in your browser.
For more information on implemented endpoints or how to customize the port the service is hosted on, have a look at the [documentation](Documentation.md).

### Examples

There is a separete [repository with a collection of examples](https://github.com/ProRunVis/ProRunVis-examples).

## Credit

This was developed as student software project in winter 2023/2024 at Technical University Darmstadt.

## License
 <a href="LICENSE">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License: MIT">
</a> 
