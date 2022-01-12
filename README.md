# Awair_Test_Project

This app gets event list from API and parses the information after sorting according to start time and end time.
In addition, it determines conflicting events in times and show in Magenta color.

## Project structure
* MVVM
* Hilt dependency injection
* Repository pattern
* Jetpack Compose
* Coroutine
* Flow


## App features
* Get event list from api (infinite scroll)
* When API fails it shows update failed pop up and asks user to retry by showing alert dialog
* Feeds user with Toast message wheather API request is successful or not
* Groups the event list by date after sorting in chronological order.
* Event list items are divided with sections by date.
* When there are conflicting events the items are colored with magenta
* If title is empty it will set title with 'No Title'
