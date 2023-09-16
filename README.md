# Tickets WebApp

This is a web application that allows users to manage tickets.

There is only one default user with administrator role (username: admin; password: admin), but you can create other users who have the role of user.

## Getting started

1) Run a Docker container with the latest MySQL image:
   
   docker run -d -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=TicketsDB -p 3306:3306 mysql:latest

4) Connect to a MySQL database:
   - User: root 
   - Password: [none]
   - Database: TicketsDB

5) Run application on IDE

6) Go to this URL: http://localhost:8080/
