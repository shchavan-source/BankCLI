# Pay Anyone Application

## Introduction
The Pay Anyone application is a command line interface to transfer money amongst 2 users.
The sender needs to login to the application before performing transfer.
The reciever may not be login to the application.

The logged in user can transfer money to multiple people.

## Command line options
Following are the useful commands

**login** <user_name> : This commands allows the user to login and displays its current balance. It also shows how much it owns from other users and how much other people are owning to this user.
**topup** <amount> : This command is used to topup the said amount to the logged in users account. This amount is then transferred to the users to whom the logged in user ows money.
**pay** <reciever> <amount> : This command is used to transfer amount from logged-in users account to the receivers account.
**exit** : This commands helps to get out of the application.

## Dependencies
To execute this code, we will need Java 1.7 and above. 
It also needs some space on the disk to store the user details.
The user running the application must have read+write access to the directory where the application is running.

## Assumptions
1. During the pay operation, the transaction happens only between the sender and the receiver.
2. The topup operation can handle settling multiple users as long as the logged in user has positive balance.
3. Since, validations are marked as future enhancements, it is expected that with the current version of the code only appropriate syntax are used for issuing the commands. Sending wrong commands might give incorrect results or make the application to stop working.
4. This is a maven application, so ensure that the maven setup is able to download the necessary files.

## Persistence
File persistence is used for storing the user details. Each user has its own file and data is stored in it. The files contain binary data so this can only be read by the application.
The persistence is enabled to store the user, balance and transaction details.
Thus, the users data is maintained over multiple restarts of the program.
The data is persisted in the **users** directory which must be created under the same directory where the application is running.

## Running the application from the editor
This application can be run directly from the editor which has the capability to run the main java method.
### Prerequisite
As the users are persisted in the files, while running the program for the first time ensure that the users directory under the main project directory is empty.

## Application execution
For executing the code, open the code in your favourite editor. The main method is present in the **PayAnyone** class.

## Executing the application jar
This application creates an executable jar that can be used to run the application.
### Prerequisites
1. Create an empty directory **users** in the same directory from where the application will be executed.
2. Make sure that the user running the application has read+write permission to the **users** directory.

### Building the jar
1. As this is maven application, navigate to the project directory that has the pom file and execute:
```mvn clean install -DskipTests```
2. This will create the fat-jar file with all the dependencies under the target directory.

### Running the jar
1. Ship the jar file to the directory where you need to execute the application.
2. Ensure that the **users** directory is created.
3. Ensure that the user running the application has **read+write** access to the users directory.
4. Run the following command to start the application
```java -jar <<jarfile name>>```
***Sample: java -jar bankCliCmds-1.0-SNAPSHOT.jar ***

### Sample output
```
$ java -jar bankCliCmds-1.0-SNAPSHOT.jar
Pay AnyOne Application
> login alice
Hello, alice!
Your balance is 0.0
> topup 100
Your balance is 100.0
> login bob
Hello, bob!
Your balance is 0.0
> topup 80
Your balance is 80.0
> pay alice 50
Transferred 50.0 to alice
Your balance is 30.0
> pay alice 100
Transferred 30.0 to alice
Your balance is 0.0
Owing 70.0 to alice
> topup 30
Transferred 30.0 to alice
Your balance is 0.0
Owing 40.0 to alice
Your balance is 0.0
> login alice
Hello, alice!
Your balance is 210.0
Owing 40.0 from bob
> pay bob 30
Transferred 0.0 to bob
Your balance is 210.0
Owing 10.0 from bob
> login bob
Hello, bob!
Your balance is 0.0
Owing 10.0 to alice
> topup 100
Transferred 10.0 to alice
Your balance is 90.0
Your balance is 90.0
> exit
$
```


## Future enhancements
1. Validation of the inputs: Currently there are no validations for the user inputs, in this version, the user is expected to provide correct inputs. In future, better validations will be included.
2. Error messages: The error messages currently are the JAVA exceptions thrown. More user friendly error messages will be added.
3. Logging frame-work: Current version contains basic logging framework from java.util package. The logging framework will be upgraded in the future releases.
4. JUnit test cases: Only the basic test cases are included in the current version. More extensive JUnit testing cases will be included in the future release.
