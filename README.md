# Programm Run Visualizer

An interactive, visual debugging tool for Java, allowing the user to freely traverse the flow of a programm and quickly analyse the programms behaviour. In Addition, the provided commandline interface allows to instrumment code without ecplicitely tracing or visualizing the control flow.  

**Note:** In the current version it is not possible to analyze the values of programm variables or define an entry point for the visualization.

## Table of Contents

- [Installation](#installation)
  - [Prerequisites](#prerequisites)
  - [Repository](#repository)
  - [Commandline](#commandline)
  - [Webforontend](#webfrontend)
- [Usage](#usage)
  - [CLI](#cli)
  - [Webinterface](#webinterface)
- [License](#license)


## Installation

### Prerequisites

Since the tool is a Java application you need to have Java installed, the minimum requirement is Java 17.  

We use Gradle as primary build tool and since a Gradle wrapper is provided no local installation of Gradle is required, however if you want to use a local version, make sure to use Gradle 8.3 or newer.

### Repository

You can clone the repository with  

`git clone https://git.key-project.org/prorunvis23/prorunvis.git`  

or any other common way, ie. via SSH or thrugh an IDE like VS Code or IntelliJ.  
After cloning the repository, make sure to also clone the frontend submodule. This can be done with:  

`git submodule update --init --recursive`  

This will clone the frontend submodule into a folder called frontend, which will be necessary if you want to use the webinterface.

### Commandline

To build the commandline application you will only need to build the prorunvis package. For this, using the gradle wrapper, type  

`./gradlew prorunvis:build`  

This will generate a .jar archive in prorunvis/build/libs/prorunvis.jar  

### Webfrontend

prorunvis-api:build, jar, endpoint/port für access  
For the web interface, the process is basically the same as for the commandline interface.  
You can build it using  

`./gradlew prorunvis-api:build`  

and the jar will be placed in prorunvis-api/build/libs/prorunvis-api.jar  
There will also be a plain jar, however that is not needed for running the service.  

## Usage

### Cli

To use the commandline interface, you need the prorunvis.jar  
As every JAR it can be executed with  

`java -jar prorunvis.jar`  

For information on arguments and input parameters, you can use the -h flag or have a look at the dokumentation (insert link here)

### Webinterface

For using the webinterface you need the prorunvis-api.jar  
Again, this JAR can be executed with  

`java -jar prorunvis-api.jar`  

This will start a spring service in an embeded tomcat server locally and you can now access the webinterface by navigating to localhost:8080/ in your browser.  
For additional information on implemented endpoints or how to customise the port the service is hosted on, have a look at the api documentation (insert link here)

## License

<a href="LICENSE">
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License: MIT">
</a>
