Feature: Frontpage

Background:
Given the front page is loaded

Scenario: Logo on top left of the page
Then logo is on top left of the page

Scenario: 6 recent news on the page
Given the front page has news feed
Then 6 most recent news should be on the news feed

Scenario: contact information for purchasing tickets
Then the contact information for ticket purchase and opening hours should be visible

Scenario: contact information for parcel services
Then the contact information for parcel services and opening hours should be visible

Scenario: timetable is accessible from the front page
Given the front page has the schedule search
When the schedule search is done with a trip from 'Turku' to 'Helsinki' and departure date is current date
Then the timetable results should be visible 