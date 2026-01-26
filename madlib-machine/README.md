# [Madlib Machine](https://github.com/adam-lev-barnett/madlib-machine) for web app integration
A retooling of my file-based Madlib Machine that processes the text in memory for eventual integration into a full-stack web application.

This version uses Spring Boot, which will eventually connect to a [front end using React and Typescript](https://github.com/adam-lev-barnett/madlib-frontend).

## Intended workflow
1. User submits a form with the text they want to Madlibify and how many madlibifiable words they wish to skip (so the every word isn't blanked)
2. The app returns a form with fields corresponding to the parts of speech of each removed word
3. User fills out the fields with replacement words
4. The app returns the filled-out madlib

## In-progress
This project is a learning experience for me, so completion is contingent on me getting more comfortable with front end development, integration of front and back end, as well as app deployment.