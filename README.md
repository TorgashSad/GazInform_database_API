# GazInform_database_API
An API for database connection. This API uses a PostgreSQL JDBC drives to connect to a PostgreSQL database, but can be easily rebuilt for other database drivers. 
Build tool is Maven, log4j is used for a logging tool. Unit testing was implemented with junit and Mockito. 
Lombok used for more conсise code and maven surefire plugin used for sensitive information (password, etc) inhectionn into system properties.

Main class that executes queries is MyDAO. There methods implemented here - adding a user, finding user by its name (primary key) and updating specific user's surname.
Two test classes are present - one for the case when a real database is available, the other one woth no database available, dependencies are mocked with Mockito. 
