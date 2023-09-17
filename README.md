# Tickets WebApp
This is a web application that allows users to manage tickets.

there is only one default user with administrator role (username: admin; password: admin), while the others are users have user role and must register in the application and login.


## Application content.

### Homepage
Page accessible to everyone where you can see all existing itckets in the application.

### Create ticket
page accessible only to logged in users where you can create a ticket by specifying title, assigneee, two dates, type, estimated time, description and attachment (optional).

### Details ticket
page accessible to all where you can see all ticket details. If user is logged in he can add the ticket to his pesronal list of observed tickets.

### Edit ticket
page accessible only to admin user where you can edit a ticket by specifying title, assigneee, two dates, type, status, estimated time, description and attachment (optional).

### Watches
page accessible only to logged in users where you can see the tickets that the logged in user has added to their list of watched tickets.

### Watchers
page ccessible only to admin user where you can see all users who are watching that particular ticket.

### Board
page accessible to logged in users where you can see a summary of tickets. Only user specified as "assignee" can edit time spent.

### search
search bar that allows you to search for a ticket by title, desctiption, or username.

### login/logout
pages that allow a user to register and log into the application.


## Getting started

1) Run a Docker container with the latest MySQL image:
   - docker run -d -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=TicketsDB -p 3306:3306 mysql:latest

2) Connect to a MySQL database: 
  - User: root 
  - Password: [none]
  - Database: TicketsDB

3) Run application on IDE

4) Go to this URL: http://localhost:8080/