# BACKEND PROJECT
Spring boot backend project with Postgresql. REST and SOAP API's are used.
There is also cache on the project. With schedule job every 30 seconds cache gets cleaned.
There is also log file. In case of desire of application.proporties file you can use database of data.json file as data source.

## HOW TO INSTALL AND RUN
Anyone can clone repository or download the zip file. Also there should be PostgreSQL on your computer.
Create database name "SWAInternalHw". And run the script below.
```sql
CREATE TABLE subscriber (
	id bigint SERIAL PRIMARY KEY,
	name VARCHAR ( 70 ),
	msisdn VARCHAR ( 50 ),
	status VARCHAR ( 20 )
);
```
After creating the table, you can edit the application.properties file according to your database information.
Later that you can run the project.
Postman requests are available postman-requests-backend-project.postman_collection.json file on project. You can import the requests to Postman and run.