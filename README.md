![LivelyGig](http://static1.squarespace.com/static/55b995e0e4b04667a1da39a2/t/563b8a93e4b0a7b5800300e2/1450968381054/?format=400w)

[LivelyGig](http://www.livelygig.com/)'s freelance marketplace front end. Please join LivelyGig at [Slack](https://livelygig.slack.com/messages/general/) or [Twitter](https://twitter.com/LivelyGig/) for more information, community updates and the latest development.

[![Build Status](https://travis-ci.org/LivelyGig/ProductWebUI.svg?branch=master)](https://travis-ci.org/LivelyGig/ProductWebUI)

## Application Structure
    TODO

## Building
The build process requires several pieces of software to be installed on the host system:

* [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 8.0 or above
* [SBT](http://www.scala-sbt.org/download.html) 0.13.13 or above - This will download rest of the required softwares i.e. scala etc.
* Git client

Before running the application you need to have node.js installed

Then do npm install at the project root to install jsdom

To run the application, open a command line interface (CLI) follow the step below (run individual each command): 

    sbt runAll

Then visit the home page at http://localhost:9000/

To edit source code using IntelliJ IDEA editor please follow the instruction in Google Docs [here] (https://docs.google.com/document/d/1VyU5XtWzXugTa7R3odUEa8I1kmj_nVUa7VgrnkDHnQE/edit)

To install 'sbt', you may need to use [Homebrew](http://brew.sh/) on your mac:

    brew install sbt

Or you can configure after downloading and unzipping 'sbt' to your executable path by editing '.bash_profile' if using bash shell on unix or linux or mac system

    export PATH=$PATH:<SBT_DOWNLOADED_UNZIPPED_PATH>/sbt/bin

or in Windows system

    set PATH=%PATH%;<SBT_DOWNLOADED_UNZIPPED_PATH>\sbt\bin
    
Mobile project uses SRI for the mobile build on ios and android

    Start Andrtoid emulator
    cd mobile   
    yarn install
    npm start
    // open another terminal
    sbt ~android:dev
    react-native run-android
    

