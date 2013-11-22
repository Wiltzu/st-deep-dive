Feature: Frontpage
In order to have more customers
As a bus transport company owner
I want to have a fancy web page
Scenario: Logo on top left of the page
When user opens a page
Then user sees the logo on the top left of the frontpage

Scenario: Newsfeed, 6 recent visible

Scenario: timetable accessible from frontpage
Given user opens the frontpage 
And there is the timetable with the 'submit' button
When user types travel information to timetable
And user press the 'submit' button
Then page opens containing showing the timetable. 

Scenario Outline: timetable not accessible 
Given user types 
And user types 
When user presses submit button
Then user geta
Examples: 
|departure|destination|error message|
| |Vantaa |Departure city missing|
|Vantaa | |Arrival city missing|
|Vantaa |Vantaa |Departure same as arrival|

