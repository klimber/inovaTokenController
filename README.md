# InovaTokenController

Welcome to InovaTokenController. This project aims to provide a simple backend to manage and provide PowerBI embed Tokens.

## How it works

We use app only credentials (service principal) to get a token from Azure. With that token we call [PowerBi REST API](https://docs.microsoft.com/en-us/rest/api/power-bi/) to list workspaces, reports in those workspaces, and generate embed tokens for those reports.

We use spring security to secure our API and have a VERY basic user management in order to control access.

## How to test it

You will need:

 - Java 12

Then you can download the project with:

    git clone https://github.com/klimber/inovaTokenController.git

Go into the project folder:

    cd inovaTokenController

Build it with gradle wrapper:

    ./gradlew build # Unix users
    or
    gradle.bat build #Windows users (need to check on this)

Include your azure credentials and change other settings at will in the **application-dev.yml** file.
We plan to make a guide on how to properly configure the  azure app, for now see [this](https://community.powerbi.com/t5/Developer/App-only-authentication-oAuth2-token-request/td-p/759839) forum article and [this](https://www.youtube.com/watch?v=ZhMfpdXLIw0) video
In the **application.yml** file, change the active profile to **dev**

Now you can run the server with:

    ./gradlew bootrun # Unix
    or
    gradle.bat bootrun # Windows

## API Documentation

**TODO**

## How to contribute

**TODO**

> Written with [StackEdit](https://stackedit.io/).
