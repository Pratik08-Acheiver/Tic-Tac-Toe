#Instructions to setup project.

    1.Ensure MySQL is Installed and Running:

    2.Make sure MySQL server is installed and running on your machine.
        Create a MySQL Database with name tictactoe:
        query - CREATE DATABASE tictactoe;

    3.Update application.properties:
        spring.application.name=tictactoe
        server.port=8080
        spring.datasource.name=tictactoe
        spring.datasource.url=jdbc:mysql://localhost:3306/tictactoe?serverTimezone=UTC
        spring.datasource.username=your_username
        spring.datasource.password=your_password

        #JPA Configuration
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.show-sql=true

    4.Setup Port number for Front-end:
        *Navigate to package src/main/java/com/task/tictactoe/configuration/WebConfig.java
        *configure your front-end port no. like mine is http://127.0.0.1:5500.


