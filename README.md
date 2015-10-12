## Bedrock

Core foundational server framework customized for backend systems

Bedrock gives you a boiler plate code to startup any web application project

This project can be run in a web application container such as Tomcat(Tested), Jetty(tested) and others like Glass Fish, web sphere etc.

Another parallel Google Appengine's version of web application is available in our repository. It is strongly coupled with Appengine's cloud services

## This repository uses below libraries

* Apache commons fraemwork - For basic file operations
* Joda time - For time based calculations
* Java Mail - For sending emails
* Apache Velocity - For templating the emails
* Junit - for unit and integration testing
* Spring framework - for Core spring features
* Spring Mongo Data framework - for Mongodb DB writes.
* Spring Data - for ORM on mongo DB and repositories.
* Mongo DB driver - Driver for mongo DB database. 
* Jersey - for Restful web services endpoints.
* Google's Gson - for object to Json and viceversa conversion.
* Java Servlet - for containerization of web service requests
* Swagger - for web services documentation
* Amazon AWS Java SDK - for uploading images to S3
* Test NG - For tests framework


## Explanation of the structure of the code 

Below are the packages in the existing project and their highlevel functionality

* database - Code related to database, models, beans and DAO's
* filter - Code related to web app filters, like logfilter, authorization filters etc.
* Mail - Code to send emails with templating.
* json - Code to convert to and fro of object and JSON objects.
* repository - MongoDB data repositories
* response  - Response objects
* service - Restful web services to serve data.
* utils - Utilities like data enctyption,  unique ID generation etc.

## How to run
1. Import the project into IntelliJ Idea.
2. Let the maven files be downloaded
3. Create a run configuration with tomcat as server
4. Run the project. 
5. Open in browser for default port 8888.

Full documentation is found at http://generaldevelopers.github.io/bedrock