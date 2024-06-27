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
The following is a working sample that shows how to use Botz classes in a plugin. The sample plugin is a parrot bot service that simply echoes *<message/>* packets back to the sender.

`plugin.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<plugin>
    <author>John Doe</author>
    <class>org.example.ParrotBot</class>
    <name>${project.name}</name>
    <description>${project.description}</description>
    <version>${project.version}</version>
    <licenseType>Apache 2.0</licenseType>
    <date>2024-06-27</date>
    <minServerVersion>4.8.0</minServerVersion>
    <minJavaVersion>1.8</minJavaVersion>
</plugin>
```

`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>ParrotBot</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>ParrotBot</name>
    <description>Echo bot for Openfire</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <openfire.version>4.8.0</openfire.version>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.3.1</version>
                <configuration>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <dependencies>
                    <dependency>
                        <groupId>org.igniterealtime.openfire.plugins</groupId>
                        <artifactId>openfire-plugin-assembly-descriptor</artifactId>
                        <version>${openfire.version}</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <appendAssemblyId>false</appendAssemblyId>
                            <finalName>${project.artifactId}</finalName>
                            <attach>false</attach>
                            <descriptorRefs>
                                <descriptorRef>openfire-plugin-assembly</descriptorRef>
                            </descriptorRefs>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.felix</groupId>
                <artifactId>maven-bundle-plugin</artifactId>
                <version>3.0.0</version>
                <extensions>true</extensions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>9</source>
                    <target>9</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>org.igniterealtime.openfire</groupId>
            <artifactId>xmppserver</artifactId>
            <version>${openfire.version}</version>
            <scope>provided</scope>
        </dependency>
    
        <dependency>
            <groupId>org.igniterealtime.openfire.botz</groupId>
            <artifactId>botz</artifactId>
            <version>1.3.0</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>mvnrepository</id>
            <name>Maven Central Repository</name>
            <url>https://repo1.maven.org/maven2/</url>
        </repository>
        <repository>
            <id>sonatype</id>
            <name>Sonatype Repository</name>
            <url>https://oss.sonatype.org/content/repositories/releases/</url>
        </repository>
        <repository>
            <id>igniterealtime</id>
            <name>Ignite Realtime Repository</name>
            <url>https://igniterealtime.org/archiva/repository/maven/</url>
        </repository>
    </repositories>

    <pluginRepositories>
        <!-- Typically used to retrieve Maven plugins that are used by this
        project.
             This apparently is also used to obtain the dependencies _used by_ these
             plugins (such as the openfire-plugin-assembly-descriptor, needed to
             package the project as an Openfire plugin!) -->
        <pluginRepository>
            <id>igniterealtime</id>
            <name>Ignite Realtime Repository</name>
            <url>https://igniterealtime.org/archiva/repository/maven/</url>
        </pluginRepository>
        <pluginRepository>
            <id>mvnrepository</id>
            <name>Maven Central Repository</name>
            <url>https://repo1.maven.org/maven2/</url>
        </pluginRepository>
    </pluginRepositories>
</project>
```
`src/main/java/org/example/ParrotBot.java`
```java
package org.example;

import org.igniterealtime.openfire.botz.BotzConnection;
import org.igniterealtime.openfire.botz.BotzPacketReceiver;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;
import org.xmpp.packet.Message;
import java.io.File;

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
