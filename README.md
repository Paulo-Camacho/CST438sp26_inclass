# Project 01 Retrospective and overview

[Video Walkthrough](https://youtube.com/shorts/yxECam1A5QM?si=1IvXp443fGK2E22e) 

[Github Repo](https://github.com/Paulo-Camacho/CST438sp26_inclass)

## Overview
A web application that recommends video games to users based on selected preferences such as genre, platform, and play style. Users can explore games, view details, and receive random recommendations [using a public game API (Free-To-Play)](https://www.freetogame.com/api-doc).

## Introduction

* How was communication managed: Slack and in class
* How many stories/issues were initially considered: 9
* How many stories/issues were completed: 14

## Team Retrospective

### Mauricio Reynoso

[Link to my pull requests](https://github.com/Paulo-Camacho/CST438sp26_inclass/pulls?q=is%3Apr+is%3Aclosed+author%3Amreynoso123) and [Link to my issues](https://github.com/Paulo-Camacho/CST438sp26_inclass/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3Amreynoso123)

#### What was your role / which stories did you work on
I was responsible for building the landing page after login. I implemented the Random Game suggestion feature, then added a Top 10 Popular Games carousel using LazyRow. I reused our existing API and ViewModel logic to keep architecture consistent, and added Compose UI tests to ensure the features worked correctly. I also managed the README file.

+ What was the biggest challenge?
  + Overcoming the challenge of the .idea folder from getting added to main which would cause issues when pulling and running the app.
+ Why was it a challenge?
  + After multiple attempts of working together, we finally got the .gitignore file to properly ignore .idea and removed it from the repo.
+ Favorite / most interesting part of this project
  + Seeing how everyone was able to accomplish their roles at their own pace instead of comparing them to my own.
+ If you could do it over, what would you change?
  + I would try to coordinate deadlines when an issue should be completed so that we can reduce merge conflicts.
+ What is the most valuable thing you learned?
  + I learned to be more comfortable with using Git. I'm not perfect, but this project helped introduce its concepts.

### Paulo Camacho
[Link to my pull requests](https://github.com/Paulo-Camacho/CST438sp26_inclass/pulls?q=is%3Apr+is%3Aclosed+author%3APaulo-Camacho) and [Link to my issues](https://github.com/Paulo-Camacho/CST438sp26_inclass/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3APaulo-Camacho)

#### What was your role / which stories did you work on
I worked on the initial app, the fetching of api, the login function, persistent login.

+ What was the biggest challenge? 
  + The biggest challenge was merging everyone's commit such that there are no conflicts.
+ Why was it a challenge?
  + It was not that bad after getting the hang of the github ui.
+ Favorite / most interesting part of this project
  + My favorite part was getting more comfortable with version control.
+ If you could do it over, what would you change?
  + I would not change anything. I enjoyed working with my team and project.
+ What is the most valuable thing you learned?
  + Doing the work early and labeling branches.

### Joshua Chavez
[Link to my pull requests](https://github.com/Paulo-Camacho/CST438sp26_inclass/pulls?q=is%3Apr+is%3Aclosed+assignee%3AAffirmingfour61) and [Link to my issues](https://github.com/Paulo-Camacho/CST438sp26_inclass/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3AAffirmingfour61)

#### What was your role / which stories did you work on
I worked mainly on the admin system, game details, and review functionality. I implemented the admin section that allows an admin user to add new users to the database and remove existing users. I also developed the game details view, which lets users click on a game to see a full description, including additional information such as genre, platform, publisher, and release date. In addition, I created the user review system at the bottom of the game details page, where users can rate a game using stars and leave a comment, with the reviews being stored and displayed from the database.

+ What was the biggest challenge? 
  + Managing pull requests and merges
+ Why was it a challenge?
  + We were all new to git/github and not everyone followed convention
  + How was the challenge addressed?
  + I went to the TA for help and used ChatGPT and web resources to get more comfortable with git.
+ Favorite / most interesting part of this project
  + Finally getting the IDs from the API calls to store in the ROOM database
+ If you could do it over, what would you change?
  + I would get the ROOM database setup FIRST
+ What is the most valuable thing you learned?
  + Do the work early and document EVERYTHING

### Ciaran Moynihan
[Link to my pull requests](https://github.com/Paulo-Camacho/CST438sp26_inclass/pulls?q=is%3Apr+is%3Aclosed+assignee%3Aciaranmoynihan) and [Link to my issues](https://github.com/Paulo-Camacho/CST438sp26_inclass/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3Aciaranmoynihan)

#### What was your role / which stories did you work on
I worked mainly on the sorting and filtering of the api results on the games page. I made the two dropdowns that allow the user to sort the list of games by alphabetical order, relevancy, and popularity, and the dropdown that allows the user to filter the list of games by genre. I also made the search bar, that allows the user to input the title of a game, and find that game. I also made the database that stores the user's login information and the commands to interact with the table.

+ What was the biggest challenge? 
  + The biggest challenge for me was the ROOM database.
+ Why was it a challenge?
  + I had to read through a lot of documentation and worked on making sure that the inspector could see the table updating in real time.
  + How was the challenge addressed?
  + By reading the documentation, and through trial and error, I ended up setting up the database just fine.
+ Favorite / most interesting part of this project
  + Working with the API for the dropdowns was interesting, and making sure that both worked and filtered at the same time was also fun to work on.
+ If you could do it over, what would you change?
  + Get the ROOM database figured out earlier, so we could have the API inteact more with our database in some form/feature.
+ What is the most valuable thing you learned?
  + The value of testing/test files to make sure that the database was working properly.

## Conclusion

- How successful was the project?
  - We wanted to create an app that will recommend users video games based on different options and provide personal reviews and log past searches.
  - We completed about 90% of the original ideas, then implemented newer ones such as sorting, a carousel of popular game, and a search function.

- What was the largest victory?
  - Feeling a sense of trust between each of us to complete our roles when compared to past group experiences. 
- Final assessment of the project
  - It was fun, informative, and a great learning experience. The project is less about learning to code in an unknown programming language, but instead being about team coordination and building familiarity with Git and GitHub.
