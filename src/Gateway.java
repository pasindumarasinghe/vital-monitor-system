/*
E/17/207
Pasindu Marasinghe
*/
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.TreeSet;

public class Gateway{
	/**
	 * Parses a datagram object broadcasted by a vital monitor
	 * @param inData
	 * @return Monitor object whose details were sent by a vital monitor
	 */
	public Monitor parseDatagramObjectFromVitalMonitor(byte[] inData){
		Monitor mon = null;
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(inData);
			ObjectInputStream ois = new ObjectInputStream(bis);
			mon = (Monitor)(ois.readObject()); /* cast the received data into a Monitor object*/
		} catch(Exception e){
			System.out.println("Error when Parsing UDP Object...Terminating the program");
			e.printStackTrace();
			System.exit(-1);
		}
		return mon;
	}

	public static void main(String[] args){

		/*
		* 		- The Gateway receives monitor details UDP broadcast by a vital monitor.
		* 		- The Gateway establishes a TCP connection with the monitor and hand it over to a seperate thread to handle the
		* 		  communication with the vital monitor
		* 		- The main thread keeps listening for new UDP messages from the vital monitors.
		* 		- Once a TCP connection is established with a monitor, the monitor is added to a list of connected monitors so that
		* 		  the gateway won't try to establish connections repeatedly upon receiving broadcasts.
		*/

		Gateway gatewayServer = new Gateway();

		/* A TreeSet is used to store the details of connected vital monitors
		*  tree set guarantees log(n) time cost for the basic operations such as add, remove and contains
		*/
		TreeSet<String> connectedMonitors = new TreeSet<String>();

		try{
			int UDP_RECEIVE_PORT = 7568; /* using a less common port*/
			while(true){
				DatagramSocket dSocket = new DatagramSocket(UDP_RECEIVE_PORT);
				byte[] recvBuf = new byte[2048];
				DatagramPacket dPacket = new DatagramPacket(recvBuf,recvBuf.length);
				dSocket.receive(dPacket);
				Monitor m = gatewayServer.parseDatagramObjectFromVitalMonitor(dPacket.getData());
				String monitorID = m.getMonitorID();

				if(!connectedMonitors.contains(monitorID)){
					connectedMonitors.add(monitorID);
					GatewayConnection conn = new GatewayConnection(m);
					conn.start();
				}
				dSocket.close();
			}
		}
		catch(Exception e){
			System.out.println("Error");
			e.printStackTrace();
		}
	}

}