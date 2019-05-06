# Auction House

This java program is to simulate a system of multiple auction houses selling items, 
multiple agents buying items from multiple auction houses, and a bank to keep track of everyone’s funds. 
The bank will exist on one machine at a static known address, the agents and auction houses will be dynamically created on other machines.

## Getting Started

The game's source code is located in the corresponding archives, and the jars are available for download. Either using the jars or the source code, will you be able to reproduce a working game.

### Prerequisites

This application was built using java SDK 1.8 and JavaFx

## Graphical User Interface

### Auction House
For running the jar, the auction proxy needs to be ran with the following arguments:
    - Operating Port of the auction
    - Hostname of the bank
    - Port of the bank
    - Length (in milliseconds) of the item timer

Output of the AuctionHouse will be of similar format to the text below

```
THE TYPE OF REQUEST IS: GETALL
	All of the items were gotten
	Sent the response

THE TYPE OF REQUEST IS: BID
	Bid on item #32 with $1000.0
	The bid was ACCEPTANCE
	Sent the response
THE TYPE OF REQUEST IS: CLOSEREQUEST
	Checked if 1 can leave
	Sent the response

```

This text details the requests made from the multiple clients. 

Please note: Since System.out.println() is used, messages may appear in jumbled order under strenuous circumstances


### Bank
For running the jar, the auction proxy needs to be ran with the following arguments:
    - Operating Port of the Bank

Output of the Bank will be of similar format to the AuctionHouse

```

```

This text details the requests made from the multiple clients. Since the Bank offers different functionality than the Auction House, the commands will differ

Please note: Since System.out.println() is used, messages may appear in jumbled order under strenuous circumstances



![Main page](https://i.imgur.com/74jczka.png)
![Bank](https://i.imgur.com/0ltvDRl.png)
![Main Menu](https://i.imgur.com/jlgL8ue.png)

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


