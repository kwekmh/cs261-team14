# CS261: Software Engineering

## Prerequisites
* Java 8
* Apache Maven (https://maven.apache.org/)
* Apache Spark 2.1.0 (https://spark.apache.org/)
* MySQL Server (https://www.mysql.com/)

## <a name="initial"></a>Initial setup

It is imperative that you install and configure the prerequisites correctly as per the instructions on their websites. You need to make sure that all the environment variables are set for Apache Spark and Apache Maven.

For the initial setup, you would need to configure various properties such that the application can connect to the MySQL database and locate the models used for learning and training.

These options can be set in a file *user.properties* that should be placed under **src/main/resources/**. The corresponding directives to be set are:
* cs261.learning.models.directory=C:\\\\CS261\\\\models
* cs261.learning.checkpoints.directory=C:\\\\CS261\\\\checkpoints
* cs261.uploads.directory=C:\\\\CS261\\\\uploads
* spring.datasource.url=jdbc:mysql://localhost/cs261
* spring.datasource.username=username
* spring.datasource.password=password
* spring.datasource.driver-class-name=com.mysql.jdbc.Driver

If you want to allow larger file uploads, you need to set the following properties in *user.properties*:
* spring.http.multipart.max-file-size=512MB
* spring.http.multipart.max-request-size=512MB

## Instructions on running the system

**NOTE:** The build process includes unit tests that rely on the presence of a physical database for testing. Therefore, before you can use Apache Maven to build the software package, you need to ensure that you have set up MySQL properly.

1. Install Apache Spark and Apache Maven as per the instructions in their respective documentations.
2. Create a MySQL database and execute the schema.sql dump on the newly created database (e.g. `source schema.sql` in the MySQL console).
3. Create a MySQL user for the system and make sure that you assign to it the necessary privileges for reading and writing to the database. (e.g. `CREATE USER 'cs261'@'localhost' IDENTIFIED BY 'password'; GRANT ALL PRIVILEGES ON cs261.* TO 'cs261'@'localhost'; FLUSH PRIVILEGES;`)
4. Create three separate directories in a location of your choice for the following three purposes: models, checkpoints and uploads.
5. Copy the files underneath the *models/* directory to the directory that you have created for models in **Step 3**.
6. Configure *user.properties* as specified above in [Initial Setup](#initial) with the MySQL database, MySQL credentials and locations of the directories.
7. Run `mvn package` in the root of the source code directory.
8. Execute `java -jar target/cs261-1.0-SNAPSHOT.jar` in the root of the source code directory to run the system.
