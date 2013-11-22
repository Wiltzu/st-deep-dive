Feature: Frontpage
Background:
Given we have page
Scenario: Logo on top left of the page
Given we have logo
And we have page
When page loaded
Then show logo on top left of the page

Scenario: 6 recent news on the page
Given we have news
When page loaded
Then show 6 most recent news on news feed

Scenario: news feed update
Given we have a news feed
When new news available
Then remove oldest news
And add new news

Scenario: contact information for purchasing tickets
Given we have contact information
And we have open hours information
When page loaded
Then show contact information and open hours

Scenario: contact information for parcel services
Given we have phone number for the parcel services
And we have open hours of the parcel office
When page loaded
Then show the contact information and open hours

Scenario: timetable accessible from front page
Given we have timetable service
When button is clicked
Then show the timetable front page

