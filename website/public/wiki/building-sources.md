## Overview

You can easily build Instafel projects by following the guidelines below. Ensure you have Java 17+ and NPM 20+ installed before proceeding with the build process.

---

## App

The Gradle build file is located at **app/build.gradle.kts**. This project requires Java 17 or higher to build. The app can be built in two channels: **Debug** and **Release**. APK outputs will always be generated at **app/output/ifl-app-xxxxx-tag.apk**.

To generate a Debug APK, use the following command:  
`./gradlew :app:generate-app-debug`  
To generate a Release APK, run this command:  
`./gradlew :app:generate-app-release`

Note that this project does not use the same libraries as the official Instagram APK (e.g., Instagram APKs use a restricted AndroidX library), which may result in varying behavior.

---

## Updater

The Gradle build file is located at **updater/build.gradle.kts**. This project requires Java 17 or higher to build. The updater can be built in two channels: **Debug** and **Release**. APK outputs will always be generated at **updater/output/ifl-updater-xxxxx-tag.apk**.

To generate a Debug APK, use the following command:  
`./gradlew :updater:generate-app-debug`  
To generate a Release APK, run this command:  
`./gradlew :updater:generate-app-release`

---

## GPlayAPI

This project produces a standalone JAR file. The output file is saved as **gplayapi/output/ifl-gplayapi-xxxxx.jar**. To generate the JAR file, run the following command:  
`./gradlew :gplayapi:build-jar`

---

## Website

The website is built using NextJS. For information on dependency installation and package management, refer to the [NextJS Docs](https://nextjs.org/docs). There are no exceptional cases for the Instafel website.

---
