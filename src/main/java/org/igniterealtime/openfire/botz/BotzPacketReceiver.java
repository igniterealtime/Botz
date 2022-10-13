/**
 * Copyright (C) 2007 Jive Software, 2022 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.igniterealtime.openfire.botz;

import org.xmpp.packet.Packet;

/**
 * BotzPacketReceiver interface is to be used in conjunction with
 * {@link BotzConnection} class as the bot's packet receiver. Classes will
 * normally perform processing of received packets here. Assignment of
 * BotzPacketReceiver object is made via
 * {@link BotzConnection#BotzConnection(BotzPacketReceiver)} constructor or
 * {@link BotzConnection#setPacketReceiver(BotzPacketReceiver)} method.
 * 
 * <p>
 * XMPP packets are passed to BotzPacketReceiver object via
 * {@link #processIncoming(Packet)} method.
 * 
 * @author Aznidin Zainuddin
 * @see BotzConnection
 */
public interface BotzPacketReceiver {
	/**
	 * Called by {@link BotzConnection} login methods or
	 * {@link BotzConnection#setPacketReceiver(BotzPacketReceiver)} to provide
	 * the class implementor a chance to initialize itself before starting to
	 * receive packets. The bot's object reference is passed to this method so
	 * that the implementor can choose to store this object that it can later
	 * use to call the bot's {@link BotzConnection#sendPacket(Packet)} method to
	 * send bot packets to other XMPP entities.
	 * 
	 * @param botzConnection
	 *            The bot object that is being assigned the packet receiver
	 *            object.
	 */
	public void initialize(BotzConnection botzConnection);

	/**
	 * Called by {@link BotzConnection} when a packet arrives for the bot. This
	 * is usually where the implementor starts processing the received packet.
	 * 
	 * @param packet
	 *            The XMPP packet received by the bot.
	 */
	public void processIncoming(Packet packet);

	/**
	 * Called by {@link BotzConnection} whenever a raw text arrives for the bot.
	 * 
	 * @param rawText
	 *            The raw text received by the bot.
	 */
	public void processIncomingRaw(String rawText);

	/**
	 * Called by {@link BotzConnection} whenever the bot's (virtual) connection
	 * is closed so that the class implementor will have the chance to perform
	 * necessary clearance when terminated.
	 */
	public void terminate();
}