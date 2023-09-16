# Tickets WebApp

This is a web application that allows users to manage tickets.

There is only one default user with administrator role (username: admin; password: admin).



## Application content

- Homepage: page accessible to everyone. You can see all existing tickets in the application.
- Create ticket: page accessible only to logged in users. You can create a ticket.
- Details ticket: page accessible to everyone. You can see all ticket details. If user is logged in, he can add the ticket to list of observed tickets.
- Edit ticket: page accessible only to admin user. You can edit a ticket.
- Watches: page accessible only to logged in users. You can see the tickets that the logged in user has added to his list of observed tickets.
- Watchers: page accessible only to admin user. You can see all users who are watching that ticket.
- Board: page accessible to logged in users. You can see a summary of tickets. Only user specified as "assignee" can edit time spent.
- Login/Logout: pages that allow a user to register and log into the application.
- Search bar: it allows you to search for a ticket by title, description or username.




## Getting started

1) Run a Docker container with the latest MySQL image:
   
   docker run -d -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=TicketsDB -p 3306:3306 mysql:latest

4) Connect to a MySQL database:
   - User: root 
   - Password: [none]
   - Database: TicketsDB

5) Run application on IDE

6) Go to this URL: http://localhost:8080/
