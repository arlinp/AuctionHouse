# Auction House

This java program is to simulate a system of multiple auction houses selling items, 
multiple agents buying items from multiple auction houses, and a bank to keep track of everyone’s funds. 
The bank will exist on one machine at a static known address, the agents and auction houses will be dynamically created on other machines.

## Getting Started

The game's source code is located in the corresponding archives, and the jars are available for download. Either using the jars or the source code, will you be able to reproduce a working game.

### Prerequisites

This application was built using java SDK 1.8 and JavaFx

## Graphical User Interface

Running the jar, the first window has a couple options:

![Main page](https://i.imgur.com/74jczka.png)
![Bank](https://i.imgur.com/0ltvDRl.png)
![Main Menu](https://i.imgur.com/jlgL8ue.png)

As you can see from the following screenshots...

## Algorithm Explanation

This is further explained in the documentation found in /docs/

## Bugs and Assumptions

* There are times inbetween when a auctionHouse unexpectedly closes and the item list updates that a player can hang onto the last result until the next refresh (Around 2 seconds)
* Everytime a socket gets disconnected, the AuctionProxy/BankProxy fails in a controlling manner to reduce the impact
* Assumed Bank is never closed.

## Built With
This was made using Java SDK 1.8
* [JavaFX](https://openjfx.io/) - The GUI framework used

## Authors

* **Connor Frost** - *Developing work* - [CS351 Project 5 Group 6](https://csgit.cs.unm.edu/frostc/)
* **Gavin McGuire** - *Developing work* - [CS351 Project 5 Group 6](https://csgit.cs.unm.edu/mcguireg/)
* **Arlin Pedregon** - *Developing work* - [CS351 Project 5 Group 6](https://csgit.cs.unm.edu/arlin/)


