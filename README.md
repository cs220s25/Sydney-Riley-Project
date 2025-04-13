## Project Overview
The project is a Discord Bot that plays Word Scramble. Word Scramble is a game in which a player is given a scrambled word and tries to guess the unscrambled version of the word correctly. The Discord Bot implements a multiplayer racing game, where each player tries to guess what the scrambled word is first in order to be awarded a point. After a word is unscrambled correctly, the next scrambled word in the selected game category will appear. There are two game modes that players can select to play: endless mode and best of # mode. After the game has ended, the results of the game will be shown with information on who won the game along with the end scores for each player that participated in the game.

### Game Commands
Provided below are the valid commands users can enter into the Discord Chat to interact with the Bot:
* **!categories -** get a list of categories.
* **!start <category> <mode> -** start a new game with specified category and selected game mode.
* **!start random <mode> -** start a new game with a random category and selected game mode.
* **!modes -** shows the different game modes.
* **!join -** executed by any user who wants to join the game.
* **!go -** used to start gameplay.
* **!status -** get the status of the game.
* **!quit -** ends the game and shows results.
* **!rules -** explains the rules of the game.
* **!help -** list the commands and basic explanation.

### System Diagram
![UMLDiagram](WordScrambleGameBot_UML.png)
