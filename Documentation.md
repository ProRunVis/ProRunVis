# CLI and API Documentation

## Table of contents
- [CLI](#cli)
- [API](#api)
  - [Endpoints](#endpoints)
  - [Customization](#customization)


## Cli

The commandline interface provides functionality instrument or trace a Java program.  
For an overview of how to use the tool you can type  

`java -jar prorunvis.jar -h | --help`

or have a look at the following explanation.    

**Input**:  
Unless using -h or --help, input is always required and does not need to be specified
by a flag.  
ProRunVis accepts the top-level directory of a Java program as input, please Note that  
if you want to trace the program and not only instrument it, the input program will have 
to be executable, i.e. have a defined main method.

**Help**:  
As mentioned before, for help or information on how to use the commandline tool, you can  
use either **-h** or **--help**.  
The usage-message will also be displayed for every case of unrecognized commandline arguments, 
for example missing input, or specifying unrecognized options.  

**Options**:  
There are two additional options that can be employed.  
If you only want to instrument a piece of code, for example a library that has no entry point or  
defined main method, you can use **-i** or **--instrument** to generate an instrumented version  
of your code. An example would look like this:

`java -jar prorunvis.jar path/to/input/ --instrument`

**Note**: if instrumenting is not specified, prorunvis will always default to tracing, meaning that  
the code will be fully instrumented, attempted to compile and execute and a full trace in JSON  
format will be included in the output directory.  

The second option can be used to specify an output directory, if needed.  
You can pass the path to the directory using **-o** | **--output** [directory]. This will set the output  
directory to the entered path and create the directory, if it does not exist.  
So an example might look like this:  

`java -jar prorunvis.jar input/ -i -o output/`

## Api

The communication between back and frontend is established by a Spring Boot API in form of
a Java service.  
The service can be started with:  

`java -jar prorunvis-api.jar`  

and will host the service on you machine. The default Port is localhost:8080, for information
on how to set a custom port please refer to the Customization part of this documentation.

### Endpoints

In the current version, the API exposes three primary Endpoints for communication.
- GET: "/"
- POST: "/api/upload"
- GET: "/api/process"

**/**:  
The default endpoint "/" is responsible for providing access to the webinterface to the user. When navigating to
localhost:8080/ this endpoint will host the frontend by returning a reference to index.html
using the Thymeleaf templating system.  

**/api/upload**:  
The POST endpoint "/api/upload" manages data upload to the embedded server. It expects a multipart form-data as
payload, consisting of all the files in the project to be uploaded. These files will be stored in a directory 
that is accessible by the server while retaining their relative path in the project structure.  

**/api/process**:  
The GET endpoint "/api/process" is responsible for processing an uploaded project for visualization.
It does not accept any kind of payload and solely works on previously uploaded data, utilizing the prorunvis
backend library to instrument and trace the program.  
The response contains a JSON string representing the complete traced information. For additional information
about the structure of the trace, you can refer to the [TraceNode documentation](TraceNodes.md).

### Customization

Using the default settings, the webinterface is going to be hosted at localhost:8080. If you want to specify a
different port, that can be done by setting the "server.port"-property in "prorunvis-api/src/main/resources/application.properties".  
For example, if you wanted to use port 9000, you can simply add  
`server.port=9000`  
to the properties file. Please note that for any changes to apply you will have to re-run the build-task in order to 
recompile the project.  

If you need to increase or limit the maximum size of files or http request the api can handle, you can also specify that by adding  
`spring.servlet.multipart.max-file-size=1MB` or  
`spring.servlet.multipart.max-request-size=10MB`  
and adjust the size accordingly.  

Note that you can use MB or KB as suffix to specify Megabyte or Kilobyte.