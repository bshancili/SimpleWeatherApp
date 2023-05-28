# SimpleWeatherApp

This app is a simple weather application for Android that allows users to view weather information for different cities. It uses the OpenWeatherMap API to retrieve weather data, and implemented using Kotlin on Android Studio.

## Features

### Weather Page

The Weather Page is the main tab of the app, where users can view weather information for a selected city. The page displays the following details:

- City name
- Current temperature
- Feels like temperature
- Highest and lowest temperature
- Humidity information
- Wind information
- Sea level information
- Coordinates of the city
- Current weather description

The weather information is fetched from the OpenWeatherMap API using the city's ID. By default, the selected city is Istanbul when the app first opened.

### City Selection/Search Page

The City Selection/Search Page allows users to browse and select cities from the avaible cities. The information about cities are stored in a local JSON file "city.list.json" in the assets folder. 

This page provides the following functionality:

- Displays the available cities
- Allows users to select a specific city
- Enables users to search for cities using the search area

When a city is selected, the user is automatically redirected to the Weather Page with updated weather information for the chosen city.
