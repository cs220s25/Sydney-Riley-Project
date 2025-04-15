[![CI](https://github.com/cs220s25/Sydney-Riley-Project/actions/workflows/run_tests.yml/badge.svg)](https://github.com/cs220s25/Sydney-Riley-Project/actions/workflows/run_tests.yml)





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


## Setup Instructions
### Terminal
* Install [Homebrew](https://brew.sh)
  ```
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  ```

* Install [Git](https://git-scm.com/downloads/mac)
  ```
  brew install git
  ```

* Install [OpenJDK](https://formulae.brew.sh/formula/openjdk)
  ```
  brew install openjdk
  ```
  ```
  sudo ln -sfn /opt/homebrew/opt/openjdk/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk.jdk
  ```

* Install [Maven](https://maven.apache.org/install.html)
  ```
  brew install maven
  ```

* Install [Redis](https://redis.io/docs/latest/operate/oss_and_stack/install/archive/install-redis/)
  ```
  brew install redis
  ```
  
### Discord
1. Create an Account (https://discord.com)

2. Create an App (https://discord.com/developers/applications)
    * Create a "New Application".
    * Set the name of the App anything you want (e.g. WordScrambleBot).
    * Click on the "Bot" section in the menu on the left.
    * Set the username to be the desired name you want shown on Discord.
    * Find the section named "Privileged Gateway Intents".
    * Click the toggle button next to "Message Content Intent".
    * Click "Save Changes" that appears at the bottom of the webpage.

3. Obtain the App Token
    * Click on the "Bot" section in the menu on the left.
    * Find the section named "Token".
    * Click "Reset Token" and copy token for later use.

4. Authorize the App
    * Click on the "OAuth2" section in the menu on the left.
    * In the "Scopes" section, select the checkbox for "bot".
    * In "Bot Permissions" under "Text Permissions", click the checkbox for "Send Messages".
    * At the bottom under "Generated URL" click "Copy".
    * Paste the URL into a new browser tab.
    * When the page loads, under "Add to Server" select the desired server for app.
    * Authorize the bot, and then close this tab.

5. Create a Channel
   * Click the server that you authorized the bot in.
   * Click the plus sign next to "Text Channels".
   * Under the "Channel Type" section, select "Text".
   * Make the channel name `discord-bot` and then click "Create Channel".
  
### Secrets Manager
1. Create a Secret for Discord Token
    * Open the AWS Console, and go to [Secrets Manager](https://aws.amazon.com/secrets-manager/).
    * Click "Store a new Secret".
    * Under the "Secret Type" section, select "Other type of secret".
    * Under the "Key/value pairs" section, Key: `DISCORD_TOKEN`, Value: `<your discord token>`.
    * Click "Next" to go to the Configure secret page, Secret Name: `220_Discord_Token`.
    * Click "Next" through all remaining pages, then click "Store".

## Deployment Instructions
* Create a folder for your AWS Credentials.
  ```
  mkdir ~/.aws
  ```
* Create credentials file in AWS.
  ```
  cd ~/.aws
  ```
  ```
  nano credentials
  ```
* Copy and paste the "AWS CLI" information into the credentials file and save changes.
  * Learner Lab - Click on "AWS Details" at the top of lab interface, then click "Show" under "AWS CLI" to find information.
  
    ```
    [default]
    aws_access_key_id=<insert access key id>
    aws_secret_access_key=<insert secret access key>
    aws_session_token=<insert session token>
    ```

   * Full AWS Account - Log into [AWS Management Console](https://aws.amazon.com) and navigate to IAM. Go to the "Users" section and select the specific IAM user you want. Generate or find the AWS Access Key ID and Secret Access Key.

      ```
      [default]
      aws_access_key_id=<insert access key id>
      aws_secret_access_key=<insert secret access key>
      ```
        **NOTE**: If you are using the Learner Lab, then your credentials will change every time the lab is launched. If you are using a full AWS account, then your credentials are permanent and `~/.aws/credentials` won't need to be edited again.
     
### Run Locally
* Clone the repository.
  ```
  git clone https://github.com/cs220s25/Sydney-Riley-Project.git
  ```
  ```
  cd Sydney-Riley-Project/
  ```
* Run script to deploy project.
  ```
  chmod +x deployLocal.sh
  ```
  ```
  ./deployLocal.sh
  ```
* Stop the Bot from running, `ctrl + c`.

  **NOTE**: If you want to change the channel name to something other than `discord-bot`, then replace the string in `if (!event.getChannel().getName().equals("discord-bot"))` with your desired channel name. Make sure the channel is created in the designated server the Bot is in.
  * Change directory to repository.
    ```
    cd Sydney-Riley-Project/
    ```
  * Change "discord-bot" to desired channel name.
    ```
    nano src/main/java/edu/moravian/WordScrambleBot.java
    ```

### Run on AWS
1. Create a new EC2 Instance.
   * Log into AWS Management Console and navigate to EC2.
   * On the left, click on "Instances" and then click on "Launch instances".
   * Name the instance anything you want (e.g. DBot EC2 Instance).
   * Keep all the settings default except:
   	* Under section "Key pair", select `vockey`.
   	* Under section "Firewall" make sure `Allow SSH traffic from` is checked off.
   	* Under "Advanced Details" section, select `LabInstanceProfile` under "IAM instance profile".
   	* Upload or copy and paste information from `userData.sh` into "User data" section.
   * Click "Launch instance" and reload "Instances" page to show newly created instance is running.

2. Connect to EC2 Instance.
   * SSH to EC2 Instance.
     ```
     ssh -i /path/to/private_key.pem username@<public_ip_address>
     ```
     **Note**: You may have to wait about a minute until files are loaded and are properly running. Afterwards, the Bot you created should show active under the "Show Member List" of your designated server. You can use `sudo systemctl status redis6` and `sudo systemctl status dbot.service` in terminal to double-check that everything is actively runnning.
     

4. Redeploy EC2 Instance.
   * Change directory to cloned repository.
     ```
     cd /Sydney-Riley-Project/
     ```
   * Run script to redeploy project.
     ```
     ./redeploy.sh
     ```
     **Note**: You will see that the Bot you created should become inactive and then in a few seconds will become active under the "Show Member List" of your designated server.

5. Stop the Bot from running.
   * Navigate to EC2 on AWS Management Console.
   * On the left, click on "Instances" and then select the EC2 Instance you launched for Bot.
   * On the top next to "Connect", click "Instance state" then "Stop instance".
     
