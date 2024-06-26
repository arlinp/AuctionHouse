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
THE TYPE OF REQUEST IS: LOCK
	Locked funds to Account#: 1 | Amount locked: $1000.0 | Lock Number: $499111566
	Sent Message!

THE TYPE OF REQUEST IS: GETBALANCE
	Balance for Account#: 1 is $9000.0
	Sent Message!

THE TYPE OF REQUEST IS: GETTOTALBALANCE
	Total balance for Account#: 1 is $10000.0
	Sent Message!
	
THE TYPE OF REQUEST IS: ADD
	Added funds to Account#: 1 | Amount Added: $10000.0 | Old Balance: $0.0 | New Balance: $10000.0
	Sent Message!
```

This text details the requests made from the multiple clients and auction houses. Since the Bank offers different functionality than the Auction House, the commands will differ

Please note: Since System.out.println() is used, messages may appear in jumbled order under strenuous circumstances

### Client

Upon loading the Jar (or Source code). The user will see a following page:

![Main Menu](https://i.imgur.com/jlgL8ue.png)

Upon inspection, the user will be able to specify a hostname and port or be able to locally test the program

![Bank](https://i.imgur.com/0ltvDRl.png)

Upon entering in a valid configuration. The user will be forwarded to a nice page where he can request a bank account and add however much money is wanted

![Main page](https://i.imgur.com/74jczka.png)

After entering the auction house, the user will be able to view the items automatically pulled from the AuctionHouses. The user can then add funds and then bid on the items by clicking them from the table on the right.

After bidding on an item, the user must wait for the entire duration of the bid, or until the user gets notified that he was outbid.

Notifications work in the following way:
* A user gets notified if his bid was initially rejected
* A user gets notified when a past bid was outbid.
* A user gets notified when an item is won
* A user gets notified when he attempts to illegally leave

Notifications come in the form of popup windows.

Happy bidding!

## Algorithm Explanation

This is further explained in the documentation found in /docs/

## Bugs and Assumptions

* There are times inbetween when a auctionHouse unexpectedly closes and the item list updates that a player can hang onto the last result until the next refresh (Around 2 seconds)
* Everytime a socket gets disconnected, the AuctionProxy/BankProxy fails in a controlling manner to reduce the impact
* Assumed Bank is never closed.
* Bug: There is a chance that errors aren't fully caught when Auctions are closed
* Bug: An old bug popped up where the IP address received from the Bank is the loopback, so the IP transferred around is that of the loopback, this can be fixed with .getLocalAddress() instead of current configuration.

## Built With
This was made using Java SDK 1.8
* [JavaFX](https://openjfx.io/) - The GUI framework used

## Authors


* **Arlin Pedregon** - *Developing work* - [CS351 Project 5 Group 6](https://csgit.cs.unm.edu/arlin/)
    - Primarily created the following: 
    - Coordinated with partner to expand network capabilities within Bank:
        - This includes key edits to Bank, Account, and working with Connor to provide changes to the Bank
    - Implement the handling of exceptions from unexpected exits within the program
    - Helped implement the AuctionHouse, Bank, and Account functions
        - This included the transferring of funds from the bank
        - Also included the synchronization of fund transactions 
    - Primarily cleaned up code to conform to CS351 Project standards
    - Implemented the closing of AuctionHouse upon no objects
    - Implemented AuctionHouse's limited timed items functionality
    - Helped proofread and append final edits to the Documentation
            - Create the ReadMe and helped fill out crucial information
    - Compiled the Jars for the final product
* **Connor Frost** - *Developing work* - [CS351 Project 5 Group 6](https://csgit.cs.unm.edu/frostc/)
    - Created the AuctionProxy and BankProxy Network Design structure, which involved the following complexities:
        - The Proxies use Object streams for transportation of serialized objects, (AuctionRequest and BankRequest)
        - Each of the Proxies and Communicators make use of two theoretical threads:
            - One of which processes and responds to the incoming messages
            - The second is to asynchronously notify/get notified the user
    - Primarily created and handled the synchronization of the AuctionHouse
    - Helped create the Bank class to handle accounts
    - Created Interfaces of AuctionHouse and Bank to better facilitate network transfer / centralized understanding
        - Implemented the interfaces in most of the classes
    - Took over GUI to implement the following:
        - Implement the changing of screens for non-local testing
        - Implemented timed updates of the items that are being bid upon
        - Implemented notifications and the closeRequest feature
        - Updated the GUI format to look better
    - Fixed most of the AgentAppGUI by making it more than a static GUI
    - Correctly implemented the functions involving dynamic Auctions within the Agent class
    - Implemented the dynamic adding of AuctionHouses upon notification and request within the Bank
    - Helped clean up the code and make it compliant with CS351 Code Standards
        - Capitalized the first letter of each sentence
    - Create the documentation and graphs for the Object Design
        - Create the guide section within the readme.me
* **Gavin McGuire** - *Developing work* - [CS351 Project 5 Group 6](https://csgit.cs.unm.edu/mcguireg/)
    - Some changes with the ItemID for an auction
    - Most of the Agent App GUI
    - Comments for GUI 
    - Commented Agent
    - Agent Command Line Interface
    - Created Agent
    - Implemented Interface methods for Agent
        - getItems, addAccount, addAuction
        - handling multiple AuctionHouses
        - implementing these methods into the Auction and Bank Proxy
    - Created AgentApp
    - Helped compile the GUI