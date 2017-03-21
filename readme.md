# jMsn (Java MSN Messenger Clone)

jMsn is a MSN Messenger clone with a centralized P2P network. It has 3 modules the client, a common package and the server.

## Client
The client is the package where the actual application lives. The client gets node updates from the server, but the client will always directly to another client

## Server
The server keeps track of all the users(nodes in the P2P network) and pushes updates to nodes that need it, for example a contact of you just signed in.

When that contact logs in the server will broadcast to all his friends that he is logged in.

## Common
This module needs to be in the Client JAR and in the Server JAR we communicate through these objects.

### Technologies
- Java
- JavaFX
- BCrypt
- MySQL
- Hibernate
- Spring
- ControlsFX