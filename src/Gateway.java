/*
Pasindu Marasinghe
*/
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.*;

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

		try{
			int UDP_RECEIVE_PORT = 6000;
			DatagramSocket dSocket = new DatagramSocket(UDP_RECEIVE_PORT);

			byte[] recvBuf = new byte[2048];
			DatagramPacket dPacket = new DatagramPacket(recvBuf,recvBuf.length);
			dSocket.receive(dPacket);

			try{
				Monitor mon1 = gatewayServer.parseDatagramObjectFromVitalMonitor(dPacket.getData());
				System.out.println(mon1.getIp());
			} catch(Exception e){
				System.out.println("Error when casting");
				System.out.println(e);
			}
			
			dSocket.close();
			
		} catch(Exception e){
			System.out.println("Error");
			e.printStackTrace();
		}
	}

}