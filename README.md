# ing-sw-2018-Romeo-Sergi-Staccone

## Students' Matricola and Codice persona:
* Romeo Paolo: 			846080 - 10542300
* Sergi Guido: 			844175 - 10494956
* Staccone Francesco: 		847605 - 10504541

## Test coverage:
* global: 42.3% (from Sonar)
* related to the model: 83% (from IntelliJ)

## **UML** diagram link:
* https://github.com/fstaccone/ing-sw-2018-Romeo-Sergi-Staccone/blob/master/uml/uml_final.pdf
	
## Implemented functions:
* Complete rules
* RMI
* Socket
* GUI
* CLI
* Multiple matches
* Single-player match
	
## Project choices and implementation's limits:

### Main design patterns used
We have adopted the *MVC* pattern to develop the whole software structure.
In particular, we have implemented two different solutions for RMI and Socket. In the first case the controller is placed server side, while in the second case it is logically divided between server and client. This choice is mainly due to the *visitor pattern* used to parse the socket requests and responses, that allows us an easy serialization management through the network.
Moreover, as a consequence of the *MVC*, we have used the *observer pattern* for the sending of notifies from server to client. About design patterns, we have also decided to create the toolcards's effects adopting an hybrid solution between a factory and a *strategy pattern*. (Note about cards: we create only the actual cards randomically drawn to preserve memory)
We have just differed from regular *MVC* for some requests' syntax checks in view side, to lighten the server load.

### Network management and choices
In multiplayer match, a client can be logically disconnected from the server in three different ways.
The first one is the click made by the client on the *EXIT* button or on the *X* of the window; the second one is the closure of the process from IntelliJ through the red button; the third is the physical network disconnection due to cables disconnection or network crash.
In all these three cases, the disconnection works as we expect (player exit and relative notify to opponents) and completely preserves the server state. For what concerns client side disconnection management, we have decided to force the client process closures and oblidge the client to open the login stage again to reconnect (with differences between RMI and Socket connection). Even if it does not have a full cases coverage, it works well anyway.
In case of contemporary disconnection of both RMI and Socket connections due to cables we have noticed some minor slowdowns or notifies missings to the players still in game. We think this is caused by the physical network state and so it has not been possible to improve its management further. We finally report some minor and unpredictable slowdowns in RMI cases.
In case of singleplayer, we have dediced to not allow the reconnection, so when the player disconnects the match closes.
Finally, about connection and reconnection we have decided to avoid authentication to make the login process quickier for the user. This makes sense in a local context, but it has obvious limitations in a larger environmnent, so that someone could take your place in case of reconnection.
There is a minor bug concerning the names management and the reconnection: we have not fully covered the case in which a singleplayer tries to connect with the same name of a multiplayer player who has disconnected, but the server state is preserved and the operation does not succeed.

### Limits
Since we have tested the whole software using three different operating systems (Windows 10, Ubuntu Linux, MacOS X) and three different screen sizes and resolutions, we have been oblidged to find a compromise. This is represented by the current size of the GameBoard, but in MacBook Pro 13" we need to scale the screen size to obtain the wanted result.
Then we report the problem met during the JAR execution in the Linux laptop regarding the JavaFX window cutting.
Finally, we have not managed correctly an exception thrown in case of voluntary closure in Match Singleplayer, but the server state seems to be preserved.
Please note that our working configuration does not allow to use Rmi with Linux.

### Final tip:
We have decided to use Drag&Drop to use toolcards, while we used click-click to place a dice normally in GUI implementation.

#### Minor note by Francesco Staccone:
Please note that from 6th may I have changed my local Git username, due to some problems I had with the synchronization of local username and GitHub username. To be more clear, I had to change that because I was committing as "francescostaccone" while my GitHub username was actually "fstaccone", so that I was present neither as a contributor in the folder nor in the statistics made by GitHub. Currently I am present both in the contributors and in the statistics, but my preovious 63 commits (made with "francescostaccone") are not visible there. If you want to check them, they are located in the global list of commits. 
