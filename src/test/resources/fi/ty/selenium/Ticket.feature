Feature: Ticket reservation
Scenario: Search available connections
Given we have reservation page
And we have selected from location
And to destination
And date
When information is confirmed
Then list all availabSle connections for this selection

Scenario: Show the available connections
Given we a list of available connections
When the page is loaded
Then show the details

Scenario: Selecting one connection
Given the details of a chosen connection
When user choice is made
Then display all stops in connection and arrival times

Scenario: browse connections
Given a list of price details
When we make the choice
Then show the special price

Scenario: ticket

