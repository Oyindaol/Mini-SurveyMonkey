[![Java CI with Maven](https://github.com/Oyindaol/Mini-SurveyMonkey/actions/workflows/maven.yml/badge.svg)](https://github.com/Oyindaol/Mini-SurveyMonkey/actions/workflows/maven.yml)

# Mini-SurveyMonkey
Mini-SurveyMonkey is a web application that allows surveyors to create and manage surveys with different types of questions and compile meaningful results from user responses. This project simulates a simplified version of popular survey platforms, designed to be flexible and user-friendly.

Features
Create Surveys: Surveyors can create new surveys and add questions of various types.

Question Types:

Open-ended (Text): Allows users to input free-form text answers.
Numeric Range: Requests a number within a specified range.
Multiple-choice: Presents users with multiple options to select from.
Dynamic Survey Forms: Surveys are displayed as forms dynamically generated based on each question type, making it easy for users to respond.

Close Survey: Surveyors can close a survey at any time, preventing new responses.

Results Generation:

Open-ended Questions: Lists responses as entered.
Numeric Range Questions: Generates a histogram to display the distribution of responses.
Multiple-choice Questions: Generate a pie chart summarizing the choices selected.

# Milestone 1
For Milestone 1, an early prototype was expected to demonstrate basic functionality. This prototype needed to showcase at least one operational use case, which collected data from the backend, processed it, and displayed the result (a simple display was acceptable). 

We set up a GitHub repository with continuous integration (CI) and deployed the application live on Azure. We utilized GitHub features like Issues, Kanban boards, code reviews and integration testing. 
Our team completed several key tasks to establish a functional prototype. We set up the project with a basic implementation of the MVC pattern and further developed the front end using HTML and CSS to enhance user interaction. Also, we planned out the database structure for the back end where data will be collected, processed, and displayed. Lastly, we wrote unit tests, set up the test environment, and created test paths and cases, making minor adjustments to other classes as needed. This work contributed to a foundation for Milestone 1.

![image](https://github.com/user-attachments/assets/c2e5f4eb-1d0c-46e9-afe0-7d2b9a06a375)

## What's Next?
For Milestone 2 we plan on implementing a landing home page for the Mini-Survey Monkey application which will allow users to choose either to create a survey or fill out a survey. Additionally, we want to restructure our Question class to handle the different types of Questions and overall further improve the front end and back end of the web application.






