/*
 * class for a multicast server that accepts and processes
 * UDP datagrams for control logic
 *
 * for more on IPv4 Multicast addresses see
 * http://www.iana.org/assignments/multicast-addresses/multicast-addresses.xml
 *
 * this class is largely based off of
 * http://www.roseindia.net/java/example/java/net/udp/UDPMulticastServer.shtml
 */
package zonecontrol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 
 * @author Jason Zerbe
 */
public class ZoneMulticastServer {

	protected Thread serverThread = null;
	protected ZoneThread myZoneThread = null;
	protected MulticastSocket serverSocket = null;
	protected boolean printNetworkCommandToTerminal = false;

	public ZoneMulticastServer(boolean printNetworkCommandToTerminal) {
		this.printNetworkCommandToTerminal = printNetworkCommandToTerminal;
	}

	public void startServer() {
		myZoneThread = new ZoneThread();
	}

	public void stopServer() {
		System.out.print("ZMS socket and thread stopping ...");

		serverSocket.close();

		serverThread.interrupt();
		try {
			serverThread.join(3000);
		} catch (InterruptedException ex) {
			System.err.println(ex);
		}

		System.out.println("ZMS socket closed and thread joined");

		serverSocket = null;
		serverThread = null;
	}

	protected class ZoneThread implements Runnable {

		public ZoneThread() {
			System.out.println("ZMS thread starting ...");
			serverThread = new Thread(this);
			serverThread.start();
			System.out.println("ZMS thread started");
		}

		@Override
		public void run() {
			try {
				serverSocket = new MulticastSocket(ZoneConstants.getInstance()
						.getGroupPortInt());
			} catch (IOException ex) {
				System.err.println(ex);
			}

			try {
				serverSocket.setTimeToLive(ZoneConstants.getInstance()
						.getGroupTTLInt());
			} catch (IOException ex) {
				System.err.println(ex);
			}

			InetAddress groupAddress = null;
			try {
				groupAddress = InetAddress.getByName(ZoneConstants
						.getInstance().getGroupAddressStr());
			} catch (UnknownHostException ex) {
				System.err.println(ex);
			}

			boolean serverHasJoinedGroup = false;
			while (!serverHasJoinedGroup) {
				try {
					serverSocket.joinGroup(groupAddress);
				} catch (IOException ex) { // unable to join multicast group
					System.err.println(ex);

					try { // pause for 2 seconds to give NIC a chance to connect
						Thread.sleep(2000);
					} catch (InterruptedException ex1) {
						System.err.println(ex1);
					}

					continue; // try again
				}
				serverHasJoinedGroup = true; // finally connected to the group
			}

			System.out.println("ZMS started and listening to group");

			while (!serverSocket.isClosed()) {
				// receive request from client
				final byte[] buffer = new byte[ZoneConstants.getInstance()
						.getGroupMaxByte()];
				final DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length, groupAddress, ZoneConstants
								.getInstance().getGroupPortInt());
				try {
					serverSocket.receive(packet);
				} catch (IOException ex) {
					System.err.println(ex);
				}
				final String theNetworkCommand = new String(buffer).trim()
						.toLowerCase();

				// notify via the console of datagram
				if (printNetworkCommandToTerminal) {
					System.out.println(new Date().toString() + " - recieved:\n"
							+ theNetworkCommand);
				}

				// process said request
				ZoneServerLogic.getInstance().processNetworkCommand(
						theNetworkCommand);
			}
		}
	}
}
