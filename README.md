Project Overview:

Requires: Java 1.8+
Uses: Port 1450

Network simulation demonstrating the implementation of Open Shortest Path First (OSPF) with Route Advertisement.

The Route Controller will learn of nearby Routes through advertisement updates and will update the local cost to each network periodically. When sending a packet to a target node, it will select the shortest path to the next hop and each receiving node will make a similar decision.

To run the Route Controller, compile the java files using Java 8 or higher and run RouteController.class. The Route Controller will read from 'config.txt' to learn of its immediate neighbors and will then periodically advertise its knowledge to all known Route Controllers. User commands have been implemented to allow users to  manually test and update the network simulation. 

Current Network max is 25 nodes.

