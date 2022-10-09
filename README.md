# Multithreaded-Kahhot-game

Multiplayer Kahoot Game by using Java to simulate the common functions of creating and playing an online game. 
By using Multithreading, Networking, OOP, Exception Handling developed Kahoot.com as a server side and Kahoot.it as a client side. 

Program Design

1.GameServer:
Main JavaFX GUI file.
Generate Random 6 digits number as pin.
Questions and Results of the Game will appear here.


2.Player:
Enter the pin (generated on the server side) of the game.
Has username, points.

3.Questions:
Abstract class;
Read from the .txt file that was chosen by “Choose File” .
Shuffle order of the options.
Has 2 types of Test questions: Question with 4 options and True/False question.

4.Test class
Extends the Question class;
Creates a multiple-choice test question;
Several possible options (only 4 for now), including single correct answer;
Labels are set automatically, starting from A, B, C, ….

5.FillIn class (Fill + In)
Extends the Question class;
Fill in the blanks question type;
Only one blank , that is indicated by “{blank}” string in a text file;
The answer is case-insensitive.

6.Quiz class
Consists of list of questions;
Throws InvalidQuizFormatException if the file format is not correct; 

7.InvalidQuizFormatException class
A special exception type thrown if the input file format is not correct;
Prints an appropriate error message.

8.Quiz text file
Each question separated by exactly one empty line;
Two types of questions: Test or FillIn;
First line: question description; Second line: answer;
If only one answer line, then it is a FillIn type question;
In case there are multiple answer lines following the question description, this is a Test type question, the first answer being the correct one;

Sample run of the program:
Kahoot.com - as a server side
Kahoot.it - as a client side 



