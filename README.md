# CS261: Software Engineering

## Prerequisites
* Apache Maven (https://maven.apache.org/)
* Apache Spark 2.1.0 (https://spark.apache.org/)
* MySQL Server (https://www.mysql.com/)

## Initial setup

It is imperative that you install and configure the prerequisites correctly as per the instructions on their websites. You need to make sure that all the environment variables are set for Apache Spark and Apache Maven.

For the initial setup, you would need to configure various properties such that the application can connect to the MySQL database and locate the models used for learning and training.

These options can be set in a file *user.properties* that should be placed under **src/main/resources/**. The corresponding directives to be set are:
* cs261.learning.models.directory=C:\\\\CS261\\\\models
* spring.datasource.url=jdbc:mysql://localhost/cs261
* spring.datasource.username=username
* spring.datasource.password=password
* spring.datasource.driver-class-name=com.mysql.jdbc.Driver