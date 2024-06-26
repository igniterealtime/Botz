[![Botz CI](https://github.com/igniterealtime/Botz/workflows/Botz%20CI/badge.svg?branch=main)](https://github.com/igniterealtime/Botz/actions?query=workflow%3A%22Botz+CI%22+branch%3Amain)

About
-----
The Botz library adds to the already rich and extensible [Openfire] with the ability to create internal user bots. With the Botz library, programmers may choose to develop a user bot to run as a service bearing myservice@example.com as its JID. To Openfire, the user bot is just like other (human) users.

[Openfire] is a XMPP server licensed under the Open Source Apache License.

[Botz] - an [Ignite Realtime] community project.

Ignite Realtime
===============

[Ignite Realtime] is an Open Source community composed of end-users and developers around the world who 
are interested in applying innovative, open-standards-based Real Time Collaboration to their businesses and organizations. 
We're aimed at disrupting proprietary, non-open standards-based systems and invite you to participate in what's already one 
of the biggest and most active Open Source communities.

[Botz]: http://www.igniterealtime.org/projects/botz/index.jsp
[Openfire]: http://www.igniterealtime.org/projects/openfire/index.jsp
[Ignite Realtime]: http://www.igniterealtime.org
[XMPP (Jabber)]: http://xmpp.org/

## Overview
A common way of creating a new XMPP service is to develop a plugin that will serve the service as a sub domain. That said, if Openfire's domain is **example.com** programmers would develop the new service as an internal or external component and deploy it as **myservice.example.com**.

Botz library adds the already rich and extensible Openfire with the ability to create internal user bots. With Botz library, programmers may choose to develop a user bot to run as a service bearing **myservice@example.com** as its JID. To Openfire, the user bot is just like other (human) users.

Botz library is strictly internal for Openfire. The notion of a user connection doesn't involve any TCP/IP or socket; hence virtual. There isn't even a C2S implementation.

## Botz Classes
Botz library contains **BotzConnection** class that allows a user bot to login as a registered or anonymous user. The class optionally automates the creation and registration of the user bot if it has not existed in the database. To make the user bot useful, programmers would implement **BotzPacketReceiver** interface to respond to received packets. **BotzPacketReceiver.processIncomingPacket(Packet)** will be called for every packet received by the user bot. To send packets to other XMPP entities, programmers in turn call **BotzConnection.sendPacket(Packet)**.

Botz classes may be used in situations where an internal user bot is needed. Botz most likely proves itself useful in the development of Openfire extensions through plugins.

## Key Features
- Login anonymously

- Login as an existing Openfire user

- Optionally create a new user as a registered user (bot) if it does not exist. The newly created user account will be stored in the database. Because user creation is done using SQL statements internal to Openfire, this should work for all Openfire-supported databases.

- The above features hide programmers from handling the connection establishment and allow programmers to focus on packet exchanges.

- Change **BotPacketReceiver** on the fly, thus switch behaviors and create multiple personalities of a bot.

## Using Botz in A Plugin
The following is the code snippet that shows a way to use Botz classes in a plugin. The sample plugin is a parrot bot service that simply echoes *<message/>* packets back to the sender.

```java
import org.igniterealtime.openfire.botz.BotzConnection;
import org.igniterealtime.openfire.botz.BotzPacketReceiver; 

public class ParrotBot implements Plugin
{
    @Override
    public void destroyPlugin() {}
    
    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) 
    {
        BotzPacketReceiver packetReceiver = new BotzPacketReceiver() {
            BotzConnection bot;
            
            public void initialize(BotzConnection bot) {
                this.bot = bot;
            }
            
            public void processIncoming(Packet packet) {
                if (packet instanceof Message) {
                    // Echo back to sender
                    packet.setTo(packet.getFrom());
                    bot.sendPacket(packet);
                }
            }
            public void processIncomingRaw(String rawText) { };
            
            public void terminate() { };
        };  
        
        BotzConnection bot = new BotzConnection(packetReceiver);
        try {
            // Create user and login
            bot.login("parrot");
            Presence presence = new Presence();
            presence.setStatus("Online");
            bot.sendPacket(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Installation
The Botz library is not a plugin in itself, and does not contain any plugin-related class. It is meant for use in an application development.

To use the library in an Openfire plugin, it needs to be defined as a dependency of your plugin project. The dependency can be obtained from Ignite's Maven repository, as shown in this snippet of a pom.xml file:

```xml
<dependencies>
    <dependency>
        <groupId>org.igniterealtime.openfire.botz</groupId>
        <artifactId>botz</artifactId>
        <version>1.3.0</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>igniterealtime</id>
        <name>Ignite Realtime Repository</name>
        <url>https://igniterealtime.org/archiva/repository/maven/</url>
    </repository>
</repositories>
```
