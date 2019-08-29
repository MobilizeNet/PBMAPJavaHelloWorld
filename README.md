# PowerBuilder to Java (Java) Hello World Sample App

This repo contains the source code and binaries for this app.


# How to Build

If you want to build this app:

- BackEnd: you can use the `./build.ps1` or `./build.sh` scripts
- FrontEnd: you can use the `./buildFrontEnd.ps1` or `./buildFrontEnd.sh`

# Code Structure

When you migrate your app from Powerbuilder to Java you will have 3 main folders:

- WebApp
- Target
- ReferenceApplication

## WebApp
Angular FrontEnd is inside this folder.

This folder is structured as:
- sampleSite
    - sampleSite-angular
        - src
            - app
                - components
                    - sample
                        - d_sample_list
                        - w_sample
    - wwwroot (this is generated after Angular Compilation)


## Target
The migrated powerbuilder code to java is inside this folder.

## ReferenceAppl
The project to generate the WAR is here.

# Running the app directly from your browser

[Gitpod](https://gitpod.io) is a great company that allows you to get your repo and hosted on a cloud IDE.

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/MobilizeNet/PBMAPJavaHelloWorld)

Below you can see an screenshot of this repo on Safari on MAC OS

![PBMAPHelloWorld](./ScreenShot.png)

