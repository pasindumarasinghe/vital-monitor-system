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

	public Monitor parseDatagramObjectFromVitalMonitor(byte[] inData){/* Parses a the datagram object broadcasted by a vital monitor */
		Monitor mon = null;
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(inData);
			ObjectInputStream ois = new ObjectInputStream(bis);
			mon = (Monitor)(ois.readObject());
		} catch(Exception e){
			System.out.println("Error when Parsing UDP Object...Terminating the program");
			e.printStackTrace();
			System.exit(-1);
		}
		return mon;
	}

	public static void main(String[] args){

		Gateway gatewayServer = new Gateway();

		/* A TreeSet is used to store the details of connected vital monitors
		*  tree set guarantees log(n) time cost for the basic operations such as add, remove and contains
		*/
		TreeSet<String> connectedMonitors = new TreeSet<String>();

		try{
			int UDP_RECEIVE_PORT = 7568;
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