# Simple Spring Boot Application
I made a generic version of my personal website ( http://BrianScottRussell.com ) for all to enjoy.

The following instructions assume that Java 8 and Maven 3.3+ is available from the command line.

##Prepare configuration files
There is a sample application.properties file under the resources directory, however it is recommended to make your own profiles.
e.g. application-production.properties

##Build the Spring Boot jar
Build the jar file by running the "package" Maven goal (note: jar will include dependencies):
	```
	mvn package
	```

##Running the Program
The Spring Boot application is a runnable jar so from the project root (jar file name depends on current version):
    ```
    java -jar ./target/simple-spring-boot-X.X.X.jar
    ```

## Accessing the Web Application
The Spring application is running on Tomcat on port 8580 by default, so you can simply open a browser window to http://localhost:8580