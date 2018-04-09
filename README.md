# RouteControl
Route Control Simulation for Asynchronous Networks, supports up to 25 notes. Utilizes OSPF, Route Advertisement over TCP connections.

To deploy the Route Controller, feed a config.txt file containing the direct connections to the ASN.
The Route Controller will learn of nearby Routes through advertisement updates and will update the local cost to each network periodically.
The config.txt file placed in git is an example.

Current Network max is 25 nodes.

Created using Java 1.8, will work on both Windows and Linux servers.
