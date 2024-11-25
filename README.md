[![Java CI with Maven](https://github.com/Oyindaol/Mini-SurveyMonkey/actions/workflows/maven.yml/badge.svg)](https://github.com/Oyindaol/Mini-SurveyMonkey/actions/workflows/maven.yml)

# Mini-SurveyMonkey (Group 17)
Mini-SurveyMonkey is a web application that allows surveyors to create and manage surveys with different types of questions and compile meaningful results from user responses. This project simulates a simplified version of popular survey platforms, designed to be flexible and user-friendly.

Features
1. Create Surveys: Surveyors can create new surveys and add questions of various types.
Question Types:
Open-ended (Text): Allows users to input free-form text answers.
Numeric Range: Requests a number within a specified range.
Multiple-choice: Presents users with multiple options to select from.

2. Dynamic Survey Forms: Surveys are displayed as forms dynamically generated based on each question type, making it easy for users to respond.

3. Close Survey: Surveyors can close a survey at any time, preventing new responses.

4. Results Generation:
Open-ended Questions: Lists responses as entered.
Numeric Range Questions: Generates a histogram to display the distribution of responses.
Multiple-choice Questions: Generate a pie chart summarizing the choices selected.



### ----------------------------------------COMPLETED MILESTONES---------------------------------------------
# Milestone 1
For Milestone 1, an early prototype was expected to demonstrate basic functionality. This prototype needed to showcase at least one operational use case, which collected data from the backend, processed it, and displayed the result (a simple display was acceptable). 

We set up a GitHub repository with continuous integration (CI) and deployed the application live on Azure. We utilized GitHub features like Issues, Kanban boards, code reviews and integration testing. 
Our team completed several key tasks to establish a functional prototype. We set up the project with a basic implementation of the MVC pattern and further developed the front end using HTML and CSS to enhance user interaction. Also, we planned out the database structure for the back end where data will be collected, processed, and displayed. Lastly, we wrote unit tests, set up the test environment with Jacoco and Mockito, and created test paths and cases, making minor adjustments to other classes as needed. This work contributed to a foundation for Milestone 1.

### Milestone 1 UML class diagram:
![image](https://github.com/user-attachments/assets/c2e5f4eb-1d0c-46e9-afe0-7d2b9a06a375)

### Milestone 1 Database Schema:
![image](https://github.com/user-attachments/assets/4a0262aa-6c72-4b56-9fed-072ae2e1824c)
### -----------------------------------------------------------------------------------------------------------------

# Milestone 2
For Milestone 2, the alpha release, the system is now somewhat usable, although not feature-complete. These are the features we have implemented:

1. The survey management feature allows users to create surveys through the SurveyController. Surveys are saved in the database and can be accessed by their unique ID or name, providing an easy way to retrieve and manage survey data.

2. Through the Question and Answer classes, users can add questions to surveys through the QuestionController. The app currently supports multiple question types, including Open-Ended (text), Numeric range, and Multiple Choice questions. Numeric questions allow validation for specified ranges, while Multiple-choice questions store options as a list. Each question is linked to its respective survey, ensuring proper organization and context.

3. The answer validation process allows users to respond to surveys using the AnswerController. This functionality displays all questions in a survey, captures users responses, and associates the answers with the appropriate survey and questions. All submitted answers are stored persistently in the database for future retrieval and analysis.

4. The application also includes functional **Navigation and Views**. Users should be able to search for surveys by name, making it easier to find specific surveys. Additionally, the application displays views of surveys, including their questions and responses, allowing for interaction with the survey data.

### This is our current UML class diagram:


### This is our current Database Schema:


### Testing
To test with Jacoco, simply use Maven to test and a target folder will be generated. Then navigate to target/site/index.html and open the index.html in your preferred browser. There you will be able to view the coverage report.

## What's Next?
For Milestone 3, we plan on:

1. Improving the validation for the answer submission feature. Currently, the validateAnswer method in Question ensures responses match question types but the current flow doesn't invoke validation directly in the controllers. This might result in invalid answers being saved.
2. Implementing the Feature Toggle Pattern in Spring, FF4J, as required for our presentation topic.
3. Work on error handling. Currently, the controllers rely on RuntimeException for error handling, which could cause abrupt failures.
4. Update the front end to handle the features added to the backend.







