![gif](/readme/app.gif)

# Android application

Android application made with Java and Android Studio.

Can login, show sport list and show sport details

## Login activity

Sends a post request to the API, if it gets an "OK" response, it will continue the main activity.
otherwise, it will display a login failure message.

## Main activity

It will send a get request to the API to get a JSON array of all the sports. Then it will show the sports array
in the application. And by pressing at events, it will show it's details.